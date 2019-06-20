import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class DuckOlives {
    public static void main(String[] args) {
        int criticalTime = 1226665740;
        System.out.println("\nAnno del evento");
        System.out.println(Utils.yearFromEpoch(criticalTime));


        String rootsPath = args[0];
        String childsPath = args[1];

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Duck Olives, your surname, very B A C A N!"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath).distinct();
        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath).distinct();

        JavaPairRDD<String, Integer> justDuck = inputRootsRDD
                .mapToPair(line -> new Tuple2<>(
                        line.split("\t")[2],
                        Double.valueOf(line.split("\t")[3]).intValue()
                ))
                .union(inputChildsRDD.mapToPair(line -> new Tuple2<>(
                        line.split("\t")[3],
                        Double.valueOf(line.split("\t")[4]).intValue()
                )))
                .filter(tup -> tup._1.equals("Patricio Aceituno G."));

        System.out.println("\nPrimer anno comentario de duck olives");
        System.out.println(
                Utils.yearFromEpoch(
                        justDuck
                                .map(tup -> tup._2)
                                .sortBy(row -> row, true, 1)
                                .take(1).get(0)
                )
        );

        System.out.println("\nUltimo anno comentario de duck olives");
        System.out.println(
                Utils.yearFromEpoch(
                        justDuck
                                .map(tup -> tup._2)
                                .sortBy(row -> row, false, 1)
                                .take(1).get(0)
                )
        );


        System.out.println("\nComentarios de duck olives pre rodilleras");
        System.out.println(
                justDuck
                        .filter(tup -> tup._2 < criticalTime)
                        .count()
        );
        System.out.println("\nComentarios de duck olives post rodilleras");
        System.out.println(
                justDuck
                        .filter(tup -> tup._2 >= criticalTime)
                        .count()
        );
    }
}
