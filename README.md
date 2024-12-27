# NTTDATA Bootcamp Microservicios

## Descripción del Proyecto

Estoy participando en el Bootcamp de Microservicios organizado por NTTData, donde estoy desarrollando soluciones basadas en microservicios utilizando tecnologías como Spring Boot, MongoDB, Java, y WebFlux. Este bootcamp forma parte de mi proceso de formación continua en arquitectura de software y desarrollo de aplicaciones escalables.

Mi nombre es Bruno Andre Castro Barrientos y estoy comprometido en adquirir las habilidades necesarias para contribuir al desarrollo de soluciones tecnológicas de alto impacto en la industria.

## Tecnologías y Herramientas Utilizadas

- **Java 17**: Lenguaje de programación utilizado para el desarrollo de los microservicios.
- **Spring Boot Webflux**: Framework para la creación de aplicaciones reactivas utilizando Spring Boot.
- **MongoDB**: Base de datos NoSQL utilizada para almacenar los datos del sistema.
- **Maven**: Herramienta de construcción y gestión de dependencias.
- **Spring Data MongoDB Reactive**: Para manejar operaciones reactivas con MongoDB.
- **Swagger/OpenAPI**: Para generar y documentar las APIs de los microservicios.
- **Lombok**: Para reducir la cantidad de código boilerplate en el proyecto.
- **Spring Boot Test**: Framework de pruebas unitarias para probar los microservicios.
- **JaCoCo**: Para medir la cobertura del código durante las pruebas.
- **Checkstyle**: Para asegurar el cumplimiento de las convenciones de estilo de código.

## Enfoque Contract-First

Este proyecto utiliza el enfoque Contract-First para el desarrollo de las APIs. Primero se define la especificación de la API utilizando OpenAPI, y luego se generan las interfaces y modelos correspondientes basados en esa especificación. Todos los microservicios siguen los principios REST para los endpoints y los contratos de los servicios.

En particular, el contrato de la API se encuentra en el archivo bamking-api.yaml, ubicado en la carpeta resource. Este archivo define detalladamente las rutas, los parámetros de entrada y salida, así como los modelos de datos que son utilizados por todos los microservicios que forman parte del sistema.

## Funcionalidades del Sistema

### Proyecto I: Funcionalidades Básicas

- **Gestión de Clientes**: El sistema gestiona clientes personales y empresariales.
- **Productos Bancarios**:
  - **Pasivos**: Cuentas de Ahorro, Corrientes y a Plazo Fijo.
  - **Créditos**: Créditos personales y empresariales, además de tarjetas de crédito.
- **Operaciones Bancarias**:
  - Depósitos y retiros en cuentas.
  - Pagos a productos de crédito.
  - Carga de consumos en tarjetas de crédito dentro del límite disponible.
  - Consultas de saldos y movimientos de productos bancarios.

### Proyecto II: Nuevas Funcionalidades

- **Nuevos Perfiles de Clientes**:
  - **VIP**: Cuenta de ahorro con saldo promedio mínimo mensual, requiere tener tarjeta de crédito.
  - **PYME**: Cuenta corriente sin comisiones, requiere tener tarjeta de crédito.
- **Comisiones**:
  - Las cuentas bancarias tendrán un límite de transacciones sin comisiones, después de ese número, se cobrará comisión por transacción.
- **Transferencias Bancarias**: Transferencias entre cuentas de un mismo cliente y a terceros dentro del mismo banco.
- **Generación de Reportes**:
  - Reporte de saldo promedio de productos bancarios y créditos.
  - Reporte de comisiones cobradas por producto en un periodo de tiempo.

## Estructura del Proyecto

El sistema está dividido en microservicios independientes que gestionan distintas funcionalidades dentro del negocio bancario. Cada microservicio tiene su propia base de datos (siguiendo el patrón **Database per Service**) y se desarrollan utilizando **Spring WebFlux** para operaciones reactivas.

### Endpoints

- Los microservicios exponen **endpoints REST** definidos según las especificaciones de la API generadas con **OpenAPI**.
- La documentación de la API está disponible mediante **Swagger UI**, lo que permite consultar y probar los endpoints.

## Instrucciones de Ejecución

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/BrunoAndreCastroBarrientos/ms-banking-system.git
## Instrucciones de Ejecución

2. **Ejecutar un microservicio**:
   Desde la raíz del proyecto, navega a la carpeta de uno de los microservicios que desees ejecutar. Luego, utiliza el siguiente comando Maven para ejecutar el microservicio:
   ```bash
   mvn spring-boot:run
3. **Probar con Postman**:
   Dado que este proyecto no tiene interfaz gráfica, puedes interactuar con los microservicios utilizando **Postman** o cualquier otro cliente HTTP para probar los endpoints.

   Para obtener la lista de todos los endpoints disponibles, puedes acceder a la **documentación generada por Swagger/OpenAPI**. La UI de Swagger estará disponible en el siguiente URL (puede variar según la configuración del puerto):
http://localhost:8080/swagger-ui.html

Allí podrás ver todos los servicios disponibles, probar los endpoints y consultar la documentación generada automáticamente.

## Requerimientos de Calidad y Pruebas

- **Cobertura de Código**: Para garantizar la calidad del código, se utiliza **JaCoCo** para generar reportes de cobertura de pruebas y asegurar que el código esté correctamente cubierto.

- **Estilo de Código**: El proyecto sigue las convenciones de estilo de **Google**, que son verificadas mediante el plugin **Checkstyle** para asegurar que el código sea consistente y legible.

## Entregables

- **Código Fuente**: Todo el código fuente del proyecto está disponible en este repositorio de GitHub.

- **Documentación de la API**: La documentación de la API está generada automáticamente mediante **Swagger UI**, y puede ser consultada para probar los diferentes endpoints.

- **Pruebas y Cobertura de Código**: El proyecto incluye pruebas unitarias, y se generan reportes de cobertura de código utilizando **JaCoCo**.

---
**Este proyecto fue desarrollado por Bruno Andre Castro Barrientos**.


¡Gracias por revisar este proyecto! Si tienes preguntas o sugerencias, no dudes en abrir un **issue** en el repositorio o contactarme por Teams.
