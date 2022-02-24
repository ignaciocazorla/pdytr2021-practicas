#!/bin/bash

input="scripts/containers_list.txt"
while IFS= read -r line
do
  echo "Iniciando container $line"
  java -cp lib/jade.jar jade.Boot -container -container-name $line -host 127.0.0.1 &
done < "$input"