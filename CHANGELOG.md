# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [21.1.9]

### Added
* More wooden Sluice types; there are now Sluices for every vanilla wood type

### Changed
* Cleaned up some block model JSONs, better block model re-use and more datagen (no player visible changes)

## [21.1.8]

### Fixed
* Hotfix for last release: fixed cobblegen config getting lost

## [21.1.7]

### Added
* Added a Basalt Generator, similar to Cobblestone Generator
  * No recipe added for the block
* Added ru_ru translation (thanks ~BazZziliuS)

### Fixed
* Disabled wrench-rotation for Sluices, since they're a 2-part multiblock
  * Orphaned sluice funnels left by previous version can now be broken normally

## [21.1.6]

### Fixed
* Fixed loot table summary in JEI not drawing items in the last slot
* Auto-Hammers can now output to other Auto-Hammers; an inventory in between is no longer required (but still works)
* Reversed orientation when placing down Auto-Hammer blocks so input is to the left and output is to the right
* Drippers now autofill themselves from a water source block placed directly above
  * Note: only water works like this, not any other fluids

## [21.1.5]

### Fixed
* Fixed fluid usage modifiers not working for different tiers of sluice
* Sluice items now all have tooltips showing their speed/fluid/etc. modifiers and abilities
* Auto Hammers now correctly update their input/output locations when they're rotated by a wrench
* Auto Hammers client animation playing when machine is not active
* Auto Hammers not creating output buffer when no storage next to output

## [21.1.4]

### Added
* Initial public release

