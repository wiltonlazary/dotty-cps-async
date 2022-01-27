package cps.macros.forest

import scala.quoted._

import cps._
import cps.macros._
import cps.macros.misc._


class RepeatedTransform[F[_]:Type,T:Type,C<:CpsMonadContext[F]:Type](cpsCtx: TransformationContext[F,T,C]):

  import cpsCtx._

  // TODO: implement
  def run(using Quotes)(repeated: quotes.reflect.Repeated): CpsExpr[F,T] =
      RepeatedTreeTransform.run(cpsCtx, repeated, repeated.elems, repeated.elemtpt)



  
