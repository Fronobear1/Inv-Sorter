package fronobear.invsorter.preset;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class SlotRule {
    public enum Type {
        ITEM_ID,
        CATEGORY
    }

    private int slotIndex; // 0-35
    private Type type;
    private String itemId; // when type == ITEM_ID
    private ItemCategory category; // when type == CATEGORY

    public SlotRule() {}

    public SlotRule(int slotIndex, Identifier itemId) {
        this.slotIndex = slotIndex;
        this.type = Type.ITEM_ID;
        this.itemId = itemId.toString();
    }

    public SlotRule(int slotIndex, ItemCategory category) {
        this.slotIndex = slotIndex;
        this.type = Type.CATEGORY;
        this.category = category;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public Type getType() {
        return type;
    }

    public String getItemId() {
        return itemId;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public boolean matches(Item item) {
        if (type == Type.ITEM_ID) {
            Identifier id = Registries.ITEM.getId(item);
            return id != null && id.toString().equals(itemId);
        } else {
            return ItemCategory.classify(item) == category;
        }
    }
}

