# EnhancedRegions | Added functionality for WorldGuard regions

EnhancedRegions adds new functionality to your WorldGuard regions, including keep inventory, teleporting upon region
enter, and custom messages upon teleport. This software is a work in progress, and not all functionality is complete or
stable.

## Installing EnhancedRegions

1. Download the latest [release](https://github.com/TheD4nM4n/EnhancedRegions/releases/).
2. Place the .jar file in your server's `plugins`  directory, alongside
   [WorldGuard](https://dev.bukkit.org/projects/worldguard), [WorldEdit](https://dev.bukkit.org/projects/worldedit),
   and [WorldGuardEvents](https://www.spigotmc.org/resources/worldguard-events.65176/).
3. Profit!

## Using EnhancedRegions
EnhancedRegions utilizes custom WorldGuard region flags to make setup as painless as possible. There are currently three
custom flags introduced with this plugin:

|         Flag         |                                   Description                                   |   Type   |
| -------------------- | ------------------------------------------------------------------------------- | :------: |
| keep-inventory       | When enabled, any player who dies within this region will not lose their items. |   State  |
| teleport-coordinates | Coordinates to teleport players to upon entering the region.                    | Location |
| message-on-teleport  | Message to send to players when teleported after entering the region.           |  String  |

To learn how to use these flags, check out
[WorldGuard's documentation](https://worldguard.enginehub.org/en/latest/regions/flags/).

# License

EnhancedRegions is licensed under the GNU General Public License (v3). All contents within this repository fall under
said license. License can be found in
[COPYING.txt](https://github.com/TheD4nM4n/EnhancedRegions/blob/master/COPYING.txt).
