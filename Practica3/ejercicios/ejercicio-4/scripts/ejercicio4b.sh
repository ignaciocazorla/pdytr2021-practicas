#!/bin/bash

OUTPUT=output/ejercicio4.txt
PATH1=database/cliente/proceso1.txt
PATH2=database/cliente/proceso2.txt
SERVER_PATH=database/server/prueba.txt

#Limpia el archivo $OUTPUT
echo -n > $OUTPUT

# Arrancar el servidor
echo "Inciando servidor..."
mvn -DskipTests package exec:java -Dexec.mainClass=pdytr.example.grpc.App &
server=$!
sleep 8

# Ejecutar dos clientes
echo "Ejecutando dos clientes en paralelo..."
for((i = 0; i<10; i++))
do
    echo "Ejecucion `expr $i + 1`"
    mvn -DskipTests exec:java -Dexec.mainClass=pdytr.example.grpc.Client -Dexec.args="-w ${PATH1} ${SERVER_PATH} 2" -q &
    pid1=$!
  
    mvn -DskipTests exec:java -Dexec.mainClass=pdytr.example.grpc.Client -Dexec.args="-w ${PATH2} ${SERVER_PATH} 2" -q &
    pid2=$!

    wait $pid1
    wait $pid2

    echo

    echo "Ejecucion `expr $i + 1`" >> $OUTPUT
    echo $(cat $SERVER_PATH) >> $OUTPUT
    echo >> $OUTPUT

    rm $SERVER_PATH
done

echo "Termino la ejecucion. Ver los resultados en $OUTPUT."

kill $server