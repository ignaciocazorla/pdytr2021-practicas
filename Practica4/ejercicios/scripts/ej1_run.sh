#!/bin/bash

# Compilar agente
javac -classpath lib/jade.jar -d classes/ ejercicio1/AgenteMovil.java

# Correr agente
java -cp lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents mov:AgenteMovil

