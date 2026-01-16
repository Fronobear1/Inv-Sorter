package fronobear.invsorter.mixin;

import fronobear.invsorter.InvSorterClient;
import fronobear.invsorter.client.gui.SortMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T widget);

    @Inject(method = "init", at = @At("TAIL"))
    private void invsorter$addSortButton(CallbackInfo ci) {
        InventoryScreen self = (InventoryScreen) (Object) this;
        int x = self.width - 90;
        int y = self.height / 2 - 10;
        ButtonWidget sortButton = ButtonWidget.builder(Text.literal("SORT"), btn -> {
                    MinecraftClient.getInstance().setScreen(new SortMenuScreen(Text.translatable("screen.invsorter.sort_menu"), InvSorterClient.getPresetManager()));
                })
                .dimensions(x, y, 70, 20)
                .build();
        addDrawableChild(sortButton);
    }
}
