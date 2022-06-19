package com.github.Eleonora8510

import scala.io.StdIn.readLine

object Day2ViewMapFilterReduceExercise {
  println("Ask user for input")
  val number = readLine("Please Enter a number ").toInt // you could add error checking
  println(number)

  val numbers = (0 to number).toArray
  println(s"All numbers: "+numbers.mkString(","))

  val oddSquares = numbers.map(n => n*n).filter(n => n%2 == 1)
  println(s"Only odd squares of numbers " + oddSquares.mkString(","))

  //def mult(x: Int, y: Int):Int  = x*y

  val productOfOddSquares = numbers.view.map(n => n*n).filter(n => n%2 == 1).reduce(_*_)

  println(s"Product of odd squares is  $productOfOddSquares")


  // println product(multiplication) of odd squares of numbers from 1 to user input
  //so user enters 5
  // answer would be 1*9*25 = 225

  // so for reduce part you should define mult function or you could write it inline but that is a bit harder

  // goal would be to use view map filter reduce in a single line
  //you can do it first in separate operations of course

}
