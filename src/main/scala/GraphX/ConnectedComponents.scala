package GraphX

import org.apache.spark.sql.SparkSession
import org.apache.spark.graphx._

object ConnectedComponents extends App {
  val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("GraphX")
    .getOrCreate()
  import spark.implicits._
  val path = "Dataset/facebook_combined.txt"

  val graph = GraphLoader.edgeListFile(spark.sparkContext, path)

  val cc = graph.connectedComponents()
  val ccDF = cc.vertices
    .sortBy(-_._2).toDF()
    .withColumnRenamed("_1","User")
    .withColumnRenamed("_2","Connectecd Component")
  ccDF.write
    .option("header",true)
    .csv("Results/ConnectedComponents")

  ccDF.show(5)
}
