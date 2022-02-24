#!/bin/bash

# Ejecuta el cliente y le da como argumento la cantidad de veces a ejecutar el cliente:
mvn -DskipTests exec:java -Dexec.mainClass=pdytr.example.grpc.Client -Dexec.args="100"