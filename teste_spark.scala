// Databricks notebook source
import org.apache.spark.sql.functions._

val fileJ = spark.read.format("csv")
  .option("sep", " ")
  .option("inferSchema", "true")
  .option("header", "false")
  .load("/FileStore/tables/NASA_access_log_Jul95.gz")

val fileA = spark.read.format("csv")
  .option("sep", " ")
  .option("inferSchema", "true")
  .option("header", "false")
  .load("/FileStore/tables/NASA_access_log_Aug95.gz")

val dfUnion = fileJ.union(fileA)

val dfAfterDrop = dfUnion.drop("_c1").drop("_c2").drop("_c4").drop("_c4").drop("_c5")
val dfAfterDrop_aux = dfAfterDrop.withColumn("date", $"_c3".substr(2,11))
val dfAfterRename = dfAfterDrop_aux.withColumnRenamed("_c0", "host").withColumnRenamed("_c3", "timestamp").withColumnRenamed("_c6", "error").withColumnRenamed("_c7", "bytes")

// Resposta 1
println("Resposta 1:")
dfAfterRename.agg(countDistinct("host")).show()
println

// Resposta 2
println("Resposta 2:")
println(dfAfterRename.filter("error == '404'").count())
println

// Resposta 3
println("Resposta 3:")
dfAfterRename.filter("error == '404'").groupBy("host").count().orderBy($"count".desc).show(5)
println

// Resposta 4
println("Resposta 4:")
dfAfterRename.filter("error == '404'").groupBy("date").count().show(70)
println

// Resposta 5
println("Resposta 5:")
println(dfAfterRename.agg(sum("bytes")).first.get(0))
println
