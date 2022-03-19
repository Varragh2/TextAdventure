# TextAdventure

A library desinged for extensibility and user friendliness.<br>
The library features JsonPrototypeFactory a class that makes it easy to read any subclass of 
MapSite from Json with clear defined semantics into a conencted Maze of MapSites. The library also features
common examples of MapSite like Room that defines a `HashMap<Direction, String>` to describe the sides of the Room.
Use `JsonPrototypeFactory.register("name", name.Class)` to register your new MapSite implementation with JsonPrototypeFactory,
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

The other main problem that this library solves is user input. Each MapSite implementation is passed a reference to Player and the Player's ReadInput when Player moves 
into the MapSite. 
MapSite.setDefaultCommands(), MapSite.setAddCommandsOnEnter(), and MapSite.setCurrentCommands() can be used to customize the ReadInput that is used on MapSite.runComands().
