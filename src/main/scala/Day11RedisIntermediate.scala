package com.github.Eleonora8510

import com.redis.RedisClient

object Day11RedisIntermediate extends App{
  println("Deeper look into Redis commands")
  val port = 19305
  //val url = scala.util.Properties.envOrElse("REDISHOST","nodatabaseurl")
  val url = "redis-19305.c293.eu-central-1-1.ec2.cloud.redislabs.com"
  //val dbName = "Eleonora-free-db"
  val pw = Some("INSERT THE PASSWORD")

  val r = new RedisClient(host=url, port, 0, secret= pw)
  r.set("computer:type", "Lenovo")

//  //so we either get a List of String Options or we get a blank List of String Options
  val myValues = r.mget("myname", "mycount", "badkey", "number", "favorites:berry").getOrElse(List[Option[String]]()).toArray
  //these are just the values, associate the keys yourself
  for (value <- myValues) {
  println(s"value -> ${value.getOrElse("")}")
}

  //set multiple values at once
  val msetResults = r.mset(("weather","sunny"),("temperature", 25),("favorites:berry","strawberries")) //notice no restrictions on values
  println(s"Mset worked?: $msetResults")

  //let's get all the present keys
  val keys = r.keys().getOrElse(List[Some[String]]()).map(_.getOrElse(""))
  println("My keys are")
  //  keys.foreach(key => println(s"Key $key type is ${key.getClass} value: ${r.get(key.getOrElse(""))}")) //only works on primitives
  keys.foreach(key => println(s"Key $key type is ${key.getClass}"))

  //so we set key user:42 it will have hash fields name, likes , color withs corresponding values
  r.hmset("user:42",Array(("name","Valdis"),
  ("likes","potatoes"),
  ("color","green"),
  ("parkingTickets", 3)
  ))

  val myName = r.hget("user:42", "name").getOrElse("")
  println(s"My name is $myName")

  var parkingTickets = r.hget("user:42", "parkingTickets").getOrElse("0").toInt
  println(s"$myName has got $parkingTickets parking Tickets")

  r.hincrby("user:42", "parkingTickets", 10)
  parkingTickets = r.hget("user:42", "parkingTickets").getOrElse("0").toInt
  println(s"$myName has got $parkingTickets parking Tickets")

  //sortedSets
  //https://redis.io/docs/manual/data-types/data-types-tutorial/#sorted-sets

  r.zadd("hackers",  1940, "Alan Kay")
  r.zadd("hackers",  1957, "Sophie Wilson")
  r.zadd("hackers" ,1912, "Alan Turing")
  r.zadd("hackers", 1969, "Linus Torvalds")

  val hackers = r.zrange("hackers", 0, -1).getOrElse(List[String]())
  println(hackers.mkString(", "))

  //TODO 3 more hackers with their scores/birthyear ( you can use your own or use the ones from redis.io example
  //TODO get all hackers born after 1960 -
  //use zrangebyscore method

  r.zadd("hackers", 1953, "Richard Stallman")
  r.zadd("hackers", 1949, "Anita Borg")
  r.zadd("hackers", 1965, "Yukihiro Matsumoto")

  val hackersAfter1960 = r.zrangebyscore("hackers",  1961, max = 2022, limit = Option(0,1000)).getOrElse(List[String]())
  println(hackersAfter1960.mkString(", "))

  //TODO create a new hash key with at least 5 fields with corresponding values
  //TODO retrieve 3 of those values - you can use hget
  // alternative would be r.hmget

  r.hmset("book:456",Array(("title","The Picture of Dorian Gray"),
    ("author","Oscar Wilde"),
    ("genre","novel"),
    ("language", "English"),
    ("ISBN", "978-0141439570"),
    ("print length", 304),
    ("publisher", "Penguin Classics"),
    ("publication date","February 4, 2003")
  ))

  val title = r.hget("book:456", "title").getOrElse("NO")
  val author = r.hget("book:456", "author").getOrElse("NO")
  val publicationDate = r.hget("book:456", "publication date").getOrElse("NO")
  val pages = r.hget("book:456", "print length").getOrElse("NO")

  println(s"Title -> $title \nauthor -> $author \npublication date -> $publicationDate \nthe number of pages -> $pages")

  val myBook = r.hmget("book:456", "title", "author", "genre", "language").getOrElse(List[Option[String]]()).toArray
  for (value <- myBook) {
    println(s"$value")
  }

}
