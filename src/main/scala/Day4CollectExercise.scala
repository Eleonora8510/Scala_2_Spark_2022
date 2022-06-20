package com.github.Eleonora8510

object Day4CollectExercise extends App{
  println("Day 4 Exercise on Partial Functions and collect")

  val numbers = (-1 to 28).toArray

  val getEvenSquare = new PartialFunction[Int, Long] {
    def apply(i: Int): Long = i*i
    def isDefinedAt(i: Int) = i % 2 == 0 && i >0
  }

  val getOddCube = new PartialFunction[Int, Long] {
    def apply(n: Int): Long = n*n*n
    def isDefinedAt(n: Int) = n % 2 != 0 && n > 0
  }

  val evenSquareOrOddCube = getEvenSquare orElse getOddCube
  val processedNumbers = numbers.collect(evenSquareOrOddCube)
  println(processedNumbers.mkString(","))

}
