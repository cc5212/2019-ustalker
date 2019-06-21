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
# Methods

## Técnicas y tecnologías utilizadas

El desarrollo del proyecto se divide en dos partes.

### 1. Descarga del dataset

La descarga del dataset se realizó _web scraping_ del foro institucional de la FCFM con Python y BeautifulSoup. 

Para ver más sobre esto, acceder al directorio [intento-de-scrapper](/intento-de-scrapper/).

### 2. Procesamiento de datos

En el procesamiento de datos se realizó localmente utilizó Apache Spark 2.3.3 para para Hadoop 2.7.3. Además se procesaron los datos con Spark 2.1.0 para Hadoop 2.3.0 en el HDFS provisto en el curso.

Los *jobs* de Spark fueron escritos en Java 8. Estos se encuentran en el directorio [java-apps](/java-apps/src/main/java).

<!-- lecaro -->
# Results

Detail the results of the project. Different projects will have different types of results; e.g., run-times or result sizes, evaluation of the methods you're comparing, the interface of the system you've built, and/or some of the results of the data analysis you conducted.

     | saludos | 9125 |
     |---------|------|
     | gracias | 9086 |
     | 🙂       | 8228 |


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
