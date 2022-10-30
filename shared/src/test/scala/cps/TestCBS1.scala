package cps

import org.junit.{Test,Ignore}
import org.junit.Assert._

import scala.quoted._
import scala.util.Success
import cps.testconfig.given

class TestCBS1:

  @Test def tConstantMeta(): Unit = 
     val c = macros.Async.transform[ComputationBound,Int,ComputationBoundAsyncMonad.Context](3, CpsMonadInstanceContextBody(ComputationBoundAsyncMonad))
     val r = c.run()
     assert(r == Success(3))
  
  @Test def tConstantMetaTypeInference(): Unit = 
     val c = async[ComputationBound](3)
     val r = c.run()
     assert(r == Success(3))

  @Test def tAwaitErase(): Unit = 
     val c = async[ComputationBound](await(T1.cb()))
     val r = c.run()
     assert(r == Success(()))
  
  @Test def tValDef(): Unit = 
     val c = async[ComputationBound]{
              val t = 3
             }
     val r = c.run()   
     assert(r == Success(()))

  @Test def tValDefAsyn(): Unit = 
     val c = async[ComputationBound]{
         val t = await(T1.cbi(3))
     }
     val c1 = c.run()
     assert( c1 == Success(()) )

  @Test def tBlockNoAsync(): Unit = 
     val c = async[ComputationBound]{
         val x1 = 3
         val x2 = 4 //await(T1.cbi(4))
         x1 + x2
         //7
     }
     val c1 = c.run()
     assert( c1 == Success(7) )

  @Test def tBlockVal2Async(): Unit = 
     val c = async[ComputationBound]{
         val x1 = 3
         val x2 = await(T1.cbi(5))
         x1 + x2
     }
     val c1 = c.run()
     assert( c1 == Success(8) )

  @Test def tSeqSimple(): Unit = 
     val c = async[ComputationBound]{
          Seq(1,2,await(T1.cbi(3)),await(T1.cbi(4)),5)
     }
     val c1 = c.run()
     assert( c1 == Success(Seq(1,2,3,4,5)) )

  @Test def tSeq2(): Unit = 
     //implicit val printTree = cps.macroFlags.PrintTree
     val c = async[ComputationBound]{
          Seq("==",(1,2,await(T1.cbi(3)),await(T1.cbi(4)),5))
     }
     val c1 = c.run()
     assert( c1 == Success(Seq("==",(1,2,3,4,5))) )


