package fronobear.invsorter.preset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fronobear.invsorter.InvSorterClient;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PresetManager {
    private final Gson gson;
    private final Path path;
    private final Logger logger = InvSorterClient.LOGGER;
    private final List<Preset> presets = new ArrayList<>(Arrays.asList(new Preset(), new Preset(), new Preset(), new Preset()));

    public PresetManager(Gson gson, Path path) {
        this.gson = gson;
        this.path = path;
    }

    public List<Preset> getPresets() {
        return presets;
    }

    public Preset getPreset(int index) {
        return presets.get(index);
    }

    public void setRule(int presetIndex, SlotRule rule) {
        presets.get(presetIndex).setRule(rule);
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(path)) {
            gson.toJson(serialize(), w);
        } catch (IOException e) {
            logger.error("Failed to save presets", e);
        }
    }

    public void load() {
        if (!Files.exists(path)) {
            save();
            return;
        }
        try (Reader r = Files.newBufferedReader(path)) {
            Type t = new TypeToken<Map<String, Map<String, String>>[]>() {}.getType();
            Map<String, Map<String, String>>[] arr = gson.fromJson(r, t);
            if (arr != null) {
                for (int i = 0; i < Math.min(arr.length, 4); i++) {
                    Map<String, Map<String, String>> slotMap = arr[i];
                    Preset preset = new Preset();
                    for (Map.Entry<String, Map<String, String>> e : slotMap.entrySet()) {
                        int slot = Integer.parseInt(e.getKey());
                        Map<String, String> ruleMap = e.getValue();
                        String type = ruleMap.get("type");
                        if ("ITEM_ID".equals(type)) {
                            preset.setRule(new SlotRule(slot, Identifier.tryParse(ruleMap.get("itemId"))));
                        } else if ("CATEGORY".equals(type)) {
                            preset.setRule(new SlotRule(slot, ItemCategory.valueOf(ruleMap.get("category"))));
                        }
                    }
                    presets.set(i, preset);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load presets", e);
        }
    }

    private Map<String, Map<String, String>>[] serialize() {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>>[] arr = new Map[4];
        for (int i = 0; i < 4; i++) {
            Map<String, Map<String, String>> slotMap = new HashMap<>();
            for (SlotRule rule : presets.get(i).getRules().values()) {
                Map<String, String> ruleMap = new HashMap<>();
                ruleMap.put("type", rule.getType().name());
                if (rule.getType() == SlotRule.Type.ITEM_ID) {
                    ruleMap.put("itemId", rule.getItemId());
                } else {
                    ruleMap.put("category", rule.getCategory().name());
                }
                slotMap.put(String.valueOf(rule.getSlotIndex()), ruleMap);
            }
            arr[i] = slotMap;
        }
        return arr;
    }

    public static Optional<Item> resolveItemByIdString(String id) {
        Identifier identifier = Identifier.tryParse(id);
        if (identifier == null) return Optional.empty();
        Item item = Registries.ITEM.get(identifier);
        if (item == null) return Optional.empty();
        return Optional.of(item);
    }
}

