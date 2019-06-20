import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Array;
import scala.Tuple2;

import java.util.*;

public class TopCommentersPerTheme {

    public static final String[] MOVILIZACION_ARR = new String[] {"paro", "toma", "marcha", "asamblea", "tricel", "votacion", "conteo"};
    public static final Set<String> MOVILIZACION = new HashSet<>(Arrays.asList(MOVILIZACION_ARR));

    public static final String[] FACULTAD_ARR = new String[] {"facultad", "escuela"};
    public static final Set<String> FACULTAD = new HashSet<>(Arrays.asList(FACULTAD_ARR));

    public static final String[] VENTAS_ARR = new String[] {"vendo", "venta", "vendo!", "vender", "compro"};
    public static final Set<String> VENTAS = new HashSet<>(Arrays.asList(VENTAS_ARR));

    public static final String[] PERDIDOS_ARR = new String[] {"encontré", "perdí", "perdido", "perdidas", "encontre", "perdidos",
            "encontrado", "encontrados", "lost"};
    public static final Set<String> PERDIDOS = new HashSet<>(Arrays.asList(PERDIDOS_ARR));

    // ofertas de trabajo


    public static HashMap<String, Set<String>> THEMES = new HashMap<String, Set<String>>(){
        {
            put("movilizacion", MOVILIZACION);
            put("facultad", FACULTAD);
            put("ventas", VENTAS);
            put("perdidos", PERDIDOS);
        }};


    public static void main(String[] args) {
        String rootsPath = args[0];
        String childsPath = args[1];

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath).distinct();
        JavaPairRDD<String, String> idTitleRDD = inputRootsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[0],
                        line.split("\t")[1]
                )
        );

        /*
         * Ver comentarios por tema
         */
        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath).distinct();
        JavaPairRDD<String, String> idRootCommentAuthorRDD = inputChildsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[1],
                        line.split("\t")[3]
                )
        );

        /*
         * la union deja (id tema, (titulo tema, persona que comenta))
         */
        JavaPairRDD<String, Tuple2<String, String>> idTitleCommenterRDD= idTitleRDD.join(idRootCommentAuthorRDD);

        idTitleCommenterRDD.mapToPair(row ->  new Tuple2<>( row._2._1, row._2._1)); // (commenter, title of root)


        spark.close();
    }
}

