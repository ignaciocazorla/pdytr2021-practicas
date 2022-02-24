#!/bin/bash

# Compilar agente
javac -classpath lib/jade.jar -d classes/ ejercicio3/AgenteMovil.java ejercicio3/Lector.java

echo "[INFO] --- Descomentar operacion que se desea realizar en ej3_run.sh"

# Limpieza de entorno para correr el ejercicio.
scripts/ej3_cleanup.sh

# Correr agente
# Archivo mp3:
## Lectura
# java -cp lib/jade.jar:classes jade.Boot -gui -container -container-name ejercicio3 -host localhost -agents "mov:AgenteMovil(r, ejercicio3/database_remota/cancion.mp3, ejercicio3/database_cliente/cancion.mp3)"

## Escritura
# java -cp lib/jade.jar:classes jade.Boot -gui -container -container-name ejercicio3 -host localhost -agents "mov:AgenteMovil(w, ejercicio3/database_cliente/cancion.mp3, ejercicio3/database_remota/cancion_copia.mp3)"

# Archivo pdf:
## Lectura
# java -cp lib/jade.jar:classes jade.Boot -gui -container -container-name ejercicio3 -host localhost -agents "mov:AgenteMovil(r, ejercicio3/database_remota/prac4.pdf, ejercicio3/database_cliente/prac4.pdf)"

## Escritura
# java -cp lib/jade.jar:classes jade.Boot -gui -container -container-name ejercicio3 -host localhost -agents "mov:AgenteMovil(w, ejercicio3/database_cliente/prac4.pdf, ejercicio3/database_remota/prac4_copia.pdf)"