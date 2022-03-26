# TextAdventure
This repo is supposed to provide easily extensible classes all built around creating a text adventure game of connected rooms, with a dynamic flow of user input.


The text adventure game consists of a `Player` and `MapSite`s that can print descriptions from `MapSite.getDescription(Direction)` and are connected to other `MapSite`s. Every `MapSite` keeps a hashmap of all the other adjacent `MapSite`s and their `Direction`. This makes it easy for the `Player` to move between `MapSite`s, using `MapSite.enter(Player, ReadInput<MapSite>)` and `MapSite.leave()`. The repo also features common examples of `MapSit`e like `Room` that defines a `HashMap<Direction, String>` to describe the sides of the `Room`. It is easy to read in `Room`s, or any subclass of `MapSite` from a Json file using `JsonPrototypeFactory`(more details in Instructions). `MazeBuilder` is also an option to create a connected maze of `MapSite`s. 

`ReadInput` is a class that handles asking the user for input. `ReadInput` is dynamic and specific to the `MapSite` that `Player` is currently in. Every `MapSite` can override the default commands passed to it by `Player` and include its own logic for responding to the user input. 

# Instructions

Use `JsonPrototypeFactory.register("name", name.Class)` to register your new `MapSite` implementation with `JsonPrototypeFactory`,
so that you can read it in from file:

    {
      "column": [
        {
          "class": "name",
          "object": {
            //Read in all your data here
          }
        }
      ]
    }


The other main problem that this library solves is user input. Each `MapSite` implementation is passed a reference to `Player` and the `Player`'s `ReadInput` when `Player` moves into the `MapSite`. `MapSite.setDefaultCommands()`, `MapSite.setAddCommandsOnEnter()`, and `MapSite.setCurrentCommands()` can be used to customize the `ReadInput` that is used on `MapSite.runComands()`.
