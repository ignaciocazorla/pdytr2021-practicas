#!/bin/bash

# Limpiar directorio classes
rm classes/*

# Correr Main container
java -cp lib/jade.jar jade.Boot -gui -local-host 127.0.0.1 &

sleep 2

# Correr containers no principales
scripts/create_containers.sh &

sleep 2