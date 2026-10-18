# CartNerf

A Paper plugin (Minecraft 1.21, Java 21) that reduces TNT minecart explosion damage to players by a set percentage.

## Commands

| Command | Permission | Description |
| --- | --- | --- |
| `/cartnerf <0-100>%` | `cartnerf.admin` (op) | Set the damage reduction, e.g. `/cartnerf 50%` for half damage |
| `/cartnerf` | `cartnerf.admin` (op) | Show the current reduction |

`0` = normal damage, `100` = no damage. The `%` sign is optional. The value is saved to `plugins/CartNerf/config.yml` and kept across restarts.

Only damage to players is reduced. Mobs and blocks are unaffected.

## Build

```
mvn package
```

Output: `target/CartNerf.jar`. Copy it into the server's `plugins/` folder.
