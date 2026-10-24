# Propuesta de Proyecto

## JTEducation: Sistema de gestión académica para una escuela secundaria

### Descripción

JTEducation es una aplicación web para administrar la información académica básica de una escuela secundaria: los alumnos, los cursos en los que se inscriben, las materias que cursan y las notas que obtienen.

La idea es contar con un lugar centralizado donde el personal de la escuela pueda registrar y consultar esos datos, y donde cada alumno pueda ver su propia información, en lugar de manejarlos en planillas separadas.

### Alcance

El proyecto representa de forma simple pero realista la organización de una escuela secundaria por año y división (por ejemplo, 4° A).

**Incluye:**

- Gestión de alumnos, con su domicilio y su cuenta de acceso.
- Gestión de materias.
- Cursos organizados por año, división y ciclo lectivo.
- Registro y consulta de las notas de cada alumno en cada materia.
- Inicio de sesión con dos tipos de usuario, con permisos distintos.

**No incluye** (podrían ser ampliaciones futuras): docentes, registro de asistencia y acceso para padres o tutores.

### Usuarios y permisos

| Rol | Qué puede hacer |
|---|---|
| **Administrador** | Gestionar alumnos y materias (alta, modificación, baja y consulta) y cargar notas. |
| **Usuario final (alumno)** | Iniciar sesión y consultar su propia ficha: curso, materias y notas. |
