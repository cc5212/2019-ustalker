import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import scala.Tuple3;

public class TopFighters {
    public static void main(String[] args) {
        String rootsPath = args[0];
        String childsPath = args[1];

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        /* Se cargan los datos de los root */
        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath).distinct();
        /* (id, (autor, id_parent, id_thr)) */
        JavaPairRDD<String, Tuple3<String, String, String>> fatherItselfRDD = inputRootsRDD.mapToPair(
                line -> new Tuple2<>(line.split("\t")[0], // id de raiz
                        new Tuple3<>(line.split("\t")[2], "-" + line.split("\t")[0], line.split("\t")[0]))
        );

        /* Se cargan los datos de los childs */
        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath).distinct();
        /* (id, (autor, id_parent, id_thr)) */
        JavaPairRDD<String, Tuple3<String, String, String>> childAndFatherRDD = inputChildsRDD.mapToPair(
                line -> new Tuple2<>(line.split("\t")[0], // id del comentario
                        new Tuple3<>(line.split("\t")[3], line.split("\t")[2], line.split("\t")[1]))
        );

        /*Se crea un rdd grandote uniendo los dos anteriores
         * (id, (autor, id_parent, id_thr)) */
        JavaPairRDD<String, Tuple3<String, String, String>> allRDD = childAndFatherRDD.union(fatherItselfRDD);

        /*Se cambia la llave del rdd de childs
         * (id_parent, (autor, id, id_thr))*/
        JavaPairRDD<String, Tuple3<String, String, String>> fatherFirstRDD = childAndFatherRDD.mapToPair(
                tup -> new Tuple2<>(tup._2._2(), new Tuple3<>(tup._2._1(), tup._1, tup._2._3()))
        );

        /*Se realiza el primer join entre el rdd de todos con el rdd de hijos que tienen
         * la id de parent como llave
         * (id/id_parent, ((autor, id_parent, id_thr), (autor, id, id_thr)))*/
        JavaPairRDD<
                String,
                Tuple2<
                        Tuple3<String, String, String>,
                        Tuple3<String, String, String>
                        >
                > firstJoin = allRDD.join(fatherFirstRDD);
        
        /*caleta de weas*/
        firstJoin
                /* Se mapea para cambiar la llave
                * (id {* de la segunda columna}, ((autor, id_parent, id_thr), (autor, id, id_thr)))*/
                .mapToPair(tup -> new Tuple2<>(
                        tup._2._2._2(),
                        tup._2())
                )
                /*Ahora se hace el join para ver quienes le respondieron a los
                * de la segunda columna
                * el formato de este esta brigido*/
                .join(fatherFirstRDD)
                /*(id_thr, (weon1, weon2, weon3))*/
                .mapToPair(tup -> new Tuple2<>(
                        tup._2._2._3(),
                        new Tuple3<>(tup._2._1._1._1(), tup._2._1._2._1(), tup._2._2._1())
                ))
                /*Ahora se filtran tal que el primero sea igual al ultimo y distinto al segundo*/
                .filter(tup -> (tup._2._1().equals(tup._2._3()) && !tup._2._1().equals(tup._2._2())))
                /*Se mapea de una tupla con el id_thr y una tupla con los wm que discutieron, ordenados
                * dentro de la tupla por orden alfabetico */
                .mapToPair(tup -> new Tuple2<>(tup._1, new Tuple2<>(tup._2._1(), tup._2._2())))
                .mapToPair(tup -> new Tuple2<>(
                        tup._1,
                        tup._2._1().compareTo(tup._2._2()) < 0 ? tup._2 : tup._2.swap()
                ))
                /*Se deja unasola ocurrencia por thread*/
                .distinct()
                /*Ahora se cuentan*/
                .mapToPair(tup -> new Tuple2<>(tup._2, 1))
                .reduceByKey(Integer::sum)
                // .mapToPair(row -> new Tuple2<>(row._2, row._1))
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .take(20)
                .forEach(System.out::println);
    }
}

