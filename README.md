# NoSQL
Proyecto de la materia NoSQL Tecnólogo en Informática

En esta estructura, "ReferenciaPersona" es una referencia al documento de la persona en la colección personas. Esto permite una relación directa entre la información de la persona y sus domicilios.

Ventajas:

Flexibilidad en las consultas: Puedes realizar consultas en una colección sin necesidad de acceder a la otra, lo cual es útil en ciertos casos.
Separación de datos: Como las personas y los domicilios están en colecciones separadas, puedes modificar o escalar cada parte de tu base de datos de manera independiente.
Desventajas:

Manejo de referencias: Tienes que manejar manualmente las referencias entre documentos y asegurarte de mantener la integridad de los datos.
