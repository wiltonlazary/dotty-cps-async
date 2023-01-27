package cps.plugin.forest.application

import dotty.tools.dotc.*
import ast.tpd.*
import core.*
import core.Contexts.*
import core.Decorators.*
import core.Names.*
import core.Symbols.*
import core.SymDenotations.*
import util.Spans.Span
import core.Types.*


import cps.plugin.*
import cps.plugin.forest.*
import QuoteLikeAPI.*


object DependencyCheck {

   case class Result(canBeDependent: Boolean, syms: Set[Symbol])

   case class CheckState(prevs:Set[Symbol], next:Set[Symbol], depFound:Boolean, inPossibleRhs:Boolean)

   def run(tree: Tree, syms: Set[Symbol])(using Context): Result = {
      val acc = new TreeAccumulator[CheckState] {
         def apply(state: CheckState,tree: Tree)(using Context): CheckState = {
             tree match
                case x: Assign =>
                  val next = state.next + x.lhs.symbol  
                  foldOver(state.copy(next = state.next + x.lhs.symbol),x.rhs)
                case x: Apply =>
                  //  dependendy is possible since all arguments
                  val oldPossibleInRhs = state.inPossibleRhs
                  val tmpState = foldOver(state.copy(withDep = true,inPossibleRhs = true),tree)
                  tmpState.copy(inPossibleRhs = oldPossibleInRhs)
                case x: Constant =>
                  state
                case other if other.symbol !== NoSymbol =>
                  val depFound = state.depFound || syms.contains(other.symbol)
                  val nextSyms = if (inPossibleRhs) then
                    state.next :+ other.symbol
                  else
                    state.next
                  foldOver(state.copy(depFound=depFound,next = nextSyms),tree)  
         }
      }
      val res = acc.apply(State(syms,Set.empty,false,false))
      Result(res.depFound, res.prevs ++ res.next)
   }

}