package service.fetcher

import cats.effect._

trait DataFetcher {
  def fetch[A](url: String): IO[A]
}

object DataFetcher {
  def make: DataFetcher = new DataFetcher {
    override def fetch[A](url: String): IO[A] = ???
  }
}
