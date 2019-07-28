package com.stock

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.collection.mutable.ListBuffer
/**
 * Hello world!
 *
 */
object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().enableHiveSupport().getOrCreate()

    val historyDf = spark.sql("select * from history_data")

    def getEvery60(closeList: Seq[String]): Seq[String] = {
      val newArray = closeList.sorted.reverse.toArray
      for (i <- 0 until newArray.length -1) {
        var sub = i + 60
        if (newArray.length - i <=60)
          sub = newArray.length
        val aSeq = newArray.toSeq.slice(i, sub)
        val theMax = aSeq.map(_.split("\\|")(1).toDouble).max
        newArray(i) = newArray(i)+"|"+theMax
      }
      newArray
    }

    spark.udf.register("getEvery60", getEvery60 _)

    historyDf.filter(
      "time > '2019-07-22 09:30:00'"
    ).selectExpr(
      "symbol",
      "concat(time,'|' ,close) as str"
    ).groupBy("symbol").agg(
      collect_list("str").as("str_list")
    ).selectExpr(
      "symbol",
      "getEvery60(str_list) as every60"
    ).show(false)
  }
}
