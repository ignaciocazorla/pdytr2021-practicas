#!/bin/bash

for ((i=0; i<10; i++))
do
    mvn -DskipTests exec:java -Dexec.mainClass=pdytr.example.grpc.Client
done