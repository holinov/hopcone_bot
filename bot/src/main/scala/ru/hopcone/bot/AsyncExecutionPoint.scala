package ru.hopcone.bot

import java.util.concurrent.Executors

import akka.dispatch.ExecutionContexts

import scala.concurrent.ExecutionContext

trait AsyncExecutionPoint {
  protected implicit val ex: ExecutionContext = ExecutionContexts.fromExecutorService(Executors.newCachedThreadPool())
}
