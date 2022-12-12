import cats.effect.{IO, IOApp}
import service.LotteryService
import service.loader._

object Main extends IOApp.Simple {

  private val pathToLotteryFiveFile = "src/main/resources/test/otos.csv"

  override def run: IO[Unit] =
    for {
      loader  <- IO(new DataLoader)
      service <- IO(new LotteryService)
      data    <- loader.load(pathToLotteryFiveFile)
      res     <- service.calculateStatistics(data)
      _       <- IO(println(res))
    } yield ()

}
