package com.github.Eleonora8510

import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.{and, equal, gte, or}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import java.lang.Thread.sleep
import scala.collection.mutable.ArrayBuffer

object Day13MongoDB_Exercise extends App{

  val userName = scala.util.Properties.envOrElse("MongoDBClient", "nothing")
  val pw = scala.util.Properties.envOrElse("MongoPassword", "nothing")
  println(s"Connecting with user $userName")

  val uri: String = s"mongodb+srv://$userName:$pw@cluster0.ydtbmmc.mongodb.net/?retryWrites=true&w=majority"
  val client: MongoClient = MongoClient(uri)

  val db: MongoDatabase = client.getDatabase("sample_restaurants")

  val collectionName = "restaurants"
  val collection = db.getCollection(collectionName)

  val resultsBuffer = ArrayBuffer[Document]()

  //TODO find ALL restaurants in Manhattan offering barbeque OR BBQ  in name (maybe try cuisine as well)

  val allRestaurants = collection.find(and(equal("borough", "Manhattan"),
                                    or(Filters.regex("name", ".*(b|B)arbe(q|c)ue.*"),
                                           Filters.regex("name", ".*B(b|B)(q|Q).*"),
                                         Filters.regex("cuisine", ".*(B|b)arbe(c|q)ue.*"))))
    .subscribe((doc: Document) => {
      resultsBuffer += doc
    },
      (e: Throwable) => println(s"Query error: $e"),
            afterQuerySuccess
    )
   println("Query is still running - Data is not guaranteed to be ready")

  def afterQuerySuccess() : Unit = {
    println("Closing after last query")
    val allRestaurantsDocs = resultsBuffer.toArray
    println(s"We got ${allRestaurantsDocs.length} restaurants total")
    println("First restaurant")
    println(allRestaurantsDocs.head.toJson())
    val savePath = "src/resources/json/restaurants.json"
    val restaurantJSON = allRestaurantsDocs.map(_.toJson())
    Util.saveLines(savePath, restaurantJSON)
    client.close()
  }


}
