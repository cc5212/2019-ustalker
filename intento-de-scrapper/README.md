# Como correr test_login.py
Pa correr la wea, primero instalar requisitos (usen ipython es bonito).

Esto corre solamente con python 3.6, con python 3.7 no me funciono. Quiza tengan que usar un entorno virtual


```shell
pip install -r requirements.txt
```

El script viene con un mensaje de help, asique siempre pueden ver que hace poniendo
```shell
python scraper.py -h
```


Para la autenticacion hay dos opciones.

La primera es guardar tus datos en un pickle y el script los abre y ocupa pa loguearse
(esto se puede hacer en una shell de python en la carpeta del escrip)
```python
import pickle
d = {
  "username": "mi_nombre_de_usuario",
  "password": "mi_contrasenna"
}
pickle.dump(d, open('user_data.pic', 'wb'))
```

La otra opcion es no poner nada y el scrip va a preguntar por nombre de usuario y contrasenna. Esto quiza es menos comodo si hay que correrlo varias veces.

El escrip necesita un rango de paginas que descargar, esos se le pasan por linea de comandos.

Ademas opcionalmente se puede setear el tamanno del batch (default 10) usando la flag -b

Un ejemplo de ejecucion teniendo el pickle en la misma carpeta seria
```shell
python scraper.py 0 100 -b 5 -l data.pic
```
Esto baja de la pagina 1 hasta la pagina 100, en grupos de 5 usando los datos
del usuario que estan en data.pic




# Como correr test_concurrent.py
Este escrip le hace peticiones a example.com y guarda datos de la pagina (los mismos cada vez) en un archivito que se llama out.tsv. En el main del escrip se pueden modificar la cantidad total de requests y la cantidad que se hace a la vez. Pa correrlo primero se deberian instalar los requisitos en caso que no esten actualizados
```shell
pip install -r requirements.txt
```

y luego correrlo simplemente con 
```shell
python3 test_login.py
```

# Notas sobre formatos de hora.
Aca voy a poner los posibles formatos de hora que he pillado en los mensajes de u-cursos:


Relativos a la fecha actual:
* Hoy, hace "MM" mins
* Hoy, hace "SS" segs
* Hoy, a las "HH:MM" hrs.
* Ayer, a las "HH:MM" hrs.


Absolutos:
* "NOMBRE_DIA" "DD" de "NOMBRE_MES" a las "HH:MM" hrs.
* "DD" de "NOMBRE_MES" a las "HH:MM" hrs.
* (parece que el cambio entre las dos anteriores fue el 24/04/19 xD)
* "DD/MM/YY" a las "HH:MM" hrs.
* (y parece que el cambio de esta a las anteriores fue el 01/01/19)


# Que weas seria bueno guardar.
## Para el OP
* id
* titulo
* autor
* fecha
* tema
* mensaje

## Para las respuesta
* id
* id del thread al que pertenece
* id a quien responde
* autor
* fecha
* mensaje
