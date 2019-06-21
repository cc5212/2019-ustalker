# Descripcion de los datos
Los datos se pueden encontrar en el siguiente enlace https://users.dcc.uchile.cl/~gchapero/ustalker/ustalker_wrepeat.tar.xz

Hay filas repetidas en ambos archivos

## root
comentarios raiz en ucursos
las columnas son
```
(id, titulo, autor, fecha, categoria, mensaje)
```
* el id deberia ser unico
* autor es solo el nombre que sale en ucursos, puede que se repita
* la fecha esta en unix time
* el mensaje solo deberia contener el caracter espacio, no saltos de linea ni tabulaciones

## child
comentarios hijo
las columnas son
```
(id, id del comentario raiz, id del papa directo, autor, fecha, mensaje)
```

Mismos comentarios que para root donde aplica

# Datos extra

Ademas usamos una lista de stop words sacada de internet y la lista de los integrantes del curso.