# 2019-ustalker
Proyecto de CC5212 Procesamiento masivo de datos, en donde analizamos un dataset extraido del foro de la facultad.



> En este foro no se mueve una hoja sin que yo lo sepa
>
> -- <cite>[Gabriel Chaperon, Valeria Guidotti, Francisco Lecaros. Group 15]</cite>


<!-- gabriel -->
# Descripcion del contenido del repositorio
En el directorio data se encuentra una pequeña descripcion de los datos junto con archivos extra que fueron usados para el proyecto, como las stop words y la lista de integrantes del curso.


<!-- lecaro -->
# Overview

<!-- holaaaaaaaaaaaaaaaaaaaaa -->
EL obejtivo general del proyecto es ver el comportamiento de la comunidad en el foro de la facultad (y buscar memes). La idea es ver como ha sido el comportamiento a traves de los años viendo, por ejemplo, el flujo de los comentarios y ver si hay correlacion con eventos que han ocurrido en la facultad o el cambio de tecnologias. Otro ejemplo es ver quienes son los que mas comentan en el foro de la facultad, quienes son las personas que mas discuten en el foro y otras consultas relevantes para historia de la facultad.

<!-- gabriel -->
# Data

Describe the raw dataset that you considered for your project. Where did it come from? Why was it chosen? What information does it contain? What format was it in? What size was it? How many lines/records? Provide links.


<!-- guido -->
# Metodología

## Técnicas y tecnologías utilizadas

El desarrollo del proyecto se divide en dos partes.

### 1. Descarga del dataset

La descarga del dataset se realizó _web scraping_ del foro institucional de la FCFM con Python y BeautifulSoup. 

Para ver más sobre esto, acceder al directorio [intento-de-scrapper](/intento-de-scrapper/).

### 2. Procesamiento de datos

En el procesamiento de datos se realizó localmente utilizó Apache Spark 2.3.3 para para Hadoop 2.7.3. Además se procesaron los datos con Spark 2.1.0 para Hadoop 2.3.0 en el HDFS provisto en el curso.

Los *jobs* de Spark fueron escritos en Java 8. Estos se encuentran en el directorio [java-apps](/java-apps/src/main/java).

La elección de Spark como *engine* para el *data processing* se explica por la ventaja en el tiempo que toma correr los *jobs* comparado con el tiempo de Hadoop y Apache Pig. Al comparar Spark con Hadoop, el primero ofrece facilidades para escribir el código de las aplicaciones de manera más rápida.

El último motivo de la elección de Spark el aumento y superioridad en el interés mundial de Apache Spark por sobre Apache Hadoop y Apache Pig lo que lo hace más atrayente aumentar el conocimiento grupal en esta herramienta.

<script type="text/javascript" src="https://ssl.gstatic.com/trends_nrtr/1845_RC03/embed_loader.js"></script> <script type="text/javascript"> trends.embed.renderExploreWidget("TIMESERIES", {"comparisonItem":[{"keyword":"Apache Spark","geo":"","time":"2014-06-21 2019-06-21"},{"keyword":"Apache Hadoop","geo":"","time":"2014-06-21 2019-06-21"},{"keyword":"Apache Pig","geo":"","time":"2014-06-21 2019-06-21"}],"category":0,"property":""}, {"exploreQuery":"date=today%205-y&q=Apache%20Spark,Apache%20Hadoop,Apache%20Pig","guestPath":"https://trends.google.es:443/trends/embed/"}); </script>


<!-- lecaro -->
# Results

Detail the results of the project. Different projects will have different types of results; e.g., run-times or result sizes, evaluation of the methods you're comparing, the interface of the system you've built, and/or some of the results of the data analysis you conducted.


<!-- quien sabe -->
# Conclusion

Summarise main lessons learnt. What was easy? What was difficult? What could have been done better or more efficiently?


<!-- que wea -->
# Appendix

You can use this for key code snippets that you don't want to clutter the main text.


### INFO
* grupo 15
* integrantes:
  * gabriel chaperon
  * valeria guidotti
  * francisco lecaro
