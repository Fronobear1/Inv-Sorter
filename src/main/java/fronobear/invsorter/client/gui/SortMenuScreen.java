package fronobear.invsorter.client.gui;

import fronobear.invsorter.InvSorterClient;
import fronobear.invsorter.preset.PresetManager;
import fronobear.invsorter.sort.InventorySorter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class SortMenuScreen extends Screen {
    private final PresetManager manager;
    private boolean multiplayerBlocked;

    public SortMenuScreen(Text title, PresetManager manager) {
        super(title);
        this.manager = manager;
    }

    @Override
    protected void init() {
        MinecraftClient mc = MinecraftClient.getInstance();
        multiplayerBlocked = (mc.getServer() == null) && (mc.getNetworkHandler() != null);

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int w = 80;
        int h = 20;
        int spacing = 6;

        addPresetButton(centerX - w - spacing, centerY - h - spacing, 0);
        addPresetButton(centerX + spacing, centerY - h - spacing, 1);
        addPresetButton(centerX - w - spacing, centerY + spacing, 2);
        addPresetButton(centerX + spacing, centerY + spacing, 3);
    }

    private void addPresetButton(int x, int y, int index) {
        ButtonWidget.Builder builder = ButtonWidget.builder(Text.literal(InvSorterClient.getPresetName(index)), b -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            PlayerEntity player = mc.player;
            if (player != null && !multiplayerBlocked) {
                if (mc.getServer() != null) {
                    var server = mc.getServer();
                    var serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayer != null) {
                        server.execute(() -> InventorySorter.sort(serverPlayer.getInventory(), manager.getPreset(index)));
                    }
                } else {
                    InventorySorter.sort(player.getInventory(), manager.getPreset(index));
                }
                close();
            }
        }).dimensions(x, y, 80, 20);
        ButtonWidget btn = builder.build();
        btn.active = !multiplayerBlocked;
        addDrawableChild(btn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (multiplayerBlocked) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Auto-sort works in singleplayer only"), this.width / 2, this.height / 2 - 40, 0xFF5555);
        }
    }
}
