package service.loader

import cats.data.NonEmptyList
import cats.effect._
import domain.LotteryFive
import domain.LotteryFive._
import fs2.Pipe
import fs2.data.csv._
import fs2.io.file.{Files, Path}

import scala.annotation.tailrec

class DataLoader {

  implicit class NonEmptyListOps[A](l: NonEmptyList[A]) {
    def takeRightN(num: Int): NonEmptyList[A] = {
      @tailrec
      def helper(remaining: List[A], result: List[A], counter: Int): List[A] =
        if (counter == 0) result
        else {
          remaining match {
            case Nil          => result
            case head :: tail => helper(tail, head :: result, counter - 1)
          }
        }

      NonEmptyList.fromListUnsafe(helper(l.toList.reverse, List.empty, num))
    }
  }

  private val retrieveWinnerNumbers: Pipe[IO, Row, Row] = input =>
    input.map { row =>
      row.copy(values = row.values.takeRightN(5))
    }

  def load(path: String): IO[List[LotteryFive]] =
    Files[IO]
      .readUtf8(Path(path))
      .through(lowlevel.rows(';'))
      .through(lowlevel.noHeaders)
      .through(retrieveWinnerNumbers)
      .through(lowlevel.decode)
      .compile
      .toList
}
