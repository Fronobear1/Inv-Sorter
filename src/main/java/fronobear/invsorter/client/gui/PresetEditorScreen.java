package fronobear.invsorter.client.gui;

import fronobear.invsorter.InvSorterClient;
import fronobear.invsorter.preset.ItemCategory;
import fronobear.invsorter.preset.Preset;
import fronobear.invsorter.preset.PresetManager;
import fronobear.invsorter.preset.SlotRule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PresetEditorScreen extends Screen {
    private final PresetManager manager;
    private int presetIndex = 0;
    private int selectedSlot = 0;
    private TextFieldWidget searchField;
    private TextFieldWidget nameField;
    private List<Item> searchResults = new ArrayList<>();
    private final List<ButtonWidget> searchButtons = new ArrayList<>();
    private final ButtonWidget[] presetButtons = new ButtonWidget[4];
    private final ButtonWidget[] slotButtons = new ButtonWidget[36];

    public PresetEditorScreen(Text title, PresetManager manager) {
        super(title);
        this.manager = manager;
    }

    @Override
    protected void init() {
        int gridLeft = this.width / 2 - 90;
        int gridTop = this.height / 2 - 80;

        int searchLeft = gridLeft - 190;
        int searchTop = gridTop;

        searchField = new TextFieldWidget(this.textRenderer, searchLeft, searchTop - 24, 130, 20, Text.literal("Search"));
        searchField.setChangedListener(this::onSearchChanged);
        addDrawableChild(searchField);

        addDrawableChild(ButtonWidget.builder(Text.literal("Clear"), b -> {
                    manager.getPreset(presetIndex).getRules().clear();
                }).dimensions(searchLeft + 135, searchTop - 24, 45, 20).build());

        nameField = new TextFieldWidget(this.textRenderer, gridLeft, gridTop - 24, 80, 20, Text.literal("Name"));
        nameField.setText(InvSorterClient.getPresetName(presetIndex));
        addDrawableChild(nameField);

        int buttonY = gridTop + 140;
        addDrawableChild(ButtonWidget.builder(Text.translatable("button.invsorter.save"), b -> {
                    InvSorterClient.setPresetName(presetIndex, nameField.getText());
                    refreshPresetButtonLabels();
                    manager.save();
                    close();
                }).dimensions(gridLeft, buttonY, 50, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), b -> close()).dimensions(gridLeft + 55, buttonY, 50, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Rename"), b -> {
                    InvSorterClient.setPresetName(presetIndex, nameField.getText());
                    refreshPresetButtonLabels();
                }).dimensions(gridLeft + 110, buttonY, 70, 20).build());

        int presetsY = buttonY + 26;
        presetButtons[0] = ButtonWidget.builder(Text.literal(InvSorterClient.getPresetName(0)), b -> {
                    presetIndex = 0;
                    nameField.setText(InvSorterClient.getPresetName(presetIndex));
                }).dimensions(gridLeft, presetsY, 55, 20).build();
        presetButtons[1] = ButtonWidget.builder(Text.literal(InvSorterClient.getPresetName(1)), b -> {
                    presetIndex = 1;
                    nameField.setText(InvSorterClient.getPresetName(presetIndex));
                }).dimensions(gridLeft + 60, presetsY, 55, 20).build();
        presetButtons[2] = ButtonWidget.builder(Text.literal(InvSorterClient.getPresetName(2)), b -> {
                    presetIndex = 2;
                    nameField.setText(InvSorterClient.getPresetName(presetIndex));
                }).dimensions(gridLeft, presetsY + 24, 55, 20).build();
        presetButtons[3] = ButtonWidget.builder(Text.literal(InvSorterClient.getPresetName(3)), b -> {
                    presetIndex = 3;
                    nameField.setText(InvSorterClient.getPresetName(presetIndex));
                }).dimensions(gridLeft + 60, presetsY + 24, 55, 20).build();
        for (ButtonWidget btn : presetButtons) {
            addDrawableChild(btn);
        }

        int categoryY = presetsY + 52;
        addDrawableChild(ButtonWidget.builder(Text.literal("TOOL"), b -> setCategory(ItemCategory.TOOL)).dimensions(gridLeft, categoryY, 55, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("FOOD"), b -> setCategory(ItemCategory.FOOD)).dimensions(gridLeft, categoryY + 24, 55, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("BLOCK"), b -> setCategory(ItemCategory.BLOCK)).dimensions(gridLeft, categoryY + 48, 55, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("MISC"), b -> setCategory(ItemCategory.MISC)).dimensions(gridLeft, categoryY + 72, 55, 20).build());

        for (int slot = 0; slot < 36; slot++) {
            int x;
            int y;
            if (slot < 9) {
                x = gridLeft + slot * 20;
                y = gridTop + 100;
            } else {
                int index2 = slot - 9;
                int row2 = index2 / 9;
                int col2 = index2 % 9;
                x = gridLeft + col2 * 20;
                y = gridTop + row2 * 20 + 40;
            }
            final int s = slot;
            ButtonWidget slotButton = ButtonWidget.builder(Text.literal(""), b -> selectedSlot = s)
                    .dimensions(x, y, 18, 18)
                    .build();
            slotButtons[slot] = slotButton;
            addDrawableChild(slotButton);
        }

        int index = 9;

        // Create 20 clickable areas over search items (two columns of 10)
        for (int i = 0; i < 20; i++) {
            int col = i / 10;
            int row = i % 10;
            int bx = searchLeft + col * 80;
            int by = searchTop + row * 20;
            final int idx = i;
            ButtonWidget btn = ButtonWidget.builder(Text.literal(""), b -> {
                if (idx < searchResults.size()) {
                    Identifier id = Registries.ITEM.getId(searchResults.get(idx));
                    if (id != null) {
                        manager.setRule(presetIndex, new SlotRule(selectedSlot, id));
                        searchField.setSelectionStart(0);
                        searchField.setSelectionEnd(searchField.getText().length());
                    }
                }
            }).dimensions(bx, by, 18, 18).build();
            searchButtons.add(btn);
            addDrawableChild(btn);
        }
        populateSearchResults("");
    }

    private void setCategory(ItemCategory cat) {
        manager.setRule(presetIndex, new SlotRule(selectedSlot, cat));
    }

    private void onSearchChanged(String text) {
        populateSearchResults(text);
    }

    private void populateSearchResults(String query) {
        String q = query.toLowerCase(Locale.ROOT).trim();
        String qs = (q.endsWith("s") && q.length() > 1) ? q.substring(0, q.length() - 1) : q;
        List<Item> all = Registries.ITEM.stream()
                .filter(item -> {
                    Identifier id = Registries.ITEM.getId(item);
                    String name = item.getName().getString().toLowerCase(Locale.ROOT);
                    boolean hit = q.isEmpty()
                            || name.contains(q)
                            || name.contains(qs)
                            || (id != null && (id.toString().contains(q) || id.toString().contains(qs)));
                    return hit;
                })
                .collect(Collectors.toList());
        if (q.contains("potion") || qs.contains("potion")) {
            Item[] potions = new Item[] { Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION };
            for (Item pi : potions) {
                if (!all.contains(pi)) {
                    all.add(0, pi);
                }
            }
        }
        searchResults = all.stream().limit(40).collect(Collectors.toList());
    }

    private void refreshPresetButtonLabels() {
        for (int i = 0; i < presetButtons.length; i++) {
            if (presetButtons[i] != null) {
                presetButtons[i].setMessage(Text.literal(InvSorterClient.getPresetName(i)));
            }
        }
        nameField.setText(InvSorterClient.getPresetName(presetIndex));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int gridLeft = this.width / 2 - 90;
        int gridTop = this.height / 2 - 80;
        int searchLeft = gridLeft - 190;
        int searchTop = gridTop;

        drawGrid(context, gridLeft, gridTop);
        drawSearchResults(context, searchLeft, searchTop);
    }

    private void drawGrid(DrawContext ctx, int left, int top) {
        Preset preset = manager.getPreset(presetIndex);
        // Hotbar row: slots 0-8
        for (int i = 0; i < 9; i++) {
            int x = left + i * 20;
            int y = top + 100;
            drawSlot(ctx, x, y, i, preset);
        }
        // Main inventory: slots 9-35
        int index = 9;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = left + col * 20;
                int y = top + row * 20 + 40;
                drawSlot(ctx, x, y, index, preset);
                index++;
            }
        }
    }

    private void drawSlot(DrawContext ctx, int x, int y, int slotIndex, Preset preset) {
        int color = slotIndex == selectedSlot ? 0x80FFFFFF : 0x40000000;
        ctx.fill(x, y, x + 18, y + 18, color);
        SlotRule rule = preset.getRules().get(slotIndex);
        if (rule != null) {
            ItemStack ghost = ghostForRule(rule);
            if (!ghost.isEmpty()) {
                ctx.drawItem(ghost, x + 1, y + 1);
            }
        }
    }

    private ItemStack ghostForRule(SlotRule rule) {
        if (rule.getType() == SlotRule.Type.ITEM_ID) {
            return PresetManager.resolveItemByIdString(rule.getItemId())
                    .map(item -> new ItemStack(item, 1))
                    .orElse(ItemStack.EMPTY);
        }
        return new ItemStack(sampleItemForCategory(rule.getCategory()));
    }

    private Item sampleItemForCategory(ItemCategory cat) {
        switch (cat) {
            case TOOL:
                return Registries.ITEM.get(Identifier.of("minecraft", "diamond_pickaxe"));
            case FOOD:
                return Registries.ITEM.get(Identifier.of("minecraft", "apple"));
            case BLOCK:
                return Registries.ITEM.get(Identifier.of("minecraft", "stone"));
            default:
                return Registries.ITEM.get(Identifier.of("minecraft", "paper"));
        }
    }

    private void drawSearchResults(DrawContext ctx, int left, int top) {
        int count = Math.min(searchResults.size(), 20);
        for (int i = 0; i < count; i++) {
            int col = i / 10;
            int row = i % 10;
            int x = left + col * 80;
            int y = top + row * 20;
            Item item = searchResults.get(i);
            ItemStack stack = new ItemStack(item);
            ctx.fill(x, y, x + 18, y + 18, 0x60000000);
            ctx.drawItem(stack, x + 1, y + 1);
        }
    }

 

 
}

