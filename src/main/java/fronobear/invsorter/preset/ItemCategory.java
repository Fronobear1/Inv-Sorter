package fronobear.invsorter.preset;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public enum ItemCategory {
    TOOL,
    FOOD,
    BLOCK,
    MISC;

    public static ItemCategory classify(Item item) {
        if (item instanceof BlockItem) {
            return BLOCK;
        }
        if (item.getComponents().contains(DataComponentTypes.FOOD)) {
            return FOOD;
        }
        if (item.getComponents().contains(DataComponentTypes.TOOL)) return TOOL;
        return MISC;
    }
}
