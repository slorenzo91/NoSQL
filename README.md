
![Logo](https://res.cloudinary.com/practicaldev/image/fetch/s--VnDDBkku--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://dev-to-uploads.s3.amazonaws.com/uploads/articles/akm5od0383lcbhxvb9zb.png)


# Domicilios noSQL.

Segundo laboratorio del taller noSQL. El segundo laboratorio consiste en la implementación de una solución de software sin interfaz gráfica, cuyos servicios son ofrecidos y consultados únicamente a través de servicios rest, y cuyos datos sean almacenados y extraídos de una base de datos nosql.





## Autores.

- [Pedro Aldama](https://github.com/LordAmbush)
- [Pablo Cristiani](https://github.com/pcristiani)
- [Santiago Lorenzo](https://github.com/slorenzo91)
- [Eduardo Allo](https://www.github.com/eduardoAllo)


## Intercambio de datos.

Todo el intercambio de información fue realizado mediante JSON a través de
servicios rest.





## Tecnologias Utilizadas.

- Springboot.
- Java.
- Firebase.
- Firestore.
- Apache tomcat.
- JMeter.
- Docker.
- Postman.


## Instalacion y configuracion.

- Instalar jdk17 de Java.
- Clonar el repositorio de forma local.
- Instalar [SpringTool suite](https://download.springsource.com/release/STS4/4.20.0.RELEASE/dist/e4.29/spring-tool-suite-4-4.20.0.RELEASE-e4.29.0-win32.win32.x86_64.self-extracting.jar). Luego de descargado el jar, descomprimirlo. Descomprimir content.zip. Ingresar a la carpeta sts-4.20.0.RELEASE y ejecutar SpringToolSuite4.exe.
- Importar el proyecto clonado como maven project.
- Correr el proyecto como SpringBootApp.


    
## Endpoints.

#### Obtener direccion por persona

```http
  GET /nosql/domicilio/obtenerPorPersona/{idPersona}
```

| Parameter   | Type     | Description                |
| :--------   | :------- | :------------------------- |
| `idPersona` | `string` | **Required**. Cedula buscada |

Endpoint para obtener todos los domicilios de la persona buscada. Se envia en string y se recibe un JSON.
Si la persona no existe retorna: Error 402.

#### Obtener direccion por criterio

```http
  GET /nosql/domicilio/obtenerPorCriterio?departamento=valor&barrio=valor&localidad=valor
```

| Query parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `departamento`      | `string` | Departamento buscado |
| `barrio`      | `string` | Barrio buscado |
| `localidad`      | `string` | Localidad buscada |

Endpoint para obtener todos los domicilios segun los criterios enviados por query parameters. Los parametros se pueden combinar como sea necesario.

#### Agregar Persona

```http
  POST /nosql/persona/agregar
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `ci`      | `string` |  **Required** Cedula de identidad |
| `nombre`      | `string` | **Required** Nombre |
| `apellido`      | `string` | **Required** Apellido |
| `edad`      | `int` | **Required** Edad |

Endpoint para agregar una persona a la base de datos. En caso de que la Cedula de identidad ya exista en la base de datos retorna: Error 401

#### Agregar Domicilio

```http
  POST /nosql/domicilio/agregar
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `idPersona`      | `string` |  **Required** Referencia a la persona |
| `departamento`      | `string` | **Required** Departamento |
| `calle`      | `string` | **Required** Calle |
| `numero`      | `int` | **Required** Numero |
| `localidad`      | `string` | Localidad |
| `apartamento`      | `string` |  Apartamento |
| `padron`      | `string` | Padron |
| `ruta`      | `string` | Ruta |
| `letra`      | `string` | Letra |
| `barrio`      | `string` | Barrio |

Endpoint para agregar un domicilio a la base de datos. En caso de que la persona referenciada no exista retorna: Error 402.



## Diseño de esquema.

Utilizamos Firebase ya que ofrece escalabilidad en tiempo real, integración sencilla con otros servicios de Firebase, gestión de datos sin servidor y una configuración rápida para aplicaciones web y móviles.

Usamos una estructura Referencial, haciendo referencia desde el domicilio al documento de la persona en su coleccion. Esto permite una relación directa entre la información de la persona y sus domicilios.

#### Ventajas:
- Flexibilidad en las consultas: Se pueden realizar consultas en una colección sin necesidad de acceder a la otra, lo cual es útil en ciertos casos.
- Separación de datos: Como las personas y los domicilios están en colecciones separadas, se puede modificar o escalar cada parte de la base de datos de manera independiente.

#### Desventajas:
- Manejo de referencias: Se deben manejar manualmente las referencias entre docuemtnos para mantener la integridad de los datos.



## Prueba de carga con JMeter.

#### Obtener direccion por persona

```http
  GET /nosql/domicilio/obtenerPorPersona/{idPersona}
```
- Se realizaron 200 test de la API y todos fueron efectivos. Los datos generales fueron los siguientes:

| Label       | # Samples | Average | Min | Max | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------------|-----------|---------|-----|-----|----------|---------|------------|-----------------|-------------|------------|
| getAddress  | 200       | 118     | 0   | 193 | 13.03    | 0.000%  | 19.77066   | 18.65           | 5.08        | 966.2      |
| TOTAL       | 200       | 118     | 0   | 193 | 13.03    | 0.000%  | 19.77066   | 18.65           | 5.08        | 966.2      |

#### Obtener direccion por criterio

```http
  GET /nosql/domicilio/obtenerPorCriterio?departamento=valor&barrio=valor&localidad=valor
```
- Se realizaron 200 test de la API y todos fueron efectivos. Los datos generales fueron los siguientes:

| Label                | # Samples | Average | Min | Max | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|----------------------|-----------|---------|-----|-----|----------|---------|------------|-----------------|-------------|------------|
| getAddressByCriteria | 200       | 668     | 0   | 754 | 26.29    | 0.000%  | 18.00991   | 38.92           | 5.58        | 2213.0     |
| TOTAL                | 200       | 668     | 0   | 754 | 26.29    | 0.000%  | 18.00991   | 38.92           | 5.58        | 2213.0     |

#### Agregar Persona

```http
  POST /nosql/persona/agregar
```

- Se realizaron 100 test de la API los 50 primeros fueron efectivos mientras que los otros 50 retornaron error porque ya existian. Los datos generales fueron los siguientes:

| Label       | # Samples | Average | Min | Max | Std. Dev. | Error %  | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------------|-----------|---------|-----|-----|----------|----------|------------|-----------------|-------------|------------|
| addPerson   | 100       | 86      | 0   | 260 | 38.26    | 50.000%  | 10.10101   | 1.97            | 3.78        | 200.0      |
| TOTAL       | 100       | 86      | 0   | 260 | 38.26    | 50.000%  | 10.10101   | 1.97            | 3.78        | 200.0      |

#### Agregar Domicilio

```http
  POST /nosql/domicilio/agregar
```

- Se realizaron 200 test de la API y todos fueron efectivos. Los datos generales fueron los siguientes:

| Label       | # Samples | Average | Min | Max | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------------|-----------|---------|-----|-----|----------|---------|------------|-----------------|-------------|------------|
| addAddress  | 200       | 121     | 0   | 451 | 32.08    | 0.000%  | 19.75114   | 4.30            | 11.16       | 223.0      |
| TOTAL       | 200       | 121     | 0   | 451 | 32.08    | 0.000%  | 19.75114   | 4.30            | 11.16       | 223.0      |

### Conclusiones

Tiempo de Respuesta Promedio: 
- (86 + 121 + 118 + 668) / 4 = 248.25 ms
Throughput Promedio: 
- (10.10101 + 19.75114 + 19.77066 + 18.00991) / 4 = 16.90818 solicitudes/segundo
Received KB/sec Promedio: 
- (1.97 + 4.30 + 18.65 + 38.92) / 4 = 15.96 KB/s
Sent KB/sec Promedio: 
- (3.78 + 11.16 + 5.08 + 5.58) / 4 = 6.4 KB/s
Avg. Bytes Promedio: 
- (200.0 + 223.0 + 966.2 + 2213.0) / 4 = 900.55 bytes

#### Conclusión:
- En promedio, las funciones tienen un tiempo de respuesta de 248.25 ms. Gestionan aproximadamente 16.9 solicitudes por segundo. En cuanto a la transferencia de datos, el sistema recibe un promedio de 15.96 KB/s y envía un promedio de 6.4 KB/s. Cada respuesta tiene un tamaño promedio de 900.55 bytes. 
- Es importante mencionar que la función "getAddressByCriteria" tiene tiempos de respuesta significativamente más altos que las otras funciones, lo que influye en el promedio general.




