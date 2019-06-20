import org.apache.spark.SparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.time.Instant;
import java.util.Date;

public class PlusOneMinusOne {
    public static void main(String[] args) {

        String filePath = args[0];
        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));


        JavaRDD<String> inputRDD = spark.textFile(filePath).distinct();
        long countPreBan = inputRDD
            .filter(line -> line.split("\t")[1].contains("+1/-1"))
            .filter(line -> Double.valueOf(line.split("\t")[3]) < 1530403200).count();

        long countPostBan = inputRDD
            .filter(line -> line.split("\t")[1].contains("+1/-1"))
            .filter(line -> Double.valueOf(line.split("\t")[3]) >= 1530403200).count();

        System.out.println();
        System.out.println("\t\tMira ctm, la wea es asi " + countPreBan + " y asi " + countPostBan);
        System.out.println();

        spark.close();
    }
}
