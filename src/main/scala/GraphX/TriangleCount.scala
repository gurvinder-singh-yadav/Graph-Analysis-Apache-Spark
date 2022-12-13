package GraphX

import org.apache.spark.graphx.GraphLoader
import org.apache.spark.sql.SparkSession

object TriangleCount extends  App {
  val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("GraphX")
    .getOrCreate()

  import spark.implicits._

  val path = "Dataset/facebook_combined.txt"

  val graph = GraphLoader.edgeListFile(spark.sparkContext, path)

  val triangleCount = graph.triangleCount()

  val triangleCountDF = triangleCount.vertices.sortBy(-_._2).toDF()
    .withColumnRenamed("_1", "User")
    .withColumnRenamed("_2", "Triangle Count")

  triangleCountDF.write
    .option("header", true)
    .csv("Results/TriangleCount")
  triangleCountDF.show(5)

}
