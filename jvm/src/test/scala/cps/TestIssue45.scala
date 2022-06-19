package cps

import org.junit.{Test,Ignore}
import org.junit.Assert._

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.quoted._
import scala.util.Success

import java.util.Timer
import java.util.TimerTask

import cps.monads.given

class TestIssue45:

  import scala.concurrent.ExecutionContext.Implicits.global 


  @Test def runSpawn(): Unit =
    val threadIndexOutside1 = Thread.currentThread().nn.getId()
    val beforeStart = System.currentTimeMillis()
    val f = async[Future]{
      val threadIndexInside = Thread.currentThread().nn.getId()
      Thread.sleep(100)
      threadIndexInside
    }
    val afterStart = System.currentTimeMillis()
    val threadIndexOutside2 = Thread.currentThread().nn.getId()
    assert(threadIndexOutside1  == threadIndexOutside2, "changed thread after spawn")
    val startDuration = afterStart - beforeStart 
    //println(s"startDuration=${startDuration}")
    assert(startDuration < 100, "start too slow")
    val r: Long = Await.result(f, 1000 milliseconds)
    //assert(r != threadIndexOutside2, "f was executed in the same thread")
    
