import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class MostAnsweredRoots {
    public static void main(String[] args) {
        String rootsPath = args[0];
        String childsPath = args[1];

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("MostAnsweredRoots"));

        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath).distinct();
        JavaRDD<String> inputsRootsRDD = spark.textFile(rootsPath).distinct();

        JavaPairRDD<String, Integer> childParentRDD = inputChildsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[1],
                        1
                )
        ).reduceByKey(Integer::sum);

        JavaPairRDD<String, String> idAndTitleRDD = inputsRootsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[0],
                        line.split("\t")[1]
                )
        );

        JavaPairRDD<String, Tuple2<String, Integer>> rootTitleAndOccurrences = idAndTitleRDD.join(childParentRDD);
        rootTitleAndOccurrences
                .mapToPair(tup -> new Tuple2<>(tup._2._2, tup._2._1))
                .sortByKey(false)
                .take(10)
                .forEach(System.out::println);

//        JavaPairRDD<String, Integer> r = rootTitleAndOccurrences.mapToPair(tup -> tup._2);
//
//        r.sortByKey(false).take(10).forEach(System.out::println);
    }

}
