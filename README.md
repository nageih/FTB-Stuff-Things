# FTB Stuff & Things

FTB Stuff & Things is our general use mod for adding new content to our modpacks that don't quite fit into our other mods. This is mostly a collection of small features and blocks that our packs have needed over the years. 

This mod is designed to be used with a modpack so it is not recommended to use this mod on its own as there are no recipes for a start...

## Features

- 🫧 **Sluices**: A block that can be used to wash materials using a variety of fluids. It can be used to wash ores, crops, and other items to get additional resources.
- 🫙 **Jar**: Jars are a both a storage solution for fluids and also a new way to craft items using items and fluids.
- 💧 **Dripper**: A block that can be used to slowly drip fluids into the world. It is compatible with most fluids and can be used to convert blocks into other blocks through the use of fluids. (Recipe powered)
- ♻️ **Item Recycler**: A block that can be used to recycle items into other items. It can be used to recycle items that are no longer needed or to get additional resources from items. (Recipe powered)
- ❄️ **Fusing & Coolers**: These are used to fuse materials into new forms or supercool them to get additional resources. (Recipe powered)
- ⚒️ **Hammers**: Hammers are used to crush blocks into smaller pieces. This is your typical cobble to gravel to sand to dust type of block. This is recipe powered and can be used to get additional resources from blocks.
- 📦 **Crates & Barrels**: These are basic loot dropping blocks that will drop items when broken. These are modified by updating their own blocks loot table.

## Integration

- We natively support `JEI` for all our custom recipes
- There is KubeJS support for recipe schema declaration
- We're open to suggestions for other mods to integrate with

## KubeJS recipe schemas

You can get an instance of the recipe object like this (we'll use this for all examples below):

```javascript
const ftbstuff = event.recipes.ftbstuff;
```

### Crook
`ftbstuff.crook(<list of output-with-chance>, <input>)`

`ftbstuff.crook(<list of output-with-chance>, <input>, <max-drops>, <replace-existing-drops>)`

Optional arguments:
* `<max-drops>` is the max items which can be dropped in one break operation, and defaults to 0 (no limit) if omitted
* `<replace-existing-drops>` defaults to true if omitted; when true, Crook drops completely replace the existing block's loot (e.g. in the case of leaves, sticks/saplings)

#### Example

Give breaking leaves a 50% chance to drop one iron or two gold nuggets (but only one of these drops), and continue to allow default leaves drops (saplings/sticks):

```javascript
ftbstuff.crook([
          { "item": { "item" : "minecraft:gold_nugget", "count" : 2 }, "chance": 0.5 },
          { "item": "minecraft:iron_nugget", "chance": 0.5 }
], {"tag": "minecraft:leaves"}, 1, false);
```

### Hammer (including Auto-Hammers)

`ftbstuff.hammer(<list-of-itemstack-results>, <item-ingredient>`

#### Example

```javascript
ftbstuff.hammer([ "9x iron_ingot ], "minecraft:iron_block")
```

Drop 9 iron ingots when an iron block is broken with any hammer:

### Tempered Jar

`ftbstuff.jar(<sized-item-ingredient-list>, <sized-fluid-ingredient-list>, <output-items>, <output-fluids>)`

`ftbstuff.jar(<sized-item-ingredient-list>, <sized-fluid-ingredient-list>, <output-items>, <output-fluids>, <temperature>, <processing-time>, <repeatable>, <gamestage>)`

Optional arguments:
* `<temperature>` is the required temperature: one of "normal" (default), "hot", "superheated", "chilled"
* `<processing-time>` is time in ticks to run one craft, default 200
* `<repeatable>` boolean - can the recipe be repeated if a Jar Automater is on the jar, default true
* `<gamestage>` game stage required, default "" (no requirement)

### Fusing Machine (aka SlowMelter 9000)

`ftbstuff.fusing_machine(<output-fluidstack>, <list-of-inputs>, {"fe_per_tick": <fe-per-tick>, "ticks_to_process": <processing-time> } )`

#### Example

```javascript
ftbstuff.fusing_machine(Fluid.of("pneumaticcraft:lpg", 1000), [ "minecraft:sugar" ], { "fe_per_tick": 70, "ticks_to_process": 50 })
```

Converts 1 sugar to 1000mB of PneumaticCraft LPG, using 70 FE/t, and taking 50 ticks (so 3500 FE in total):

### "Super" Cooler

`ftbstuff.supercooler(<itemstack-result>, <item-ingredient-list>, <sized-fluid-ingredient>, {"fe_per_tick": <fe-per-tick>, "ticks_to_process": <processing-time> })`

#### Example

```javascript
ftbstuff.supercooler("minecraft:mud", [ "minecraft:dirt", "minecraft:clay" ], Fluid.of("minecraft:water", 1000), {"fe_per_tick": 50, "ticks_to_process": 20 })
```

Converts 1 dirt & 1 clay to 1 mud, using 50 FE/t, taking 20 ticks.

### Temperature Source

`ftbstuff.temperature_source(<blockstate>, <temperature>)`

`ftbstuff.temperature_source(<blockstate>, <temperature>, <efficiency>, <display-item>, <hide-from-jei>)`

`<temperature>` is the same temperature used by Tempered Jar recipes.

Optional arguments:
* `<efficiency>` - a floating point value which acts a multiplier for jar recipe processing speed, default: 1.0
* `<display-item>` - in case the blockstate doesn't have a suitable item to use (e.g. `minecraft:fire`), an item for JEI display purposes, default: empty stack
* `<hide-from-jei>` - boolean value, if true, temperature source not shown in JEI, default: false)

**KubeJS bug!** Although the recipe schema uses a recipe component which should allow empty item stacks, KubeJS refuses to actually allow them. So for now at least, the `<display-item>` has to be specified.

#### Example

```javascript
ftbstuff.temperature_source("minecraft:campfire[lit=true]", "hot", 1.0, "minecraft:campfire")
```

Makes lit campfires act as a hot temperature source.

## Support

- For **Modpack** issues, please go here: https://go.ftb.team/support-modpack
- For **Mod** issues, please go here: https://go.ftb.team/support-mod-issues
- Just got a question? Check out our Discord: https://go.ftb.team/discord

## Licence

All Rights Reserved to Feed The Beast Ltd. Source code is `visible source`, please see our [LICENSE.md](/LICENSE.md) for more information. Any Pull Requests made to this mod must have the CLA (Contributor Licence Agreement) signed and agreed to before the request will be considered.

## Keep up to date

[![](https://cdn.feed-the-beast.com/assets/socials/icons/social-discord.webp)](https://go.ftb.team/discord) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-github.webp)](https://go.ftb.team/github) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-twitter-x.webp)](https://go.ftb.team/twitter) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-youtube.webp)](https://go.ftb.team/youtube) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-twitch.webp)](https://go.ftb.team/twitch) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-instagram.webp)](https://go.ftb.team/instagram) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-facebook.webp)](https://go.ftb.team/facebook) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-tiktok.webp)](https://go.ftb.team/tiktok)

