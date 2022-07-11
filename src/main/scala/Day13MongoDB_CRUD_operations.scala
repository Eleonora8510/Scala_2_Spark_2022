package com.github.Eleonora8510

import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import java.lang.Thread.sleep
import scala.collection.mutable.ArrayBuffer

object Day13MongoDB_CRUD_operations extends App{
  println("Exploring CRUD operations in MongoDB")

  val userName = scala.util.Properties.envOrElse("MongoDBClient", "nothing")
  val pw = scala.util.Properties.envOrElse("MongoPassword", "nothing")
  println(s"Connecting with user $userName")

  val uri: String = s"mongodb+srv://$userName:$pw@cluster0.ydtbmmc.mongodb.net/?retryWrites=true&w=majority"
  //here we connect to the MongoDB cluster
  val client: MongoClient = MongoClient(uri)

  // connecting to the actual database - single cluster could have many databases
  val db: MongoDatabase = client.getDatabase("sample_restaurants")

  //we connect to the collection (which is roughly similar to table in SQL)
  val collectionName = "restaurants"
  val collection = db.getCollection(collectionName)

  val resultsBuffer = ArrayBuffer[Document]()

//  val firstRestaurant = collection.find().first()
//  println(firstRestaurant)

  val allRestaurants = collection.find() // find() is similar SQL SELECT * FROM restaurants
    .subscribe((doc: Document) => {
      resultsBuffer += doc //so each document (row of JSON) will be added to our buffer
          },
      (e: Throwable) => println(s"Query error: $e"),
      //this is what we can do after the query is finished
      () => {
        println("Closing after last query")
        //so idea is to close after last query is complete
        val allRestaurants = resultsBuffer.toArray
        println(s"We got ${allRestaurants.length} restaurants total")
        client.close()
      }
    )
  //this line should run before our closing line
  println("Query is still running")

  //TODO move on success query here
  def afterQuerySuccess() : Unit = {

  }


}
