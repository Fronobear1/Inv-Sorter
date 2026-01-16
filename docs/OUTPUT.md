# InvSorter — Smart Inventory Preset Auto-Sorter

## Overview
- Client-only Fabric mod for Minecraft 1.21.11 that lets players design up to 4 inventory presets and perform one-click sorting in singleplayer/hardcore.
- Sorting uses item IDs or categories (TOOL, FOOD, BLOCK, MISC). Custom names are ignored to keep renamed items safe.
- Multiplayer is blocked; the SORT menu shows disabled buttons with a clear note.

## Components
- Classes
  - InvSorterClient: Client entrypoint. Loads/saves presets, registers keybind, opens editor.
  - Preset, SlotRule, ItemCategory, PresetManager: Data model and JSON storage for four presets.
  - InventorySorter: Deterministic sorter that snapshots inventory, matches rules, places items, restores on error.
  - PresetEditorScreen: GUI editor with grid, search, category buttons, preset selectors and Save.
  - SortMenuScreen: In-inventory menu to run sorting for Preset 1–4; disabled on multiplayer.
  - InventoryScreenMixin: Injects SORT button into the inventory screen and opens SortMenu.
- Assets
  - assets/invsorter/lang/en_us.json: Localization for key, screens, buttons, tooltips.
- Config
  - config/invsorter_presets.json: JSON file under the run directory storing presets.

## Registration
- fabric.mod.json
  - id: invsorter
  - environment: client
  - client entrypoint: fronobear.invsorter.InvSorterClient
  - mixins: invsorter.mixins.json
- Keybinding
  - Category: invsorter/category.invsorter
  - Default key: H
- Mixin
  - InventoryScreenMixin injects after init to add the SORT button.

## How It Works
1. Press H to open Preset Editor.
   - Select preset 1–4.
   - Click slots to select and assign rules via item search or category buttons.
   - Save writes to config/invsorter_presets.json.
2. Open player inventory; click SORT to open preset menu.
   - If singleplayer (integrated server), choose preset to sort instantly.
   - If multiplayer, buttons are disabled and a tooltip explains the limitation.
3. Sorting flow
   - Snapshot the 36 main slots.
   - Collapse stacks by item ID respecting max stack size.
   - Place exact item ID matches, then fill remaining slots with unmatched items.
   - Apply back to inventory; restore snapshot on any error.

## Notes
- Armor slots are never touched.
- No chat spam, sounds, or animations; sorting is immediate.
