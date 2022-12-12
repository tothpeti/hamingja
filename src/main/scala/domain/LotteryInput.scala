package domain

import fs2.data.csv.RowDecoder
import fs2.data.csv.generic.semiauto.deriveRowDecoder

sealed trait LotteryInput                                                            extends Product with Serializable
case class LotteryFive(first: Int, second: Int, third: Int, fourth: Int, fifth: Int) extends LotteryInput

object LotteryFive {
  implicit val lotteryFiveDecoder: RowDecoder[LotteryFive] = deriveRowDecoder
}
