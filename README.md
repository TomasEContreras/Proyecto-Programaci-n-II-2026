# JTEducation

Sistema de gestión académica para una escuela secundaria: alumnos, cursos, materias y notas.
Segundo parcial de Programación II (INSPT). La propuesta completa está en [Propuesta.md](Propuesta.md).

## Documentación

- [Mapa del sitio](docs/Mapa%20del%20sitio.md): páginas, navegación y rutas.
- [Casos de uso](docs/Casos%20de%20uso.md): diagrama UML, actores y permisos de cada rol.
- Diagrama Entidad-Relación: [modelo conceptual](bbd/DER.drawio.png) y [modelo físico](bbd/EER.svg) de la base.

## Tecnologías

- Java 21 y Spring Boot 4 (Spring MVC, Spring Data JPA, Spring Security, Bean Validation)
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

   y abrir <http://localhost:8080>. Sin sesión iniciada, la aplicación pide el login.

## Usuarios y permisos

| Rol | Qué puede hacer | Página al ingresar |
|---|---|---|
| **Administrador** | Gestionar alumnos, cursos y materias, cargar notas y crear o eliminar otros administradores. | Inicio (`/`) |
| **Alumno** (usuario final) | Consultar su propia ficha: datos, curso, materias y notas. | Mi ficha (`/mi-ficha`) |

Los dos roles pueden cambiar su propia contraseña desde el menú ("Cambiar contraseña").

Las contraseñas se guardan hasheadas con BCrypt. La sesión y los permisos los maneja Spring Security (`config/SeguridadConfig.java`). Si un alumno intenta entrar a la gestión, ve el error 403 "No tenés permiso".

## Tests

```bash
./mvnw test
```

- **Tests unitarios** (`src/test/java`): validaciones de las entidades, formato de las notas y reglas de los servicios (alumnos, administradores y cambio de contraseña). No necesitan la base de datos: los repositorios se reemplazan con Mockito.
- `JteducationApplicationTests` levanta la aplicación completa, así que necesita la base de datos creada.

## Estructura (MVC)

```
src/main/java/com/LOTUS/jteducation/
├── config/       Configuración de Spring Security (login, permisos por rol, BCrypt)
├── entity/       Modelo: entidades JPA con sus validaciones (Alumno, Curso, Materia, AlumnoMateria, ...)
├── repository/   Acceso a datos (Spring Data JPA)
├── service/      Reglas de negocio y transacciones
├── controller/   Controladores: reciben la petición, llaman al servicio y eligen la vista
├── dto/          Datos de los formularios (no se enlaza el formulario directo con la entidad)
├── exception/    Excepciones propias y manejo centralizado de errores
└── util/         Validaciones comunes
src/main/resources/templates/   Vistas Thymeleaf
bbd/                            Scripts SQL y diagramas de la base
docs/                           Documentación
```

## Requisitos del parcial

| # | Requisito | Estado | Dónde |
|---|---|---|---|
| 1 | Aplicación web tradicional con motor de plantillas | Hecho | Thymeleaf, `src/main/resources/templates/` |
| 2 | Backend Java con patrón MVC | Hecho | `entity/` + `templates/` + `controller/`, con `service/` y `repository/` |
| 3 | CRUD completo con validaciones de al menos una entidad | Hecho | Alumno, Curso y Materia. Validaciones en los DTO (`dto/`), en las entidades y en los servicios |
| 4 | Manejo de excepciones en operaciones críticas | Hecho | `exception/GlobalExceptionHandler`, `ReglaDeNegocioException`, `RecursoNoEncontradoException` |
| 5 | Base relacional con relaciones 1:1, 1:N y N:M | Hecho | [`bbd/Escuela_DDL.sql`](bbd/Escuela_DDL.sql), [DER](bbd/DER.drawio.png) y [EER](bbd/EER.svg) |
| 6 | Documentación: mapa del sitio y DER | Hecho | [Mapa del sitio](docs/Mapa%20del%20sitio.md) y diagramas en [`bbd/`](bbd/) |
| 7 | Interfaz funcional, moderna y responsive | Hecho | `static/css/style.css`. Probado en celular (375 px): ninguna página se desborda |
| 8 | Dos tipos de usuario (final y administrador) | Hecho | Roles `ADMIN` y `ALUMNO`; permisos en `config/SeguridadConfig.java`; `/mi-ficha` para el alumno |
| 9 | Manejo de sesión y autenticación | Hecho | Spring Security: login propio, logout, BCrypt, CSRF y cambio de id de sesión al ingresar |
| 10 | Diagrama UML de casos de uso | Hecho | [Casos de uso](docs/Casos%20de%20uso.md) |
