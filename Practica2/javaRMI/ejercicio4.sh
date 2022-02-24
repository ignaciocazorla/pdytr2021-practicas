#!/bin/bash

OUTPUT=output/ejercicio4.txt
CLI_PATH1=database/client/archivo_proc_1.txt
CLI_PATH2=database/client/archivo_proc_2.txt
SERVER_PATH=database/server/data.txt

#Limpia el archivo $OUTPUT
echo -n > $OUTPUT

#Compilacion de archivos java
javac AskRemote.java StartRemoteObject.java RemoteClass.java IfaceRemoteClass.java

#Inicio del servidor
java StartRemoteObject &
server=$!
sleep 1

#Ejecuta para escribir
echo "Ejecutando dos clientes en paralelo..."
for((i = 0; i<10; i++))
do
    java AskRemote -4 $CLI_PATH1 $SERVER_PATH &
    pid1=$!
    java AskRemote -4 $CLI_PATH2 $SERVER_PATH &
    pid2=$!
    wait $pid1
    wait $pid2

    echo "Ejecucion `expr $i + 1`" >> $OUTPUT
    echo $(cat $SERVER_PATH) >> $OUTPUT
    echo >> $OUTPUT

    rm $SERVER_PATH
done

echo "Termino la ejecucion. Ver los resultados en $OUTPUT."

kill $server