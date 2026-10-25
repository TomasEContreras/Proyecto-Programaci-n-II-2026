# JTEducation

Sistema de gestión académica para una escuela secundaria: alumnos, cursos, materias y notas.
Segundo parcial de Programación II (INSPT). La propuesta completa está en [Propuesta.md](Propuesta.md).

## Documentación

- [Mapa del sitio](docs/mapa-del-sitio.md): páginas, navegación y rutas.
- [Diagrama Entidad-Relación](docs/DER.md): tablas, relaciones 1:1, 1:N y N:M, y reglas de negocio.

## Tecnologías

- Java 21 y Spring Boot 4 (Spring MVC, Spring Data JPA, Bean Validation)
- Thymeleaf como motor de plantillas
- MySQL 8
- Lombok

## Cómo levantar el proyecto

1. **Crear la base de datos.** En MySQL, ejecutar en este orden:
   1. [`bbd/Escuela_DDL.sql`](bbd/Escuela_DDL.sql): crea la base `jt_education_db_red` y sus tablas. **Si la base ya existe, la borra y la crea de nuevo**, así que sirve también para actualizarla después de un cambio en las tablas.
   2. [`bbd/datos_prueba.sql`](bbd/datos_prueba.sql): carga cursos, materias, alumnos y notas de ejemplo.

   Usuarios de prueba:

   | Rol | Email | Contraseña |
   |---|---|---|
   | Administrador | `admin@jteducation.com` | `Admin1234` |
   | Alumno | el email de cualquier alumno (por ejemplo `lucia.fernandez@mail.com`) | `Alumno1234` |

2. **Configurar la conexión** en [`src/main/resources/application.properties`](src/main/resources/application.properties) (usuario y contraseña de tu MySQL). La URL se puede cambiar sin tocar el archivo con la variable de entorno `DB_URL`.

3. **Ejecutar la aplicación:**

   ```bash
   ./mvnw spring-boot:run
   ```

   y abrir <http://localhost:8080>.

## Estructura (MVC)

```
src/main/java/com/LOTUS/jteducation/
├── entity/       Modelo: entidades JPA con sus validaciones (Alumno, Curso, Materia, AlumnoMateria, ...)
├── repository/   Acceso a datos (Spring Data JPA)
├── service/      Reglas de negocio y transacciones
├── controller/   Controladores: reciben la petición, llaman al servicio y eligen la vista
├── dto/          Datos de los formularios (no se enlaza el formulario directo con la entidad)
├── exception/    Excepciones propias y manejo centralizado de errores
└── util/         Validaciones comunes
src/main/resources/templates/   Vistas Thymeleaf
bbd/                            Scripts SQL y diagramas originales
docs/                           Documentación
```

## Requisitos del parcial

| # | Requisito | Estado | Dónde |
|---|---|---|---|
| 1 | Aplicación web tradicional con motor de plantillas | Hecho | Thymeleaf, `src/main/resources/templates/` |
| 2 | Backend Java con patrón MVC | Hecho | `entity/` + `templates/` + `controller/`, con `service/` y `repository/` |
| 3 | CRUD completo con validaciones de al menos una entidad | Hecho | Alumno, Curso y Materia. Validaciones en los DTO (`dto/`), en las entidades y en los servicios |
| 4 | Manejo de excepciones en operaciones críticas | Hecho | `exception/GlobalExceptionHandler`, `ReglaDeNegocioException`, `RecursoNoEncontradoException` |
| 5 | Base relacional con relaciones 1:1, 1:N y N:M | Hecho | Ver [DER](docs/DER.md) |
| 6 | Documentación: mapa del sitio y DER | Hecho | Carpeta [`docs/`](docs/) |
| 7 | Interfaz funcional, moderna y responsive | En progreso | `static/css/style.css` |
| 8 | Dos tipos de usuario (final y administrador) | Pendiente | Roles definidos en `entity/Rol` y en la tabla `usuario` |
| 9 | Manejo de sesión y autenticación | Pendiente | |
| 10 | Diagrama UML de casos de uso | Pendiente | |
