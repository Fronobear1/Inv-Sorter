package fronobear.invsorter.preset;

import java.util.HashMap;
import java.util.Map;

public class Preset {
    private final Map<Integer, SlotRule> rules = new HashMap<>();

    public void setRule(SlotRule rule) {
        rules.put(rule.getSlotIndex(), rule);
    }

    public void removeRule(int slotIndex) {
        rules.remove(slotIndex);
    }

    public Map<Integer, SlotRule> getRules() {
        return rules;
    }
}

