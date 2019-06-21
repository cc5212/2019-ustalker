# 2019-ustalker
Proyecto de CC5212 Procesamiento masivo de datos, en donde analizamos un dataset extraido del foro de la facultad.



> En este foro no se mueve una hoja sin que yo lo sepa
>
> -- <cite>[Gabriel Chaperon, Valeria Guidotti, Francisco Lecaros. Group 15]</cite>


<!-- gabriel -->
# Descripción del contenido del repositorio
En el directorio [data](/data/) se encuentra una pequeña descripción de los datos junto con archivos extra que fueron utilizados para el proyecto, como las _stop-words_ y la lista de integrantes del curso.


<!-- lecaro -->
# Overview

<!-- holaaaaaaaaaaaaaaaaaaaaa -->
EL objetivo general del proyecto es ver el comportamiento de la comunidad _"Beauchefiana"_ en el foro de la facultad (y buscar memes). La idea es ver como ha sido el comportamiento a través de los años viendo, por ejemplo, el flujo de los comentarios y observar si existe alguna correlación con eventos que han ocurrido en la facultad o el cambio de tecnologías. Otro ejemplo es ver que personas son las que más comentan en el foro de la facultad, quiénes son las personas que más discuten en el foro y otras consultas relevantes para la historia de la facultad.

<!-- gabriel -->
# Datos

Los datos fueron escrapeados del foro de la facultad en u-cursos. Este trabajo fue realizado por el grupo y por lo tanto trabajamos con un dataset custom, nunca antes visto, que no se puede encontrar en ningún otro lugar del mundoooo. El dataset está organizado en dos tablas, una para los comentarios raíz y una de los comentarios que son respuestas. Se tomo esta decisión porque los comentarios raíz tienen título y no tienen padre, pero los comentarios que son respuesta no tienen título y sí tienen padre.

Los archivos son columnas separadas por tabulación, sin header en donde al mensaje se le extrayeron todos los espacios blancos que no sean el caracter espacio. Esto permite leer los archivos por fila, sin tener que requerir de la lógica de un intérprete csv para leer los archivos. 

Ambos archivos en conjunto y descromidos pesan 277mb aproximadamente. El archivo de comentarios hijo contiene 1.3 millones de líneas aproximadamente y el archivo de comentarios raíz contiene aproximadamente 100 mil líneas.

Ambas tablas tienen filas repetidas, porque las páginas del foro de u-cursos se mueven dinámicamente a medida que se van agregando comentarios, lo que genera que algunos comentarios se cambien de página en el tiempo que transcurre entre la descargas de las distintas páginas.


La tabla de los comentarios raíz tiene el siguiente header:

| id  | titulo  | autor   | fecha   | categoria   | mensaje   |
|---- |:------: |------:  |-------  |-----------  |---------  |

La fecha esta en Unix time en segundos. El resto de nombres son autoexplicativos.


Por otro lado, los comentarios que son respuestas tiene el siguiente header:

| id_mensaje  | id_raiz   | id_padre  | autor   | fecha   | mensaje   |
|------------ |:-------:  |---------: |-------  |-------  |---------  |

El primer id es del mensaje mismo, este id es único entre todos los mensajes, incluyendo raices y comentarios respuesta. La id_raiz corresponde a la id de la raíz a la que pertenece la respuesta. La id_padre corresponde a la id del mensaje directo al que responde, a su padre directo. La fecha está en el mismo formato que los comentarios raíz. El resto de campos son autoexplicativos.


Los datos se pueden encontrar en el siguiente link https://users.dcc.uchile.cl/~gchapero/ustalker/ustalker_wrepeat.tar.xz

<!-- guido -->
# Metodología

## Técnicas y tecnologías utilizadas

El desarrollo del proyecto se divide en dos partes, de las cuales solo la de procesamiento es relevante a los contenidos del curso.

### 1. Descarga del dataset

La descarga del dataset se realizó _web scraping_ del foro institucional de la FCFM con Python y BeautifulSoup. 

Para ver más sobre esto, acceder al directorio [intento-de-scrapper](/intento-de-scrapper/).

### 2. Procesamiento de datos

En el procesamiento de datos se realizó localmente utilizó Apache Spark 2.3.3 para para Hadoop 2.7.3. Además se procesaron los datos con Spark 2.1.0 para Hadoop 2.3.0 en el HDFS provisto en el curso.

Los *jobs* de Spark fueron escritos en Java 8. Estos se encuentran en el directorio [java-apps](/java-apps/src/main/java).

La elección de Spark como *engine* para el *data processing* se explica por la ventaja en el tiempo que toma correr los *jobs* comparado con el tiempo de Hadoop y Apache Pig. Al comparar Spark con Hadoop, el primero ofrece facilidades para escribir el código de las aplicaciones de manera más rápida.

El último motivo de la elección de Spark el aumento y superioridad en el [interés mundial](https://trends.google.es/trends/explore?date=today%205-y&q=Apache%20Spark,Apache%20Hadoop,Apache%20Pig) de Apache Spark por sobre Apache Hadoop y Apache Pig lo que lo hace más atrayente aumentar el conocimiento grupal en esta herramienta.

La redacción de *jobs* de Spark en Java 8 fue realizada a partir de los ejemplos provistos en clases y en la [página de Spark](https://spark.apache.org/examples.html), acompañado de la lectura y estudio de la documentación de [JavaRDD](https://spark.apache.org/docs/2.2.1/api/java/org/apache/spark/api/java/JavaRDD.html) y [JavaPairRDD](https://spark.apache.org/docs/2.2.1/api/java/org/apache/spark/api/java/JavaPairRDD.html).

Respecto a los resultados, estos no fueron escritos en un archivo de salida, debido a que se priorizó la rapidez para visualizarlos, para lo cual se mostraban los computos finales en salida estándar.


## Problemas o dificultades

Al realizar el proyecto se tuvo una dificultad al querer trabajar con una [PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html) de Java 8 en una agregación, ya que se encontraba una NotSerializableException. Esto se solucionó evitando la agregación y utilizando un groupByKey de JavaPairRDD y luego un mapToPair (Ver línea 42 de [TopCommanders.java](/java-apps/src/main/java/TopCommanders.java)).

<!-- lecaro -->
# Resultados

Dado que durante el proyecto no se trabajó con un dataset grande, no se midió el tiempo que tomaban las consultas, puesto que no proporcionarían ningún dato interesante. 

Mencionado lo anterior, esta sección se enfoca en las consultas realizadas y los resultados que entregaron.

Todos los resultados se encuentran en la [carpeta del mismo nombre](/resultados/).

## Resultados de palabras más usadas

Los resultados de las palabras más usadas en todas las secciones de texto se encuentran en [CountWords.txt](/resultados/CountWords.txt).

- Palabras más usadas en todos lados:

    | Palabra | Ocurrencias |
    |----|--------|
    | 😆  | 219121 |
    | up | 53068  |
    | 🙂  | 48623  |

- Los comentarios raíz:

    | Palabra | Ocurrencias |
    |----|--------|
    | saludos | 9125 |
    | gracias | 9086 |
    | 🙂       | 8228 |

- Los comentarios hijos:

    | Palabra | Ocurrencias |
    |----|--------|
    | 😆  | 212297 |
    | up | 52788  |
    | 🙂  | 40395  |



## Los threads con más comentarios

Top 3 de temas más comentados. Los resultados (y Top 50) se encuentran en [MostAnsweredRoots.txt](/resultados/MostAnsweredRoots.txt).

| Thread | Cantidad de Comentarios |
|----|--------|
| a Beauchef le esta faltando Rock   | 138 |
| Baños Cafetería                    | 108 |
| juego de la palabra indefinido 2.0 | 107 |


## Top 10 _commenters_ por año

Se muestra el Top 3 resultados por año. En el archivo [TopCommanders.txt](/resultados/TopCommanders.txt) de la carpeta de resultados se ve el Top 10 por todos los años en los que el foro ha estado activo.

| Año| Persona(Cantidad de Comentarios) | | |
|----|--------|----|--------|
| 2002 | Mauricio Vásquez M.(93)      | Marcelo Vega F.(52)           | Jorge Ramirez Lores(49) |
| 2011 | Rodolfo Gutierrez Romo(7102) | Anibal Estabn Llanos Prado(3085) | Andrés V Aguirre(2923)  |
| 2018 | Cesar Diaz B.(259)           | Mario Medina Roa(201)            | Pablo Pizarro(195)      |

De esto resultados se puede observar que la actividad en el foro ha ido disminuyendo y se cree que esto se debe a APOGEO de las redes sociales, que progresivamente han ido ganando usuarios en los últimos años.

## Top _commenters_ de integrantes del curso 

Se muestra solo el Top 5 de _commenters_ del curso, tal como en la consulta mencionada en el punto anterior, el resultado completo está en el archivo [TopCommanders.txt](/resultados/TopCommanders.txt).

| Persona| Cantidad de Comentarios |
|----|--------|
| Simon Sepúlveda Osses | 570 |
| Juan P. Ruiz          | 535 |
| Adriana Concha        | 378 |
| Karina Parga          | 331 |
| Gabriel Chaperon B.   | 317 |



## Cantidad de personas distintas que comentan por año

Se muestran los años de inicio del foro, el último año calendario completo y el año en que el Top 10 de _commenters_ tenía una mayor cantidad de comentarios (2011). Los resultados se encuentran en [PersonsPerYear.txt](/resultados/PersonsPerYear.txt).


| Año | Cantidad de Commenters |
|----|--------|
| 2002 | 229  |
| 2011 | 3876 |
| 2018 | 3961 |


## Pares de personas que han discutido

Se entiende por una discusión cuando un par de personas  (A, B) comentan de tal forma que se produce la siguiente interacción al menos una vez en un thread: A comenta, B le responde a A, A le responde a B.

Los resultados completos se encuentran en el archivo [TopFighters.txt](/resultados/TopFighters.txt).

| Persona 1 | Persona 2 |Cantidad de discusiones|
|------------------------|------------------------|-----|
| Andrés V. Aguirre      | Carlos Cárdenas        | 123 |
| Carlos Alvarado Godoy  | Rodolfo Gutiérrez Romo | 112 |
| Ignacio Correa Falconi | Rodolfo Gutiérrez Romo | 85  |



## Otras consultas

También se hizo consultas con un fin más lúdico como:

- La cantidad de votaciones "+1/-1" que hubo antes y después de que se sacara el "+1/-1" del foro. Ver [PlusOneMinusOne.txt](/resultados/PlusOneMinusOne.txt).

- Cuántas comentarios hizo Patricio Aceituno antes y después que le dijieran "que bacan tu apellido". Ver [DuckOlives.txt](/resultados/DuckOlives.txt).

- Cuántos comentarios hizo Julio Salas antes y después del "incidente de las rodilleras". Ver [JulyRooms.txt](/resultados/JulyRooms.txt).



<!-- quien sabe -->
# Conclusión

Con respecto a la tecnología escogida, creemos que la sintaxis de Spark es agradable e intuitiva, es una tecnología que es popular y que además esta ganando popularidad. Por otro lado creemos que si bien java es quizá un poco verboso, en este caso nos sirvió para orientarnos con las estructuras de los RDD's y nos ayudó a evitar errores. Quizá si fueramos más experimentados con el framework la verbosidad generaría una incomodidad y sería más recomendable usar Scala o Python.

Con respecto a la complejidad del proyecto, hubo varias consultas que fueron de complejidad baja porque eran similares a las que vimos en clases. Notablemente la consulta de contar palabras más usadas era calcada, o consultas que requerían comparar cantidad de ocurrencias antes y después de un evento **IMPORTANTE** consistían simplemente en filtrar por fecha.


De todas formas hubo algunas consultas que fueron más difíciles, en particular la pregunta de discusiones fue la de mayor dificultad porque necesitó de 3 joins. Esto se debió a que se deben poner las llaves correctas en los PairRDD y hay que tener cuidado con los valores anidados que resultan de cada join. Además, esta consulta fue costosa de ejecutar. Para ésta pregunta pensamos en usar Neo4j, que es idóneo para crear el grafo que corresponde a las respuestas entre usuarios, pero esta tecnología se aprendió muy tarde en el ramo y no alcanzamos a usarla.

La consulta de los top comentadores por año también fue bastante difícil, pero ésta esta vez nos costo por el bajo manejo de Spark, ya que tuvimos problemas al tratar de usar agregación y la serialización de estructuras. Finalmente optamos por usar agrupación, a pesar de que según la documentación esto ocupa más recursos.


A largo del desarrollo del proyecto no guardamos ningún resultado obtenido, sino que los resultados lo mostrabamos en pantalla cada vez que ejecutabamos un _job_. Esto resulto en pérdidas inecesarias de tiempo cuando queríamos recuperar algún resultado previo. Esto es un aspecto que podría haber sido mejorado.


Se cree que la decisión de correr los jobs localmente fue una buena, porque no fuimos afectados por la alta congestión del cluster y la incomodidad de tener que subir nuestros ejecutables cada vez. Gracias a esto nuestros ciclos de desarrollo fueron más rápidos, porque podíamos probar nuestros cambios inmediatamente.

Por otro lado, trabajar con IntelliJ permite avanzar rápidamente gracias al autocompletado y la sugerencia de métodos posibles sobre un objeto.

De todas formas somos concientes que esto fue posible únicamente porque nuestro dataset era relativamente pequeño, pero el resto de conclusiones son aplicables a nuestro caso porque son independientes del tamaño del dataset.


<!-- que wea -->
# Appendix

Esto es un [apendice](https://www.efesalud.com/wp-content/blogs.dir/2/files/2018/09/apendice-e1537881196664.jpg)

