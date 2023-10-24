FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/*.jar

##Creamos una carpeta dentro del contenedor para mantener nuestro código.
WORKDIR /app

##Copiamos el archivo JAR construido a la carpeta que creamos en el contenedor.
##Asegúrate de que ya has construido tu aplicación y el JAR esté en el directorio 'target/'.
COPY target/domicilios-0.0.1-SNAPSHOT.jar domicilio.jar
COPY serviceAccountKey.json serviceAccountKey.json

EXPOSE 8080

##El comando para ejecutar nuestra aplicación cuando iniciemos un contenedor a partir de la imagen.
ENTRYPOINT ["java", "-jar", "domicilio.jar"]



