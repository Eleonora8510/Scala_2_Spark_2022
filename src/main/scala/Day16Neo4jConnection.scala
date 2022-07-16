package com.github.Eleonora8510

import org.neo4j.driver.Values.parameters
import org.neo4j.driver.{AuthTokens, Config, GraphDatabase}

import scala.collection.mutable.ArrayBuffer

case class Movie(
                 title: String,
                 released: Int,
                 tagline: String
                 )

object Day16Neo4jConnection extends App{
  println("Let's connect to our Neo4J database!")

  val noSSL = Config.builder().build() //so standard default configuration without SSL connection
  val pw = scala.util.Properties.envOrElse("Neo4jPassword", "N/A")
  val user = scala.util.Properties.envOrElse("Neo4jUser", "N/A")
  val uri = scala.util.Properties.envOrElse("Neo4jURI", "N/A")

  val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pw), noSSL) // <p
  println("Opening Session")
  val session = driver.session

  //val cypherQuery = "MATCH (m:Movie {title: 'The Matrix'}) RETURN m as movie"
  val cypherQuery = "MATCH (m:Movie) RETURN m as movie, id(m) as id"
  val result = session.run(cypherQuery, parameters())

  //TODO read result(s)
  val arrayBuffer = ArrayBuffer[String]()
  while (result.hasNext) {
    val record = result.next //we know this will not fail because we just checked with hasNext
    val movie = record.get("movie")
    arrayBuffer += s"${movie.get("title")} - ${movie.get("released")} : ${movie.get("tagline")}, ${movie.get("id")}}"
  }



  val movies = arrayBuffer.toArray
  println(s"We got ${movies.length} movies!")
  movies.take(5).foreach(println)

  //TODO set up your own Movies database on https://neo4j.com/cloud/platform/aura-graph-database/?ref=neo4j-home-hero
  //TODO get all movies as case classes - with all attributes - title, released,tagline and id - i think that is it
  //TODO print earliest 10 movies in this database

  //val queryMovies = "MATCH (m:Movie {title: 'The Matrix'}) RETURN m as movie"

  val queryMovies = "MATCH (m:Movie) RETURN m as movie"
  val resultMovies = session.run(queryMovies, parameters())

  val movieBuffer = ArrayBuffer[Movie]()

  while (resultMovies.hasNext) {
    val record = resultMovies.next
    val result = record.get("movie")
    val movie = Movie(
      result.get("title").asString,
      result.get("released").asInt,
      result.get("tagline").asString)
    movieBuffer += movie
  }

  val movieArray = movieBuffer.toArray
  println(movieArray.mkString(",\n").take(5))

  println("Closing Session")
  session.close() //important to close the session

}
