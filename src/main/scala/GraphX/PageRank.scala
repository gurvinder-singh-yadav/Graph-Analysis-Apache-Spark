package GraphX

import org.apache.spark.sql.SparkSession
import org.apache.spark.graphx._


object PageRank extends App {
  val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("GraphX")
    .getOrCreate()
  import spark.implicits._

  val path = "Dataset/facebook_combined.txt"

  val graph = GraphLoader.edgeListFile(spark.sparkContext, path)

  val ranks = graph.pageRank(0.00001)

  val pageRankDF = ranks.vertices.sortBy(-_._2).toDF()
    .withColumnRenamed("_1", "User")
    .withColumnRenamed("_2", "PageRank")

  pageRankDF.write
    .option("header", true)
    .csv("Results/PageRank")
  pageRankDF.show(5)

}
