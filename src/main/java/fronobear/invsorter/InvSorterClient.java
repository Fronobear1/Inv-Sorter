package fronobear.invsorter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fronobear.invsorter.client.gui.PresetEditorScreen;
import fronobear.invsorter.client.gui.SortMenuScreen;
import fronobear.invsorter.preset.PresetManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class InvSorterClient implements ClientModInitializer {

    public static final String MOD_ID = "invsorter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static PresetManager presetManager;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static KeyBinding openEditorKey;
    private static KeyBinding openSortMenuKey;

    private static final String[] PRESET_NAMES = {
            "Inv 1", "Inv 2", "Inv 3", "Inv 4"
    };

    @Override
    public void onInitializeClient() {
        MinecraftClient client = MinecraftClient.getInstance();

        Path configPath = client.runDirectory.toPath()
                .resolve("config")
                .resolve("invsorter_presets.json");

        try {
            Files.createDirectories(configPath.getParent());
        } catch (Exception e) {
            LOGGER.error("Failed to create config directory", e);
        }

        presetManager = new PresetManager(GSON, configPath);
        presetManager.load();

        Category category = Category.create(
                Identifier.of(MOD_ID, "category.invsorter")
        );

        openEditorKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.invsorter.open_editor",
                        InputUtil.Type.KEYSYM,
                        InputUtil.GLFW_KEY_H,
                        category
                )
        );

        openSortMenuKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.invsorter.open_sort_menu",
                        InputUtil.Type.KEYSYM,
                        InputUtil.GLFW_KEY_J,
                        category
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (openEditorKey.wasPressed()) {
                mc.setScreen(
                        new PresetEditorScreen(
                                Text.translatable("screen.invsorter.preset_editor"),
                                presetManager
                        )
                );
            }

            while (openSortMenuKey.wasPressed()) {
                mc.setScreen(
                        new SortMenuScreen(
                                Text.translatable("screen.invsorter.sort_menu"),
                                presetManager
                        )
                );
            }
        });

        // ✅ INVENTORY SORT BUTTON (RECIPE BOOK SAFE)
        ScreenEvents.AFTER_INIT.register((client1, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof InventoryScreen invScreen) {

                int bgWidth = 175;
                int bgHeight = 164;
                int guiLeft = (scaledWidth - bgWidth) / 2;
                int guiTop  = (scaledHeight - bgHeight) / 2;

                int x = guiLeft + 205;
                int y = guiTop + 60;

                ButtonWidget sortButton = ButtonWidget.builder(
                        Text.literal("Sort"),
                        btn -> client1.setScreen(
                                new SortMenuScreen(
                                        Text.translatable("screen.invsorter.sort_menu"),
                                        presetManager
                                )
                        )
                ).dimensions(x, y, 37, 17).build();

                Screens.getButtons(screen).add(sortButton);
            }
        });
    }

    public static PresetManager getPresetManager() {
        return presetManager;
    }

    public static String getPresetName(int index) {
        if (index < 0 || index >= PRESET_NAMES.length) {
            return "Inv " + (index + 1);
        }
        return PRESET_NAMES[index];
    }

    public static void setPresetName(int index, String name) {
        if (index < 0 || index >= PRESET_NAMES.length) return;

        String trimmed = name == null ? "" : name.trim();
        PRESET_NAMES[index] = trimmed.isEmpty()
                ? "Inv " + (index + 1)
                : trimmed;
    }
}
