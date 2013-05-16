# Petals Log Mongo

Pushing Petals logs to MongoDB.

## Configuration Parameters

    handlers=org.ow2.petals.log.mongo.MongoHandler
    
    # Level for handler
    org.ow2.petals.log.mongo.MongoHandler.level=INFO
    
    # Mongo host. Can define multiple host using space sperated values (sharding)
    org.ow2.petals.log.mongo.MongoHandler.host=localhost
    
    # Mongo port to bind to. Use space delimited values in the same order as host values
    org.ow2.petals.log.mongo.MongoHandler.port=27017
    
    # Mongo collection
    org.ow2.petals.log.mongo.MongoHandler.collection=petalslogs
    
    # Mongo database
    org.ow2.petals.log.mongo.MongoHandler.db=petals
    
    # Mongo login (if any, default to null)
    org.ow2.petals.log.mongo.MongoHandler.login=petals
    
    # Mongo password (if any, dzfault to null)
    org.ow2.petals.log.mongo.MongoHandler.password=petals

As usual with MongoDB, all required stuff (DB, Collection, ...) are created on first call if needed. The only thing needed is a running instance.

## Samples

Here are some log samples from the Mongo console (nothing really wonderful...):

    {
      "_id" : ObjectId("51949feeda0629a85bb565b5"),
      "level" : "INFO",
      "name" : "MongoHandlerTest",
      "thread_id" : 1,
      "message" : "This is a test message", 
      "time_ms" : NumberLong("1368694766172")
    },
    {
      "_id" : ObjectId("5194a077da060d8e3a38c024"),
      "level" : "INFO",
      "name" : "MongoHandlerTest",
      "thread_id" : 1,
      "message" : "This is a test message",
      "time_ms" : NumberLong("1368694903435")
    }

