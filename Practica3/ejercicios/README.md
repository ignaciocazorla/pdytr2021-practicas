# Pasos para generar el proyecto

$ mvn -DskipTests package
$ find target/generated-sources -name '*.java'

# Para correr un servidor:

$ mvn -DskipTests package exec:java -Dexec.mainClass=pdytr.example.grpc.App

# Para correr un cliente:

$ mvn -DskipTests exec:java -Dexec.mainClass=pdytr.example.grpc.Client

Si se desea darle argumentos al programa se puede hacer agregando:
-Dexec.args="arg1 arg2 arg3"

# Los ejercicio 1a, 1b y 1c incluyen el proto y las clases a utilizar.
# Es necesario copiarlas a mano dentro de donde se creo el proyecto.
# Son facilmente intercambiables, incluso con el ejercicio-5a, ya que usan el mismo proto.