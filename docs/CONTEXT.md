

## 🔧 Mod Info

- **Mod Name:** `InvSorter`
- **Mod ID:** `invsorter`
- **Author:** `FronoBear`
- **Package Name:** `com.FronoBear.InvSorter`
- **Minecraft Version:** `1.21.11`
- **Fabric Loader Version:** `>=0.16.0`
- **Yarn Mappings:** `1.21.11+build.3`
- **Fabric API Version:** `0.140.2+1.21.11``
- **Java Version (JDK):** `21`
- **Mod Type:** `client`, 

---

## 🧠 Mod Functionality

> 📦 MOD CONTEXT — SMART INVENTORY PRESET AUTO-SORTER
1️⃣ MOD PURPOSE (CORE PROBLEM)

Minecraft survival & hardcore players suffer from:

messy inventories

repetitive manual sorting

no way to enforce personal organization styles

Vanilla provides zero inventory management tools.

This mod solves that by allowing players to:

design custom inventory layouts

save up to 4 presets

auto-sort their player inventory instantly

ONLY in singleplayer / hardcore, safely and reliably

2️⃣ MOD SCOPE (STRICT)
✅ INCLUDED

Player inventory ONLY

Main inventory + hotbar

Custom GUI

Inventory presets

One-click auto sort

Item-ID based matching (custom names safe)

Survival & Hardcore focused

❌ EXCLUDED

Chests

Shulkers

Armor slots

Multiplayer servers

Any server-side behavior

3️⃣ COMPATIBILITY

Loader: Fabric

Type: Client-side

Minecraft: 1.21.x+

Works in:

Singleplayer

Hardcore

LAN (cheats allowed)

Auto-sort is disabled in multiplayer servers.

4️⃣ USER FLOW (COMPLETE)
🔹 PHASE A — PRESET CREATION (SETUP MODE)
🔑 Keybind

Default: H

Opens Preset Editor GUI

🧩 Preset Editor GUI
Layout

Inventory-like grid:

Hotbar + main inventory

❌ Armor slots hidden

Search bar at top

Save button

4 preset selector buttons:

Preset 1

Preset 2

Preset 3

Preset 4

🔍 Search Bar Behavior

Filters items by:

Item name

Item ID

Clicking an item places a ghost representation into selected slot

Ghost items:

Represent desired item type

Do NOT represent real inventory items

💾 Saving a Preset

When user clicks Save:

The layout is stored as a template

Each slot saves:

Item ID OR category

Preset overwrites only the selected slot (1–4)

No real inventory changes happen here.

5️⃣ PRESET DATA MODEL

Each preset stores:

Slot index (0–35)

Desired item rule:

Exact item ID (minecraft:diamond_pickaxe)

OR category (TOOL, FOOD, BLOCK, etc.)

🔒 Important

Matching NEVER uses:

Display name

Custom name

Lore

Custom-named items are always safe.

6️⃣ PHASE B — INVENTORY SORTING (GAMEPLAY MODE)
📂 Opening Inventory

Player presses E

Inventory screen opens

🔘 Sort Button

Visible on inventory screen:

Top-right / mid-right

Labeled: SORT

🔽 Sort Menu

Clicking SORT opens:

4 buttons:

Preset 1

Preset 2

Preset 3

Preset 4

⚙️ Sorting Execution

When a preset is selected:

Step 1 — Safety Check

If multiplayer server:

Sorting blocked

Tooltip shown:

“Auto-sort works in singleplayer only”

Step 2 — Inventory Snapshot

Read current player inventory

Copy item stacks into memory

Step 3 — Matching Logic

Match items to preset rules:

Exact item ID first

Then category matches

Stack identical items

Preserve stack limits

Step 4 — Placement

Items placed into target slots

Unmatched items moved to end

Empty slots preserved

Step 5 — Apply

Inventory slots are updated instantly

No animation

No delay

No randomness

7️⃣ HARDCORE / SURVIVAL SAFETY

Sorting does NOT:

Delete items

Merge invalid stacks

Touch armor

All operations are deterministic

If any error occurs:

Inventory is restored from snapshot

Zero item loss guarantee.

8️⃣ MULTIPLAYER BEHAVIOR (CRITICAL)

If player is connected to a server:

SORT button still visible

Preset selection opens

Auto-sort buttons are DISABLED

Tooltip explains limitation clearly

No hidden behavior.
No broken promises.

9️⃣ UX RULES (IMPORTANT)

No chat spam

No sounds

No flashy animations

Instant feedback

ESC always closes GUI safely

This is a serious survival tool, not a toy.

🔟 MOD CLAIMS (WHAT YOU CAN SAY)

✅ “Custom inventory presets”
✅ “One-click inventory sorting”
✅ “Designed for survival & hardcore”
✅ “Handles custom-named items safely”

❌ Do NOT say:

“Works on all servers”

“Universal auto-sort”

1️⃣1️⃣ EXPANSION READY (FUTURE)

Possible future upgrades:

More than 4 presets

Preset export/import

Hotbar-only sort

Category editor

Assisted mode for servers

But v1 stays focused.

---

## 📦 Output Instructions

The AI must:

- ✅ List all **Java source files**  
  - Show full relative paths (e.g., `src/main/java/com/...`)  
  - Give a one-line description of each file's purpose

- ✅ Write **full, working Java code** for every file  
  - No placeholders, stubs, or missing logic  
  - Fully import-ready and compilable

- ✅ Provide a valid, working `fabric.mod.json`

- ✅ Register all components correctly:
  - Items, blocks, screens, overlays, particles, keybinds, etc.  
  - Client or server init logic as appropriate

- ✅ Include all required **import statements** and **annotations**

---

## 🎨 Assets (If Applicable)

If assets are needed, AI must:

- 📂 **List all required assets**, including:
  - Textures (`.png`)
  - Lang files (`lang/en_us.json`)
  - Models and blockstates (`.json`)
  - Sounds or GUI elements

- 📄 **Provide full content** and **exact paths** for each file:
  - Example: `src/main/.../exampleMixin.java`
  - Example: `src/main/resources/assets/[modid]/textures/item/example_item.png`
  - Example: `src/main/resources/assets/[modid]/lang/en_us.json`

---

## 📘 Documentation Output

Also generate a brief documentation file and save it as:

> 📁 `docs/OUTPUT.md`

This file must:

- ✅ Explain what the mod does  
- ✅ Summarize all the components (classes, assets, events)  
- ✅ Describe where everything is registered and how it works  
- ✅ Be written for the user to understand how the mod is structured  
- ✅ Use Markdown formatting (headings, bullets, code blocks)

---

## ⚙️ Optional Features

If applicable, include:

- Mixin setup  
- Config file support (Cloth Config or JSON)  
- Command registration  
- Client/server networking  
- Runtime environment checks  
- Shader or render layer support

---

## ❌ Rules

- ❌ Do not skip any code  
- ❌ Do not use vague notes like “implement this later”  
- ❌ Do not generate placeholder files or comments  
- ✅ Output must be **complete and build-ready** from the start