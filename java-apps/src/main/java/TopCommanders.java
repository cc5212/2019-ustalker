import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class TopCommanders {
    public static void main(String[] args) throws IOException {
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

        System.out.println("\nTop commanders por anno:");
        JavaPairRDD<String, Integer> superWoenAndDateRDD = rootWoenAndDateRDD.union(childWoenAndDateRDD);
        superWoenAndDateRDD
                .mapToPair(pair -> new Tuple2<>(pair, 1L))
                .reduceByKey(Long::sum)
                .mapToPair(row -> new Tuple2<>(row._1._2, new Tuple2<>(row._1._1, row._2)))
                .groupByKey()
                .mapToPair(tup -> {
                    PriorityQueue<Tuple2<String, Long>> pq = new PriorityQueue<>((t1, t2) -> t1._2.compareTo(t2._2));
                    tup._2.forEach(pq::add);
                    while (pq.size() > 10) pq.poll();
                    List<Tuple2<String, Long>> sup = new ArrayList<>();
                    while (!pq.isEmpty()) sup.add(0, pq.poll());
                    return new Tuple2<>(tup._1, sup);
                })
                .sortByKey()
                .collect()
                .forEach(System.out::println);


        System.out.println("\nTop commanders de curso:");
        Set<String> set = new HashSet<>();
        String comradesPath = args[2];
        try (Stream<String> stream = Files.lines(Paths.get(comradesPath))) {
            stream.forEach(set::add);
        }
        inputRootsRDD
                .map(line -> line.split("\t")[2])
                .union(inputChildsRDD.map(line -> line.split("\t")[3]))
                .filter(set::contains)
                .mapToPair(name -> new Tuple2<>(name, 1L))
                .reduceByKey(Long::sum)
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .collect()
                .forEach(System.out::println);

        System.out.println("\nSingle commanders");
        System.out.println(
                inputRootsRDD
                        .map(line -> line.split("\t")[2])
                        .union(inputChildsRDD.map(line -> line.split("\t")[3]))
                        .mapToPair(name -> new Tuple2<>(name, 1L))
                        .reduceByKey(Long::sum)
                        .filter(tup -> tup._2 == 1)
                        .count()
        );
    }
}
