package cps.streamlike

import cps._

import cps.testconfig.given


trait Emitter[F[_]:CpsAsyncMonad, T]:

   def emitAsync(v:T): F[Unit]

   transparent inline def emit(v:T)(using CpsMonadContext[F]): Unit =
      await(emitAsync(v))


