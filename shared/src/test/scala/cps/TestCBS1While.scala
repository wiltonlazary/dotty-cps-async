package cps

import org.junit.{Test,Ignore}
import org.junit.Assert._

import scala.quoted._
import scala.util.Success
import cps.testconfig.given


class TestBS1While:


  @Test def tWhileC1_00(): Unit = 
     val c = async[ComputationBound]{
        val n = 10
        var s = 0
        var i = 0
        while(i < n) 
          s += i
          i += 1
        s
     }
     assert(c.run() == Success(45))


  @Test def tWhileC1_10(): Unit = 
     val c = async[ComputationBound]{
        val n = 10
        var s = 0
        var i = 0
        while(await( T1.cbBool(i < n) )) {
          s += i
          i += 1
        }
        s
     }
     assert(c.run() == Success(45))

  @Test def tWhileC1_01(): Unit = 
     val c = async[ComputationBound]{
        val n = 10
        var s = 0
        var i = 0
        while(i < n) {
          val q = await(T1.cbi(i))
          s += q
          i += 1
        }
        s
     }
     assert(c.run() == Success(45))

  @Test def tWhileC1_11(): Unit = 
     given ctx: CpsMonadInstanceContextBody[ComputationBound] = CpsMonadInstanceContextBody(ComputationBoundAsyncMonad)
     val c = macros.Async.transform[ComputationBound,Int, ComputationBoundAsyncMonad.Context]({
        val n = 10
        var s = 0
        var i = 0
        while(await(T1.cbBool(i < n))) {
          val q = await(T1.cbi(i))
          s += q
          i += 1
        }
        s
     }, ctx)
     assert(c.run() == Success(45))



