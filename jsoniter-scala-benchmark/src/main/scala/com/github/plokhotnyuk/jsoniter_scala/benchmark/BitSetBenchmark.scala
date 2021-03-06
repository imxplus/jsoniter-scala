package com.github.plokhotnyuk.jsoniter_scala.benchmark

import java.nio.charset.StandardCharsets.UTF_8

import com.avsystem.commons.serialization.json._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.AVSystemCodecs._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.JacksonSerDesers._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.JsoniterScalaCodecs._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.PlayJsonFormats._
import com.github.plokhotnyuk.jsoniter_scala.core._
//import io.circe.generic.auto._
//import io.circe.parser._
//import io.circe.syntax._
import org.openjdk.jmh.annotations.{Benchmark, Param, Setup}
import play.api.libs.json.Json
//import upickle.default._

import scala.collection.immutable.BitSet

class BitSetBenchmark extends CommonParams {
  @Param(Array("1", "10", "100", "1000", "10000", "100000", "1000000"))
  var size: Int = 1000
  var obj: BitSet = _
  var jsonString: String = _
  var jsonBytes: Array[Byte] = _
  var preallocatedBuf: Array[Byte] = _

  @Setup
  def setup(): Unit = {
    obj = BitSet(0 until size: _*)
    jsonString = obj.mkString("[", ",", "]")
    jsonBytes = jsonString.getBytes("UTF-8")
    preallocatedBuf = new Array[Byte](jsonBytes.length + 100/*to avoid possible out of bounds error*/)
  }

  @Benchmark
  def readAVSystemGenCodec(): BitSet = JsonStringInput.read[BitSet](new String(jsonBytes, UTF_8))
/* FIXME: Circe doesn't support parsing of bitsets
  @Benchmark
  def readCirce(): BitSet = decode[BitSet](new String(jsonBytes, UTF_8)).fold(throw _, identity)
*/
/* FIXME: Jackson throws java.lang.IllegalArgumentException: Need exactly 1 type parameter for collection like types (scala.collection.immutable.BitSet)
  @Benchmark
  def readJacksonScala(): BitSet = jacksonMapper.readValue[BitSet](jsonBytes)
*/
  @Benchmark
  def readJsoniterScala(): BitSet = readFromArray[BitSet](jsonBytes)

  @Benchmark
  def readPlayJson(): BitSet = Json.parse(jsonBytes).as[BitSet](bitSetFormat)

/* FIXME: uPickle doesn't support reading of bitsets
  @Benchmark
  def readUPickle(): BitSet = read[BitSet](jsonBytes)
*/
  @Benchmark
  def writeAVSystemGenCodec(): Array[Byte] = JsonStringOutput.write(obj).getBytes(UTF_8)
/* FIXME: Circe doesn't support writing of bitsets
  @Benchmark
  def writeCirce(): Array[Byte] = printer.pretty(obj.asJson).getBytes(UTF_8)
*/

  @Benchmark
  def writeJacksonScala(): Array[Byte] = jacksonMapper.writeValueAsBytes(obj)

  @Benchmark
  def writeJsoniterScala(): Array[Byte] = writeToArray(obj)

  @Benchmark
  def writeJsoniterScalaPrealloc(): Int = writeToSubArray(obj, preallocatedBuf, 0, preallocatedBuf.length)

  @Benchmark
  def writePlayJson(): Array[Byte] = Json.toBytes(Json.toJson(obj)(bitSetFormat))

/* FIXME: uPickle doesn't support writing of bitsets
  @Benchmark
  def writeUPickle(): Array[Byte] = write(obj).getBytes(UTF_8)
*/
}