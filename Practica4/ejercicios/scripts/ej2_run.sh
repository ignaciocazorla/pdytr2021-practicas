#!/bin/bash

# Compilar agente
javac -classpath lib/jade.jar -d classes/ ejercicio2/AgenteMovil.java

# Correr agente
java -cp lib/jade.jar:classes jade.Boot -gui -container -container-name ejercicio2 -host localhost -agents "mov:AgenteMovil(numeros.txt)"
