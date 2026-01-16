package fronobear.invsorter.sort;

import fronobear.invsorter.preset.Preset;
import fronobear.invsorter.preset.SlotRule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;
import java.util.stream.Collectors;

public class InventorySorter {
    public static void sort(PlayerInventory inv, Preset preset) {
        List<ItemStack> snapshot = snapshot(inv);
        try {
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack stack : snapshot) {
                if (!stack.isEmpty()) {
                    items.add(stack);
                }
            }
            ItemStack[] target = new ItemStack[36];
            Arrays.fill(target, ItemStack.EMPTY);

            List<ItemStack> unmatched = new ArrayList<>();

            for (int slot = 0; slot < 36; slot++) {
                SlotRule rule = preset.getRules().get(slot);
                if (rule == null) continue;
                Optional<ItemStack> match = findMatch(items, rule);
                if (match.isPresent()) {
                    ItemStack stack = match.get();
                    target[slot] = stack;
                    items.remove(stack);
                }
            }

            unmatched.addAll(items);

            int fillIndex = 0;
            for (int i = 0; i < 36; i++) {
                if (target[i].isEmpty()) {
                    while (fillIndex < unmatched.size() && unmatched.get(fillIndex).isEmpty()) {
                        fillIndex++;
                    }
                    if (fillIndex < unmatched.size()) {
                        target[i] = unmatched.get(fillIndex);
                        fillIndex++;
                    }
                }
            }

            apply(inv, target);
        } catch (Throwable t) {
            restore(inv, snapshot);
        }
    }

    private static List<ItemStack> snapshot(PlayerInventory inv) {
        List<ItemStack> copy = new ArrayList<>(36);
        for (int i = 0; i < 36; i++) {
            copy.add(inv.getStack(i).copy());
        }
        return copy;
    }

    private static void restore(PlayerInventory inv, List<ItemStack> snapshot) {
        for (int i = 0; i < 36 && i < snapshot.size(); i++) {
            inv.setStack(i, snapshot.get(i).copy());
        }
    }

    private static void apply(PlayerInventory inv, ItemStack[] target) {
        for (int i = 0; i < 36 && i < target.length; i++) {
            inv.setStack(i, target[i].copy());
        }
    }

    private static Optional<ItemStack> findMatch(List<ItemStack> items, SlotRule rule) {
        for (ItemStack stack : items) {
            if (rule.matches(stack.getItem())) {
                return Optional.of(stack);
            }
        }
        return Optional.empty();
    }

    // collapseStacks removed to preserve NBT data like enchantments and container contents
}
