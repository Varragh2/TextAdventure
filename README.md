# TextAdventure

A library desinged for extensibility and user friendliness.<br>
The library features JsonPrototypeFactory a class that makes it easy to read any subclass of 
MapSite from Json with clear defined semantics into a conencted Maze of MapSites.
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

