PROYECTO

## Electiva 3: ARQ-WEB

## Trabajo Práctico: Primer Parcial

```
Prof.: JOSÉ RODRIGO BENITEZ P. rbenitez@pol.una.py
07 - 09 - 2021
```

Este Trabajo involucra la implementación del Front-End (@primer*parcial) de y Back-end (@segundo*
parcial), un sistema informático que será
especificado en este trabajo.

El proyecto desarrollado se validará con peticiones GET, POST,
PUT y DELETE que deberán elaborar y facilitar para el día de la revisión de manera a validar el
funcionamiento. Como complemento también pueden tener preparadas consultas (select) SQL
como apoyo a las peticiones GET para verificar el estado de los datos.

Revisiones y entrega

- Entrega del TP:
- A agendar en las semanas de parciales.

Grupos de alumnos

Los grupos serán de 2 alumnos como máximo, uno de los cuales estará identificado como el Líder
del Proyecto (Project Leader), quien tendrá la obligación de tratar todos los temas concernientes a
este trabajo con el cliente (profesor).

Observaciones recomendadas

1. Para la implementación puede utilizar el IDE de su elección (Eclipse, Netbeans, IntelliJ IDEA).
2. Como servidor de aplicación deben utilizar Wildfly (versión elección).
3. La base de datos debe ser Postgres (versión 9.x para facilitar la compatibilidad).
4. El gestor de proyecto DEBE ser MAVEN.
5. Tiene que cumplir el stack tecnológico JEE:
   a. Modelo: JPA (con Hibernate, o algún otro ORM que sea una implementación de JPA)
   b. Capa de negocios: EJB
   c. Capa de exposición: JAX-RS (Restful)
6. Para el día de la entrega y defensa del TP deben estar presentes TODOS los integrantes
   del grupo, y el que no esté presente lleva ausente.

Se establecerá un día y hora para cada grupo y la sesión será solamente para los
integrantes de ese GRUPO.

8. Para los procesos que deben ejecutarse planificadamente cada cierto tiempo, pueden
   investigar acerca de los session bean @Singleton y de los timer en ejb3 (@Schedule).
   @Para el Segundo Parcial.

# Enunciado 1: Sistema de fidelización de clientes

Se requiere la implementación de un sistema WEB de fidelización de clientes que
pueda hacer seguimiento de los puntos otorgados por operaciones de pago. Los
módulos a desarrollar son los siguientes:

1. Administración de datos del cliente (POST,GET,PUT, DELETE)

Este módulo contempla la administración de datos del cliente, los cuales serán los que
acumulen puntos de fidelidad con sus operaciones.

Los datos de clientes a almacenar serán los siguientes (campos_propuestos): nombre,
apellido, número de
documento, tipo de documento, nacionalidad, email, teléfono, fecha de nacimiento.
Estructura: id autogenerado, nombre, apellido, número de documento, tipo de
documento, nacionalidad, email, teléfono, fecha de nacimiento.

2. Administración de conceptos de uso de puntos (POST,GET,PUT, DELETE)

Este módulo contempla la administración de los diferentes conceptos que especifican a
qué fueron destinados los puntos utilizados, con su respectiva cantidad de puntos
requerida. Por ejemplo: vale de premio, vale de descuento, vale de consumición, etc.
Estructura: id autogenerado, descripción de concepto, puntos requeridos.

3. Administración de reglas de asignación de puntos (POST,GET,PUT, DELETE)
   Este módulo permite definir las reglas que rigen la cantidad de puntos a asignar a un
   cliente en base al rango de valor de consumo:

- Ejemplo:
  o 0 a 199.999 Gs.: 1 punto cada 50.
  o 200.000 Gs. a 499.999 Gs. 1 punto cada 30.
  o 500.000 Gs. para arriba: 1 punto cada 20.

Observación: TODO VERIFICAR, los rangos serán opcionales, es decir, se puede tener una sola regla que
asigne 1 punto cada X Gs. sin importar en qué rango cae el monto de la operación.
Estructura: id autogenerado, limite inferior, límite superior, monto de equivalencia de 1
punto

4. Parametrización de vencimientos de puntos (POST,GET,PUT, DELETE)

Este módulo permite definir el tiempo de validez de los puntajes asignados a los clientes.
Una vez alcanzado el tiempo determinado, los puntos son descontados de la bolsa por
vencimiento.
Estructura: id autogenerado, fecha de inicio de validez, fecha fin de validez, días de

duración del puntaje.

5. Bolsa de puntos

Estructura: id autogenerado, identificador del cliente, fecha de asignación de puntaje,
fecha de caducidad de puntaje, puntaje asignado, puntaje utilizado, saldo de puntos,
monto de la operación

6. TODO VERIFICAR Uso de puntos
   Debe utilizarse en un esquema FIFO (primero se utilizan las bolsas más antiguas). Tiene
   un detalle debido a que para satisfacer una petición de puntos se puede utilizar más de
   una bolsa.

Estructura:

- Cabecera: id autogenerado, identificador del cliente, puntaje utilizado, fecha,
  concepto de uso de punto
- Detalle: id autogenerado, identificador de la cabecera, puntaje utilizado, identificador
  de la bolsa de puntos utilizada

7. TODO VERIFICAR - Se puede usar 'jpaMetamodelFiltering' Consultas (GET)

Este módulo contempla la consulta para el desarrollo de reportes.
Las consultas a proveer son:

- uso de puntos por: concepto de uso, fecha de uso, cliente
- bolsa de puntos por: cliente, rango de puntos
- clientes con puntos a vencer en x días
- consulta de clientes por: nombre (aproximación), apellido (aproximación),
  Cumpleaños

8. TODO Servicios

- carga de puntos (POST):se recibe el identificador de cliente y el monto de la
  operación, y se asigna los puntos (genera datos con la estructura del punto 5)
- utilizar puntos (POST):se recibe el identificador del cliente y el identificador del
  concepto de uso y se descuenta dicho puntaje al cliente registrando el uso de puntos
  (genera datos con la estructura del punto 6 y actualiza la del punto 5)
  o además debe enviar un correo electrónico al cliente como comprobante
- consultar cuantos puntos equivale a un monto X (GET):es un servicio
  informativo que devuelve la cantidad de puntos equivalente al monto proporcionado
  como parámetro utilizando la configuración del punto 3

9. TODO Proceso planificado cada x horas

Proceso que pueda planificarse que corra cada X horas y actualice el estado de
las bolsas con puntos vencidos.

Observaciones Generales.

1. Todos los integrantes del grupo deben estar preparados para cualquier
   pregunta del Profesor. No se considerarán desarrollo por partes.

Es decir, si bien, la distribución de desarrollo es importante, todos los
integrantes deben conocer el funcionamiento de todas las unidades del
Negocio.

# jhipster

This application was generated using JHipster 7.3.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.3.0](https://www.jhipster.tech/documentation-archive/v7.3.0).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/jhipster-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the jhipster application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.3.0 archive]: https://www.jhipster.tech/documentation-archive/v7.3.0
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.3.0/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.3.0/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.3.0/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.3.0/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.3.0/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.3.0/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[webpack]: https://webpack.github.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
[angular cli]: https://cli.angular.io/
