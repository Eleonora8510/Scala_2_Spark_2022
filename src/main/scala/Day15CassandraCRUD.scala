package com.github.Eleonora8510

import CassandraExample.{getClusterSession, rawQuery, runQuery}

import scala.collection.mutable.ArrayBuffer

case class User(country: String,
                user_email: String,
                first_name: String,
                last_name: String,
                age: Int)

object Day15CassandraCRUD extends App{
  //TODO store host and port also in System Enviroment so no need for outside parties to know your host address
  val host = scala.util.Properties.envOrElse("CassandraHost", "")
  val port = scala.util.Properties.envOrElse("CassandraPort", "").toInt
  val username = "avnadmin"
  val password = scala.util.Properties.envOrElse("CassandraPassword", "")
  val caPath = "./src/resources/certs/ca.pem"
  println("Opening up a connection to Cassandra cluster")
  val (cluster, session) = getClusterSession(host=host, port=port,username=username,password=password, caPath=caPath)

  //so instead of vs_keyspace we could use any name for our database (keyspace)
  val keyspaceCreationQuery =
    """
      |CREATE KEYSPACE IF NOT EXISTS
      |vs_keyspace WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'aiven' : 3 };
      |""".stripMargin

  //should do nothing if keyspace is already present
  rawQuery(session, keyspaceCreationQuery)

  //dropping table from vs_keyspace keyspace/database
  //  session.execute("DROP TABLE IF EXISTS vs_keyspace.users_by_country")

  //will do nothing if users_by_country already exist
  val tableCreationQuery =
    """
      |CREATE TABLE IF NOT EXISTS users_by_country (
      |    country text,
      |    user_email text,
      |    first_name text,
      |    last_name text,
      |    age int,
      |    PRIMARY KEY ((country), user_email)
      |)
      |""".stripMargin

  runQuery(session, "vs_keyspace", tableCreationQuery)


  //we need to convert our default int (4bytes) to 2 byte Short because that Column requires 2 byte integer
  //  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
  //    " VALUES (?, ?,?,?,?)", "US", "john@example.com", "John","Wick",55.toShort)
  //  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
  //    " VALUES (?, ?,?,?,?)", "UK", "mrbean@example.com", "Rowan","Atkinson",65.toShort)
  //  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
  //    " VALUES (?, ?,?,?,?)", "LV", "kk@example.com", "Krišj?nis","Kari?š",60.toShort)

  //if column is regular int then we give it regular int of course
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "US", "john@example.com", "John","Wick",55)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "UK", "mrbean@example.com", "Rowan","Atkinson",65)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "UK", "boris@example.com", "Boris","Johnson",58)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "LV", "kk@example.com", "Krišj?nis","Kari?š",60)
  //unlike SQL where primary key is usually inserted automatically the above queries would keep adding more data
  //here we just have the 4 rows (and also 3 partitions)

  val userResults = session.execute("SELECT * FROM vs_keyspace.users_by_country")
  userResults.forEach(row => println(row))



  //TODO add 2 more users, one from LV, one from LT
  //return Latvian users
  //return Lithuanian users
  //ideally you would not only print but save the users into a case class User - we can look at that later

  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "LT", "j.petraitis@gmail.com", "Jonas","Petraitis", 38)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "LV", "laura.k@gmail.com", "Laura","Kalnina", 27)

  println("Users from Latvia:")
  val usersLV = rawQuery(session, "SELECT * FROM vs_keyspace.users_by_country WHERE country = 'LV'")
  val resultBuffer = ArrayBuffer[User]()
  usersLV.forEach(row=> resultBuffer += User(
    row.getString("country"),
    row.getString("user_email"),
    row.getString("first_name"),
    row.getString("last_name"),
    row.getInt("age")))
  val resultArray = resultBuffer.toArray
  println(resultBuffer.mkString(",\n"))

  println("Users from Lithuania:")
  val usersLT = rawQuery(session, "SELECT * FROM vs_keyspace.users_by_country WHERE country = 'LT'")
  val resultBufferLT = ArrayBuffer[User]()
  usersLT.forEach(row=> resultBufferLT += User(
    row.getString("country"),
    row.getString("user_email"),
    row.getString("first_name"),
    row.getString("last_name"),
    row.getInt("age")))
  val resultArrayLT = resultBufferLT.toArray
  println(resultBufferLT.mkString(",\n"))

  //  val usersLV = session.execute("SELECT * FROM vs_keyspace.users_by_country WHERE country = 'LV'")
  //  println("Users from Latvia:")
  //  usersLV.forEach(row => println(row))
  //
  //  val usersLT = session.execute("SELECT * FROM vs_keyspace.users_by_country WHERE country = 'LT'")
  //  println("Users from Lithuania:")
  //  usersLT.forEach(row => println(row))

  println("Will close our Cassandra Cluster")
  cluster.close()
  println("Connection should be closed now")

}
