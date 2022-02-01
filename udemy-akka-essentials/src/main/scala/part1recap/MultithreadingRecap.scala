package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MultithreadingRecap extends App {
  //creating threads on the JVM

  val aThread = new Thread(() => println("I'm running in parallel"))
  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 100).foreach(_ => println("Hello")))
  val threadGoodBye = new Thread(() =>
    (1 to 100).foreach(_ => println("Good Bye"))
  )

  threadHello.start()
  threadGoodBye.start()

  // Different runs produce different results while  using threads.

  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.amount -= money // Not thread safe

    //thread safe. Because synchronized will not let no 2 threads to access at the same time.
    // We can add volatile annotation to the amount variable which solves atomic reads but not atomic write safe
    // so, we still need to add sync and it only works for Int.

    def safeWithdrawal(money: Int) = this.synchronized(
      this.amount -= money
    )
    // As the application grows this syncing thread will become increasingly difficult to maintain in large apps

    //Inter- Thread communication on the JVM - which is done via Wait - Notify mechanism

    //Scala Futures:
    //import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    val future = Future {
      42
    }

    //Callbacks
    future.onComplete {
      case Success(42) => println("I found the meaning of 42")
      case Failure(_)  => println("This is an exception")
    }

    val aProcessedFuture = future.map(_ + 1)                          // 43
    val aflatFuture      = future.flatMap(value => Future(value + 2)) // 44

    val filteredFuture =
      future.filter(
        _ % 2 == 0
      ) //If the value contained in the original future is predicate otherwise it will throw NoSuchElementException

    //For comprehensions ,andThen, recover/recoverWith

  }

}
