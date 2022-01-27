package cps.macros.flags

import scala.quoted._

/**
 * define implicit 
 **/
object PrintCode

object PrintTree

/**
 * Use this flag for debugging of async transformation process.
 *
 * ```
 *     implicit val = cps.macros.flags.DebugLevel(10)
 *     async{ 
 *        ....
 *     }
 * ```
 *  will output ton of traces during macro translation.
 *
 * @param value - debug level, from 0 to 20.  0 - no debug outout, 20 - maximum  
 **/
case class DebugLevel(value: Int)

given FromExpr[DebugLevel] with

   def unapply(x: Expr[DebugLevel])(using Quotes) =
     import quotes.reflect._
     println(s"Unliftable[DebugLevel], x=${x.show}")
     x match
       case '{ DebugLevel(${Expr(x)}) } =>
                 Some(DebugLevel(x))
       case '{ new DebugLevel(${Expr(x)}) } =>
                 Some(DebugLevel(x))
       case other =>
          val sym = other.asTerm.symbol
          sym.tree match
             case ValDef(name,tpt,Some(rhs)) =>
                      unapply(rhs.asExprOf[DebugLevel])
             case DefDef(name,params,rt,Some(rhs)) =>
                      unapply(rhs.asExprOf[DebugLevel])
             case DefDef(name,params,tp,None) =>
                      // currently it is no-way to extract definition of given.
                      // TODO: submit request to dotty
                      None
             case other =>
                      println(s"other=${other.show}")
                      None

