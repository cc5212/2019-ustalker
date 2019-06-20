# Descripcion de los datos
los fixed son los buenos
pueden haber filas repetidas en ambos

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

mismos comentarios que para root donde aplica