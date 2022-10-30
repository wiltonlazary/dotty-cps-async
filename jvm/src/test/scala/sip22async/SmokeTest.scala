/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */
package scala.async

import org.junit.{Assert, Test}

//----changed for compability with dotty-cps-async  --------
//import scala.async.Async._
import cps.compat.FutureAsync._
import cps.testconfig.given
//----------------------------------------------------------
import scala.concurrent._
import scala.concurrent.Future.{successful => f}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class SmokeTest {
  def block[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  @Test def testBasic(): Unit = {
    val result = async {
      await(f(1)) + await(f(2))
    }
    Assert.assertEquals(3, block(result))
  }

}
