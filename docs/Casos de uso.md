# Diagrama UML de casos de uso

![Diagrama UML de casos de uso de JTEducation](Casos%20de%20uso.svg)

## Actores

| Actor | Rol en el sistema | Descripción |
|---|---|---|
| **Usuario** | (abstracto) | Cualquier persona con cuenta. Agrupa lo que pueden hacer los dos roles: iniciar y cerrar sesión, y cambiar su contraseña. |
| **Administrador** | `ADMIN` | Personal de la escuela. Gestiona toda la información académica y las cuentas de otros administradores. |
| **Alumno** | `ALUMNO` (usuario final) | Estudiante inscripto. Solo consulta su propia información. |

La flecha con triángulo hueco es una **generalización**: el Administrador y el Alumno son tipos de Usuario, así que heredan sus casos de uso.

## Casos de uso

| # | Caso de uso | Actor | Descripción | Página |
|---|---|---|---|---|
| CU1 | Iniciar sesión | Usuario | Ingresa con su email y contraseña. El administrador va al inicio y el alumno a su ficha. | `/login` |
| CU2 | Cerrar sesión | Usuario | Termina la sesión y vuelve al login. | `POST /logout` |
| CU3 | Gestionar alumnos | Administrador | Alta (con su domicilio y su cuenta de acceso), listado paginado con búsqueda por apellido, nombre o DNI, modificación y baja. | `/alumnos` |
| CU4 | Gestionar materias | Administrador | Alta, listado, modificación y baja de materias. | `/materias` |
| CU5 | Gestionar cursos | Administrador | Alta, listado, modificación y baja de cursos (año y división; el ciclo lectivo lo asigna el sistema). | `/cursos` |
| CU6 | Asignar materias al curso | Administrador | Elige qué materias forman el plan del curso. | `/cursos/nuevo`, `/cursos/{id}/editar` |
| CU7 | Consultar ficha de un alumno | Administrador | Ve los datos, el curso, las materias y las notas de cualquier alumno. | `/alumnos/{id}` |
| CU8 | Cargar notas | Administrador | Registra la 1° nota, la 2° nota y la nota final de un alumno en una materia del plan de su curso. | `/alumnos/{id}/notas/{idMateria}` |
| CU9 | Consultar mi ficha | Alumno | Ve sus propios datos, su curso, sus materias y sus notas, sin poder modificarlos. | `/mi-ficha` |
| CU10 | Gestionar administradores | Administrador | Ve los administradores, crea uno nuevo (email y contraseña repetida) y elimina otros. No puede eliminar su propia cuenta. | `/administradores` |
| CU11 | Cambiar mi contraseña | Usuario | Cambia su propia contraseña: pide la actual, la nueva y repetirla. | `/cambiar-contrasena` |

## Relaciones entre casos de uso

- **«include» (CU5 → CU6):** gestionar un curso **siempre** incluye asignarle sus materias. El formulario de curso exige elegir al menos una.
- **«extend» (CU8 → CU7):** cargar notas es un paso **opcional** que se hace desde la ficha del alumno. Se puede consultar la ficha sin cargar notas.

## Permisos

Los permisos los controla Spring Security en `config/SeguridadConfig.java`:

| Ruta | Sin sesión | Alumno | Administrador |
|---|---|---|---|
| `/login`, CSS e imágenes | Sí | Sí | Sí |
| `/` (inicio) | Va al login | Lo redirige a `/mi-ficha` | Sí |
| `/mi-ficha` | Va al login | Sí | No (403) |
| `/cambiar-contrasena` | Va al login | Sí (la suya) | Sí (la suya) |
| `/alumnos`, `/cursos`, `/materias`, `/administradores` y las notas | Va al login | No (403) | Sí |

Un alumno solo puede ver su propia ficha: el sistema lo busca por el email de la sesión, nunca por un número en la URL.
