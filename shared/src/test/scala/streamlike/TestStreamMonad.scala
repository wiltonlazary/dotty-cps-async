package cps.streamlike

import org.junit.{Test,Ignore}
import org.junit.Assert._


import cps._

class TestStreamMonad:

  @Test def streamReadWrite(): Unit = 
     val in1 = LazyStream(1,2,3)
     val in2 = LazyStream(4,5,6)
     val c = async[LazyStream] {
        val x = await(in1)
        val y = await(in2)
        (x,y)
     }
     println(s"c.head==${c.head}")
     assert(c.head == (1,4))
     val c1 = c.tail
     println(s"c1.head==${c1.head}")
     assert(c1.head == (1,5))
     val c2 = c1.tail
     assert(c2.head == (1,6))
     val c3 = c2.tail
     assert(c3.head == (2,4))




