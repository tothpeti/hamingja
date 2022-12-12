List(1, 2, 3, 4, 5, 1, 2, 1)
  .foldLeft(Map.empty[Int, Int]) { (seed, elem) =>
    seed.updated(elem, seed.getOrElse(elem, 0) + 1)
  }
