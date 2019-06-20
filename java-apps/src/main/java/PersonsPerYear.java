import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.*;

public class PersonsPerYear {
    public static void main(String[] args) {
        String rootsPath = args[0];
        String childsPath = args[1];

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath).distinct();
        JavaPairRDD<String, Integer> rootWoenAndDateRDD = inputRootsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[2],
                        Utils.yearFromEpochString(line.split("\t")[3])
                )
        );

        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath).distinct();
        JavaPairRDD<String, Integer> childWoenAndDateRDD = inputChildsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[3],
                        Utils.yearFromEpochString(line.split("\t")[4])
                )
        );

        JavaPairRDD<String, Integer> superWoenAndDateRDD = rootWoenAndDateRDD.union(childWoenAndDateRDD);

        JavaPairRDD<String, Integer> distinctWoenAndDate = superWoenAndDateRDD.distinct();

        JavaPairRDD<Integer, String> dateAndWoenRDD = distinctWoenAndDate.mapToPair(
                row -> new Tuple2<>(
                        row._2,
                        row._1)
        );

        JavaPairRDD<Integer, Integer> commentersPerYear = dateAndWoenRDD.groupByKey().mapToPair(
                row -> {
                    int s = 0;
                    for(String i: row._2) {
                        s++;
                    }
                    return new Tuple2<>(row._1,s);
                }
        );

        commentersPerYear.sortByKey()
                .collect()
                .forEach(System.out::println);

        spark.close();
    }
}

