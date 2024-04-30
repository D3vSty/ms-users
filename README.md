# API RESTFul de creación de usuarios
***

## Requisitos previos

Antes de comenzar, asegúrate de tener instalado:

- **Java Development Kit (JDK)**: La aplicación está desarrollada en Java, por lo que necesitarás tener instalado JDK. Puedes descargarlo [aquí](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- **Postman**: Herramienta útil para probar APIs. Puedes descargarla [aquí](https://www.postman.com/downloads/).
- **Git**: Para clonar el repositorio de la aplicación.

## Configuración inicial

1. Clona el repositorio de la aplicación desde GitHub:

   ```bash
   git clone https://github.com/D3vSty/ms-users.git
   ```
2. Abrir en cualquier IDE de su preferencia (Intellij Idea, Eclipse, etc).
3. Ejecutar la aplicacion.

## Probar API

### Login

Para probar la API RESTFul debe loguearse en /api/v1/auth/login

Ejemplo de `Login` en Postman:

```json
{
  "email": "juan@rodriguez.org",
  "password": "hunter2"
}
```

Al ingresar los datos correctos retornaria un token que tendra una duracion de 10 minutos.

Ejemplo de `Token` en Postman:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlei5vcmciLCJpYXQiOjE3MTIyNTMwMjIsImV4cCI6MTcxMjI1MzYyMn0.Sm2VavDyUM7jb76uB8-rTedV9mEy2BHWEwKihVrDVOw"
}
```
Probar los servicios restantes con el token retornado; haciendo uso de la coleccion MS-USERS en PostMan.
***

Para ver la covertura de Test Unitarios ejecutar el comando:
   ```mvn
   mvn test
   ```
***
Luego ir a:
   ```jacoco
   target 
    > site
      >  jacoco
          > index.html
   ```
Y abrir index.html en su navegador de preferencia