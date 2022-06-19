package com.github.Eleonora8510

object Day3MatchingParenthesis {
  def areParenthesisMatching(text:String):Boolean={
    //TODO write if all opening ( are properly )
    // () ->true
    // (()()) -> true
    // )()( -> false
    // ((()) -> false
    //TODO bonus if you can also ignore an non parenthesis
    // only thing here I think break or early return might help


    val opened = "("
    val closed = ")"

    val stack = scala.collection.mutable.Stack[String]()
    for (i <-text){
      if (opened.contains(i)) {
        stack.push(opened)
      }
      else if (closed.contains(i)) {
        if (stack.nonEmpty) {
          stack.pop()
        }
        else return false
      }
    }
    if (stack.isEmpty) true
    else false
  }

  println(areParenthesisMatching("((()"))
  println(areParenthesisMatching("(()())"))
  println(areParenthesisMatching(")()()"))
  println(areParenthesisMatching("((())"))


}
