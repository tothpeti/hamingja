package service

import cats.implicits._
import cats.effect._
import domain.LotteryFive

class LotteryService {

  type WinningNumber = Int
  type Occurrence    = Int

  sealed trait Result extends Product with Serializable

  case class MeanResult(first: Double, second: Double, third: Double, fourth: Double, fifth: Double) extends Result
  case class LeastFrequentResult(
    first: List[(WinningNumber, Occurrence)],
    second: List[(WinningNumber, Occurrence)],
    third: List[(WinningNumber, Occurrence)],
    fourth: List[(WinningNumber, Occurrence)],
    fifth: List[(WinningNumber, Occurrence)]
  ) extends Result

  case class MostFrequentResult(
    first: List[(WinningNumber, Occurrence)],
    second: List[(WinningNumber, Occurrence)],
    third: List[(WinningNumber, Occurrence)],
    fourth: List[(WinningNumber, Occurrence)],
    fifth: List[(WinningNumber, Occurrence)]
  ) extends Result

  case class Statistics(
    meanResult: MeanResult,
    leastFrequentResult: LeastFrequentResult,
    mostFrequentResult: MostFrequentResult
  ) extends Result

  private implicit class ListOps(data: List[Int]) {
    def mean: Double = data.sum.toDouble / data.size

    private def createFrequencyMap: Map[Int, Int] =
      data.foldLeft(Map.empty[Int, Int]) { (seed, elem) =>
        seed.updated(elem, seed.getOrElse(elem, 0) + 1)
      }

    def mostFrequent: List[(WinningNumber, Occurrence)] = {
      val freqMap            = createFrequencyMap
      val (_, maxOccurrence) = freqMap.maxBy(_._2)

      freqMap.filter { case (_, occ) => occ == maxOccurrence }.toList
    }

    def leastFrequent: List[(WinningNumber, Occurrence)] = {
      val freqMap            = createFrequencyMap
      val (_, minOccurrence) = freqMap.minBy(_._2)

      freqMap.filter { case (_, occ) => occ == minOccurrence }.toList
    }
  }

  def calculateStatistics(list: List[LotteryFive]): IO[Statistics] = {
    val firstPos  = list.map(_.first)
    val secondPos = list.map(_.second)
    val thirdPos  = list.map(_.third)
    val fourthPos = list.map(_.fourth)
    val fifthPos  = list.map(_.fifth)

    val meanRes = (
      IO(firstPos.mean),
      IO(secondPos.mean),
      IO(thirdPos.mean),
      IO(fourthPos.mean),
      IO(fifthPos.mean)
    ).parMapN(MeanResult.apply)

    val leastFreqRes = (
      IO(firstPos.leastFrequent),
      IO(secondPos.leastFrequent),
      IO(thirdPos.leastFrequent),
      IO(fourthPos.leastFrequent),
      IO(fifthPos.leastFrequent)
    ).parMapN(LeastFrequentResult.apply)

    val mostFreqRes = (
      IO(firstPos.mostFrequent),
      IO(secondPos.mostFrequent),
      IO(thirdPos.mostFrequent),
      IO(fourthPos.mostFrequent),
      IO(fifthPos.mostFrequent)
    ).parMapN(MostFrequentResult.apply)

    (meanRes, leastFreqRes, mostFreqRes).mapN(Statistics.apply)
  }
}
