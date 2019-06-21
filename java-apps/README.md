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
* TopFighters
    1. roots
    2. childs
