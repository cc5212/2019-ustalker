import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import javax.rmi.CORBA.Util;

public class JulyRooms {
    public static void main(String[] args) {
        int criticalDate = 1313083440;
        System.out.println("\nAnno del evento");
        System.out.println(Utils.yearFromEpoch(criticalDate));


        String rootsPath = args[0];
        String childsPath = args[1];

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("July Rooms KneePads"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath).distinct();
        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath).distinct();

        JavaPairRDD<String, Integer> justJuly = inputRootsRDD
                .mapToPair(line -> new Tuple2<>(
                        line.split("\t")[2],
                        Double.valueOf(line.split("\t")[3]).intValue()
                ))
                .union(inputChildsRDD.mapToPair(line -> new Tuple2<>(
                        line.split("\t")[3],
                        Double.valueOf(line.split("\t")[4]).intValue()
                )))
                .filter(tup -> tup._1.equals("Julio Salas"));

        System.out.println("\nPrimer anno comentario de july rooms");
        System.out.println(
                Utils.yearFromEpoch(
                        justJuly
                                .map(tup -> tup._2)
                                .sortBy(row -> row, true, 1)
                                .take(1).get(0)
                )
        );

        System.out.println("\nUltimo anno comentario de july rooms");
        System.out.println(
                Utils.yearFromEpoch(
                        justJuly
                                .map(tup -> tup._2)
                                .sortBy(row -> row, false, 1)
                                .take(1).get(0)
                )
        );


        System.out.println("\nComentarios de july rooms pre rodilleras");
        System.out.println(
                justJuly
                        .filter(tup -> tup._2 < criticalDate)
                        .count()
        );
        System.out.println("\nComentarios de july rooms post rodilleras");
        System.out.println(
                justJuly
                        .filter(tup -> tup._2 >= criticalDate)
                        .count()
        );
    }
}
