package cps

import org.junit.{Test,Ignore}
import org.junit.Assert._

import scala.quoted._
import scala.util.Success

import cps.macros._

class TestBS1If:

  @Test def tIfC1_000(): Unit = 
     val c = async[ComputationBound]{
        if (true) 1 else 2
     }
     assert(c.run() == Success(1))

  @Test def tIfC1_001(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (true) 1 else await(T1.cbi(2))
     }
     assert(c.run() == Success(1))

  @Test def tIfC1_001f(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (false) 1 else await(T1.cbi(2))
     }
     assert(c.run() == Success(2))

  @Test def tIfC1_010(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (true) await(T1.cbi(1)) else 2
     }
     assert(c.run() == Success(1))

  @Test def tIfC1_011(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (true) await(T1.cbi(1)) else await(T1.cbi(2))
     }
     assert(c.run() == Success(1))


  @Test def tIfC1_100(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (await(T1.cbBool(true)))
            2 
        else 
            3
     }
     assert(c.run() == Success(2))

  @Test def tIfC1_100f(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (await(T1.cbBool(false)))
            2 
        else 
            3
     }
     assert(c.run() == Success(3))
  

  @Test def tIfC1_111(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
        if (await(T1.cbBool(true)))
            await(T1.cbi(2))
        else 
            await(T1.cbi(3))
     }
     assert(c.run() == Success(2))

  @Test def tIfC1_inBlock(): Unit = 
     val c = Async.transform[ComputationBound,Int]{
       { val x = await(T1.cbBool(true))
         val y = 3
         val z = if (x)
            await(T1.cbi(2))
         else 
            await(T1.cbi(3))
         y+z
       }
     }
     assert(c.run() == Success(5))

  @Test def tIf_OneLeg_C00(): Unit = 
     val c = async[ComputationBound]{
       var x = 0
       if (true)
         x = 1
       x+1
     }
     assert(c.run() == Success(2))

