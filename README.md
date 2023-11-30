# ItemGator

Looking for an easy way to prevent players from having *anything* to do with an item until they've made it further
through progression? Then look no further! ItemGator allows you to use datapack-style recipes in order to prevent
players from being able to have an item until some other process - be it an FTB Quest Reward, a Per-Tick `mcfunction` or
some other means - adds a tag to the player.

When the tag isn't present on the player, the item will turn into whatever item is defined as a substitute and all of
the data for the original item will be stored on the substitute as NBT.

When the tag *is* present, the mod will use that NBT data on the item to revert it back to its original form, allowing
you to push the use of an item to a later time.

## Datapack examples:

### Disallow access to an entire mod until the tag `test123` is given to the player

In this example, the mod Immersive Engineering is being gated from any player that doesn't have the tag `test123`. When
a player without that tag obtains *any* Immersive Engineering item, it'll turn into a stick until they are granted that
tag.

```json
{
  "type": "itemgator:mod",
  "mod": "immersiveengineering",
  "substitute": {
    "item": "minecraft:stick"
  },
  "tag": "test123"
}

```

### Disallow an item or item tag until the tag `test123`is given to the player

In this example, any `minecraft:planks` will be turned into a stick until the player is granted the tag `test123`:

```json
{
  "type": "itemgator:ingredient",
  "input": {
    "tag": "minecraft:planks"
  },
  "substitute": {
    "item": "minecraft:stick"
  },
  "tag": "test123"
}
```