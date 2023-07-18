package cpstest

import scala.annotation.*
import scala.concurrent.*
import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global

import cps.*
import cps.monads.{ *, given }

@experimental
object Test8m3 {
  
  def asyncCheck(i: Int)(using CpsDirect[Future]): Boolean = i < 3

  def asyncLoop(url: String)(using CpsDirect[Future]): Unit = {
    var i = 0
    while (asyncCheck(i)) do {
      i += 1
      println(s"$url:$i")
    }
  }

  def main(args: Array[String]): Unit = {
    val fr = async[Future] {
      asyncLoop("myurl")
    }
    val r  = Await.result(fr, 1000.millis)
    println("Ok")
  }

}
