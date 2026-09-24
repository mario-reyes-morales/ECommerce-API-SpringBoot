# ECommerce API Spring Boot

Aplicación de comercio electrónico desarrollada con Java y Spring Boot. El proyecto evoluciona el backend de gestión de usuarios, artículos y compras hacia una aplicación persistente con Spring Data JPA, MySQL y una interfaz interactiva basada en Spring Shell.

Fue desarrollado de forma colaborativa por Alejandro Corona Ballester y Mario Reyes Morales como proyecto académico.

> Aunque el repositorio se denomina `ECommerce-API-SpringBoot`, la interfaz disponible actualmente es una aplicación de terminal basada en Spring Shell. No expone endpoints HTTP REST.

## Funcionalidades

- Creación y listado de usuarios.
- Validación del nombre y del correo electrónico.
- Detección de correos duplicados.
- Creación, consulta, listado y eliminación de artículos.
- Gestión de artículos con nombre, precio, categoría y fecha de registro.
- Creación de compras asociadas a un usuario y a un artículo.
- Consulta de compras por usuario.
- Consulta de compras relacionadas con un artículo.
- Consulta y eliminación de compras.
- Cálculo del precio total de las compras.
- Descuento por cantidad en las líneas de compra.
- Descuento adicional para compras cuyo importe supera los 100 euros.
- Carga automática de datos iniciales mediante `ShopSeeder`.
- Comandos interactivos con ayuda integrada.

## Tecnologías

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA
- Spring Shell
- MySQL para el entorno principal
- H2 para las pruebas
- Maven
- Jakarta Validation
- Lombok
- JUnit 5, Spring Boot Test y Mockito
- Log4j 2

## Arquitectura

La aplicación está organizada en varias capas:

- **Aplicación**: `AppShop` inicia Spring Boot y activa el escaneo de comandos.
- **Modelos**: entidades JPA `User`, `Article`, `Order` y `ShoppingLine`.
- **Repositorios**: interfaces Spring Data JPA para persistir y consultar las entidades.
- **Servicios**: `UserService`, `ArticleService` y `OrderService` concentran las reglas de negocio.
- **Presentación**: comandos Spring Shell para operar con usuarios, artículos y compras desde la terminal.
- **Validación**: `ValidationHandler` ejecuta las restricciones de Jakarta Validation antes de guardar entidades.
- **Inicialización**: `ShopSeeder` crea datos de ejemplo al arrancar la aplicación.

El flujo principal comienza en `AppShop`, que arranca el contexto de Spring. Spring registra los comandos de `UserCommand`, `ArticleCommand` y `OrderCommand`. Estos comandos delegan las operaciones en los servicios, que utilizan repositorios JPA para acceder a la base de datos.

## Comandos principales

Una vez iniciada la aplicación, Spring Shell permite consultar todos los comandos con:

```text
help
```

### Usuarios

```text
create-user --email usuario@example.com --name "Nombre del usuario"
list-users
```

### Artículos

```text
create-article --name "Producto" --price 19.99 --category "Categoría"
read-article --id 1
delete-article --id 1
list-articles
```

### Compras

```text
create-order --id 1 --product 1 --quantity 2
list-orders
list-orders-by-user --id 1
list-orders-by-article --id 1
read-order --id 1
delete-order --id 1
```

Los identificadores utilizados en los comandos corresponden a los registros existentes en la base de datos.

## Configuración

### Requisitos

- JDK 21.
- Maven.
- MySQL para ejecutar la aplicación principal.
- IntelliJ IDEA u otro IDE compatible con Maven.

### Base de datos principal

La configuración por defecto utiliza una base de datos MySQL local:

```text
jdbc:mysql://localhost:3306/poodb
```

El usuario y la contraseña se pueden configurar mediante variables de entorno:

```bash
DB_USERNAME=root
DB_PASSWORD=root
```

La base de datos `poodb` debe existir antes de iniciar la aplicación. El esquema se genera automáticamente al arrancar mediante `spring.jpa.hibernate.ddl-auto: create`.

La aplicación contiene datos iniciales de ejemplo con dos usuarios, tres artículos y dos compras. Como el esquema se recrea al iniciar, los datos persistidos anteriores se sustituyen en cada ejecución.

### Pruebas

Las pruebas utilizan una base de datos H2 en memoria y no requieren una instalación local de MySQL.

## Ejecución

Clonar el repositorio:

```bash
git clone https://github.com/mario-reyes-morales/ECommerce-API-SpringBoot.git
cd ECommerce-API-SpringBoot
```

Ejecutar las pruebas:

```bash
mvn clean test
```

Iniciar la aplicación:

```bash
mvn spring-boot:run
```

También se puede ejecutar la clase principal `upm.app.AppShop` desde IntelliJ IDEA.

## Estructura del proyecto

```text
src/
  main/
    java/upm/app/
      app/             punto de entrada de Spring Boot
      data/
        models/        entidades JPA y excepciones de dominio
        repositories/  repositorios y datos iniciales
      presentation/    comandos Spring Shell y validación
      services/        reglas de negocio y excepciones
    resources/
      application.yml  configuración de MySQL y Spring Shell
  test/
    java/upm/app/      pruebas de servicios y comandos
    resources/         perfil de pruebas con H2
pom.xml                configuración de Maven
LICENSE.md             licencia del proyecto, si está incluida
```

## Pruebas automatizadas

El proyecto incluye pruebas de integración con `@SpringBootTest` para comprobar, entre otros casos:

- creación y listado de usuarios;
- rechazo de usuarios con correo duplicado;
- creación de compras;
- errores cuando no existe el usuario o el artículo solicitado;
- búsqueda de compras por usuario;
- funcionamiento de los comandos de Spring Shell.

## Equipo

- Alejandro Corona Ballester
- Mario Reyes Morales

El proyecto se desarrolló de forma colaborativa, compartiendo el análisis, el diseño, la implementación, las pruebas y la documentación.
