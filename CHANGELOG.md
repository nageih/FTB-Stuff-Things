# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [21.1.4]

### Added
* Added back the Blazing Mesh, by default only usable in the Netherite Sluice
* Added control via block tags over which meshes may go in which sluice types
  * See `ftbstuff:allowed_meshes/<mesh_type>` tags

### Fixed
* Fixed Tempered Jar voiding results when they could not be inserted to an inventory
* Sluice property values are now written to the config file on startup
* Fixed Netherite Sluice running much too slowly (a misplaced decimal point!)

## [21.1.3]

### Added
* Added a bunch of compressed blocks (3 tiers of compression)

### Fixed
* Fixed CME in tempered jar item output logic

## [21.1.2]

### Added
* Tempered Jars now work with recipes where the inputs only differ by their amounts
  * In this case, the recipe with the greatest quantity of fluid/items t****akes priority
* Added item and fluid capabilities for Tempered Jars

## [21.1.1]

### Changed
* Sluice now stalls if output inventory is full, instead of spilling items into the world

### Fixed
* Fixed Hammer recipe cache getting corrupted in some cases when using Auto-Hammers

## [21.1.0]

### Added
* Initial alpha-test version
