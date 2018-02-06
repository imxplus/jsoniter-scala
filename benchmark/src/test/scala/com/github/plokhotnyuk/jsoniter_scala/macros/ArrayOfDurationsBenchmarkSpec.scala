package com.github.plokhotnyuk.jsoniter_scala.macros

class ArrayOfDurationsBenchmarkSpec extends BenchmarkSpecBase {
  val benchmark = new ArrayOfDurationsBenchmark
  
  "ArrayOfDurationsBenchmark" should {
    "deserialize properly" in {
      //FIXME Circe doesn't parse properly "PT-4H-33M-0.00001638S"
      //benchmark.readCirce().deep shouldBe benchmark.obj.deep
      //FIXME Jackson doesn't parse properly "PT-4H-33M-0.00001638S"
      //benchmark.readJacksonScala().deep shouldBe benchmark.obj.deep
      benchmark.readJsoniterScala().deep shouldBe benchmark.obj.deep
      //FIXME Play-json doesn't parse properly "PT-4H-33M-0.00001638S"
      //benchmark.readPlayJson().deep shouldBe benchmark.obj.deep
    }
    "serialize properly" in {
      toString(benchmark.writeCirce()) shouldBe benchmark.jsonString
      // FIXME Jackson serializes Duration as a number
      //toString(benchmark.writeJacksonScala()) shouldBe benchmark.jsonString
      toString(benchmark.writeJsoniterScala()) shouldBe benchmark.jsonString
      toString(benchmark.preallocatedBuf, benchmark.writeJsoniterScalaPrealloc()) shouldBe benchmark.jsonString
      toString(benchmark.writePlayJson()) shouldBe benchmark.jsonString
    }
  }
}