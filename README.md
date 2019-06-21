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
# Data

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


_Nota: No fueron encontrados problemas en el desarrollo del proyecto._

<!-- lecaro -->
# Results

Dado que durante el proyecto no se trabajo con un dataset grande no se medio el tiempo que se demoraban las consultas dado que no proporcionaba ningun dato interesante. 

Dado lo anterior nos enfocaremos en las consultas que se hicieron y los resultados que entregaron.Ahora mostraremos parte de los resulatdos ya que poner los resultados completos 

Resultados de palabras mas usadas:

-Palabras mas usadas en todos lados:

| 😆  | 219121 |
|----|--------|
| up | 53068  |
| 🙂  | 48623  |

-Los comentarios raíz:

| saludos | 9125 |
|---------|------|
| gracias | 9086 |
| 🙂       | 8228 |

-Los comentarios hijos:

| 😆  | 212297 |
|----|--------|
| up | 52788  |
| 🙂  | 40395  |



Los threads con mas comentarios:

| a Beauchef le esta faltando Rock   | 138 |
|------------------------------------|-----|
| Baños Cafetería                    | 108 |
| juego de la palabra indefinido 2.0 | 107 |



Cantidad de personas que comentan por año(ordenado por año):

| 2002 | 229  |
|------|------|
| 2011 | 3876 |
| 2018 | 3961 |



Top 10 commenters por año(por temas de espacio se mostraran 3 por año):

| 2002 | Mauricio Vásquez M./93      | Marcelo Vega F./52              | Jorge Ramirez Lores/49 |
|------|-----------------------------|---------------------------------|------------------------|
| 2011 | Rodolfo Gutierrez Romo/7102 | Anibal Estabn Llanos Prado/3085 | Andrés V Aguirre/2923  |
| 2018 | Cesar Diaz B./259           | Mario Medina Roa/201            | Pablo Pizarro/195      |



Top commenters del curso: 

| Simon Sepúlveda Osses | 570 |
|-----------------------|-----|
| Juan P. Ruiz          | 535 |
| Adriana Concha        | 378 |
| Karina Parga          | 331 |
| Gabriel Chaperon B.   | 317 |



Par de personas que han discutido:

Se entiende por una discusion cuando un par de personas  A, B comentan de tal forma que se produce la siguiente interaccion al menos una vez: A comenta, B le responde a A, A le responde de vuelta a B.

| Andrés V. Aguirre      | Carlos Cárdenas        | 123 |
|------------------------|------------------------|-----|
| Carlos Alvarado Godoy  | Rodolfo Gutiérrez Romo | 112 |
| Ignacio Correa Falconi | Rodolfo Gutiérrez Romo | 85  |



Tambien se hicieron consultas con un fin mas ludico como:

-La cantidad de votaciones "+1/-1" que han habido antes y despues de que se sacara el "+1/-1" del foro.

-Cuantas veces ha comentado Patricio Aceituno antes y despues que le dijieran "que bacan tu apellido".

-Cuantas veces ha comentado antes y despues del "incidente de las rodilleras".



<!-- quien sabe -->
# Conclusion




Con respecto a la tecnologia escogida, creemos que la sintaxis de spark es agradable e intuitiva, es una tecnologia que es popular y que ademas esta ganando popularidad. Por otro lado creemos que si bien java es quiza un poco verboso, en este caso nos sirvio para orientarnos con las estructuras de los RDD's y nos ayudo a evitar errores. Quiza si fueramos mas experimentados con el framework la verbosidad seria un problema mayor y seria mas recomendable usar Scala o Python.

Con respecto a la complejidad del proyecto, ubo varias consultas que fueron de complejidad baja porque eran similares a las que vimos en clases. Notablemente la consulta de contar palabras mas usadas era calcada, o consultas que requerian comparar cantidad de ocurrencias antes y despues de un evento IMPORTANTE consistian simplemente en filtrar por fecha.


De todas formas hubo algunas consultas que fueron mas dificiles, en particular la pregunta de discusiones fue bastante dificil porque hubo que hacer 3 joins. Esto se debio a que hay que poner las llaves correctas en los PairRDD y hay que tener cuidado con los valores anidados que resultan de cada join y ademas fue costosa de ejecutar. Para esta pregunta pensamos en usar Neo4j, que es idoneo para crear el grafo que corresponde a las respuestas entre usuarios, pero esta tecnologia se aprendio muy tarde en el ramo y no alcanzamos a usarla.

La consulta de los top comentadores por anno tambien fue bastante dificil, pero esta esta vez nos costo por el bajo manejo de spark, ya que tuvimos problemas al tratar de usar aggregacion y la serializacion de estructuras. Finalmente optamos por usar agrupacion a pesar de que segun la documentacion esto ocupa mas recursos.


A largo del desarrollo del proyecto no guardamos ningun resultado obtenido, sino que los resultados lo mostrabamos en pantalla cada vez que ejecutabamos un job. Esto resulto en perdidas inecesarias de tiempo cuando queriamos recuperar algun resultados previo. Esto creemos que podriamos haberlo hecho mejor.


Tomar la decision de correr los jobs localmente creemos que fue una buena decision porque no fuimos afectados por la alta congestion del cluster y la incomodidad de tener que subir nuestros ejecutables cada vez. Gracias a esto nuestros ciclos de desarrollo fueron mas rapidos porque podiamos probar nuestros cambios inmediatamente.

Por otro lado, trabajar con intelliJ permite avanzar rapidamente gracias al autocompletado y la sugerencia de metodos posibles sobre un objeto.

De todas formas somos concientes que esto fue posible unicamente porque nuestro dataset era relativamente pequenno, pero el resto de conclusiones son aplicables a nuestro caso porque son independientes del tamanno del dataset.


Finalmente 
las redes sociales mataron el foro,  a partir del aprox 2014 

<!-- que wea -->
# Appendix

You can use this for key code snippets that you don't want to clutter the main text.


### INFO
* grupo 15
* integrantes:
  * gabriel chaperon
  * valeria guidotti
  * francisco lecaro
ssss