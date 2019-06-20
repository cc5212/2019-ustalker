# 2019-ustalker
Los tenemos a todos identificados. [Gabriel Chaperon, Valeria Guidotti, Francisco Lecaros. Group 15]

# Como correr los jobs
Los datos se deberian encontrar en https://users.dcc.uchile.cl/~gchapero/ustalker/ustalker_wrepeat.tar.xz

Los jobs de spark fueron hecho con java. Para crear el .jar simplemente hay que moverse a la carpeta java-apps donde esta el archivo pom.xml y correr `mvn package`. Esto deberia bajar las dependencias y empaquetar el proyecto.

Luego, para poder correr los distintos jobs se tiene que ejecutar el siguiente comando en el cluster

```shell
spark-submit --class <NOMBRE DE LA CLASE> --master spark://cluster-01:7077 <NOBMRE DEL JAR>.jar <ARGS>

```
Cada job recibe args ligeramente distintos, pero todos son de la forma
```
hdfs://cm:9000/<UBICACION EN EL HDFS>
```
A continuacion se documentan los argumentos que reciben cada uno de los jobs
* CountWords
    1. stop_words
    2. roots
    3. childs
* DuckOlives
    1. roots 
    2. childs
* JulyRooms
    1. roots 
    2. childs
* MostAnsweredRoots
    1. roots 
    2. childs
* PersonsPerYear
    1. roots 
    2. childs
* PlusOneMinusOne
    1. roots
* TopCommanders
    1. roots 
    2. childs
    3. comrades
* TopCommentersPerTheme
    1. roots 
    2. childs 
* TopFighters
    1. roots
    2. childs


# Overview

State what is the main goal of the project. State what sorts of question(s) you want to answer or what sort of system you want to build. (Questions may be non-technical -- e.g., is there a global correlation between coffee consumption and research output -- so long as they require data analysis or other technical solutions.)

# Data

Describe the raw dataset that you considered for your project. Where did it come from? Why was it chosen? What information does it contain? What format was it in? What size was it? How many lines/records? Provide links.

# Methods

Detail the methods used during the project. Provide an overview of the techniques/technologies used, why you used them and how you used them. Refer to the source-code delivered with the project. Describe any problems you encountered.

# Results

Detail the results of the project. Different projects will have different types of results; e.g., run-times or result sizes, evaluation of the methods you're comparing, the interface of the system you've built, and/or some of the results of the data analysis you conducted.

# Conclusion

Summarise main lessons learnt. What was easy? What was difficult? What could have been done better or more efficiently?

# Appendix

You can use this for key code snippets that you don't want to clutter the main text.

hola que tal






### INFO
* grupo 15
* integrantes:
  * gabriel chaperon
  * valeria guidotti
  * francisco lecaro
