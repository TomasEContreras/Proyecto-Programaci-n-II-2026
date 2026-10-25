-- Datos de PRUEBA para ver el frontend (ejecutar DESPUÉS de Escuela_DDL.sql)
-- Los password_hash son texto de relleno: cuando se agregue Spring Security se reemplazan por hashes BCrypt reales.
USE jt_education_db_red;

INSERT INTO curso (id_curso, anio, division, ciclo_lectivo) VALUES
(1, 1, 'A', 2026),
(2, 2, 'B', 2026),
(3, 4, 'A', 2026);

INSERT INTO materia (id_materia, nombre) VALUES (1,'Matemática'),(2,'Lengua y Literatura'),(3,'Historia'),(4,'Biología'),(5,'Inglés'),(6,'Química');

-- Plan de materias de cada curso (N:M). Química solo se dicta en 4° A.
INSERT INTO curso_materia (curso_id_curso, materia_id_materia) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6);

INSERT INTO usuario (id_usuario, email, password_hash, rol) VALUES
(1, 'admin@jteducation.com', 'hash_de_relleno_admin', 'ADMIN'),
(2, 'lucia.fernandez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(3, 'mateo.gonzalez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(4, 'valentina.rodriguez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(5, 'santiago.lopez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(6, 'camila.martinez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(7, 'benjamin.perez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(8, 'sofia.gomez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(9, 'thiago.sanchez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(10, 'martina.diaz@mail.com', 'hash_de_relleno', 'ALUMNO'),
(11, 'joaquin.romero@mail.com', 'hash_de_relleno', 'ALUMNO'),
(12, 'emma.alvarez@mail.com', 'hash_de_relleno', 'ALUMNO'),
(13, 'lautaro.torres@mail.com', 'hash_de_relleno', 'ALUMNO');

INSERT INTO domicilio (id_domicilio, ciudad, calle, altura) VALUES
(1, 'Goya', 'San Martín', 100),
(2, 'Goya', 'Belgrano', 137),
(3, 'Goya', 'Sarmiento', 174),
(4, 'Goya', 'Mitre', 211),
(5, 'Goya', 'Rivadavia', 248),
(6, 'Goya', 'Urquiza', 285),
(7, 'Goya', 'Moreno', 322),
(8, 'Goya', 'Alberdi', 359),
(9, 'Goya', 'Colón', 396),
(10, 'Goya', 'Italia', 433),
(11, 'Goya', 'Pellegrini', 470),
(12, 'Goya', 'Córdoba', 507);

INSERT INTO alumno (id_alumno, dni, nombre, apellido, fecha_nacimiento, fecha_inscripcion, usuario_id_usuario, curso_id_curso, domicilio_id_domicilio) VALUES
(1, '40000000', 'Lucía', 'Fernández', '2012-01-01', '2026-03-01', 2, 1, 1),
(2, '41234567', 'Mateo', 'González', '2011-02-02', '2026-03-02', 3, 2, 2),
(3, '42469134', 'Valentina', 'Rodríguez', '2009-03-03', '2026-03-03', 4, 3, 3),
(4, '43703701', 'Santiago', 'López', '2012-04-04', '2026-03-04', 5, 1, 4),
(5, '44938268', 'Camila', 'Martínez', '2011-05-05', '2026-03-05', 6, 2, 5),
(6, '46172835', 'Benjamín', 'Pérez', '2009-06-06', '2026-03-01', 7, 3, 6),
(7, '47407402', 'Sofía', 'Gómez', '2012-07-07', '2026-03-02', 8, 1, 7),
(8, '48641969', 'Thiago', 'Sánchez', '2011-08-08', '2026-03-03', 9, 2, 8),
(9, '49876536', 'Martina', 'Díaz', '2009-09-09', '2026-03-04', 10, 3, 9),
(10, '51111103', 'Joaquín', 'Romero', '2012-10-10', '2026-03-05', 11, 1, 10),
(11, '52345670', 'Emma', 'Álvarez', '2011-11-11', '2026-03-01', 12, 2, 11),
(12, '53580237', 'Lautaro', 'Torres', '2009-12-12', '2026-03-02', 13, 3, 12);

INSERT INTO alumno_materia (alumno_id_alumno, materia_id_materia, primer_nota, segunda_nota, nota_final) VALUES
(1, 1, 6, 7, 8),
(1, 2, 8, 9, 10),
(1, 3, 10, 6, 7),
(1, 4, 7, 8, NULL),
(2, 1, 9, 10, 6),
(2, 2, 6, 7, 8),
(2, 3, 8, 9, 10),
(2, 4, 10, 6, NULL),
(3, 1, 7, 8, 9),
(3, 2, 9, 10, 6),
(3, 3, 6, 7, 8),
(3, 4, 8, 9, NULL);

-- Notas de los alumnos 4 a 12 (para ver distintos rendimientos: alto, medio, bajo, en curso y recién iniciado).
INSERT INTO alumno_materia (alumno_id_alumno, materia_id_materia, primer_nota, segunda_nota, nota_final) VALUES
(4, 1, 7, 8, 8),
(4, 2, 6, 6, 6),
(4, 3, 8, 9, 9),
(4, 4, 5, 6, 6),
(4, 5, 9, 9, 9),
(5, 1, 9, 10, 10),
(5, 2, 10, 9, 10),
(5, 3, 9, 9, 9),
(5, 5, 10, 10, 10),
(6, 1, 4, 5, 5),
(6, 2, 5, 4, 4),
(6, 3, 6, 5, 6),
(6, 4, 3, 4, NULL),
(7, 1, 7, 6, 7),
(7, 2, 8, 8, 8),
(7, 4, 6, 7, NULL),
(7, 5, 7, NULL, NULL),
(8, 1, 5, 7, 6),
(8, 2, 6, 8, 7),
(8, 3, 4, 6, 5),
(8, 5, 8, 9, 9),
(9, 1, 8, 9, 9),
(9, 2, 9, 10, 10),
(9, 3, 7, 8, 8),
(9, 4, 8, 8, 8),
(9, 5, 9, 10, 10),
(10, 1, 6, NULL, NULL),
(10, 2, 7, NULL, NULL),
(10, 3, 5, NULL, NULL),
(10, 4, 8, NULL, NULL),
(11, 1, 10, 9, 10),
(11, 2, 8, 9, 9),
(11, 3, 9, 10, 10),
(11, 4, 7, 8, 8),
(12, 1, 6, 5, 6),
(12, 2, 7, 7, 7),
(12, 4, 9, 8, 9),
(12, 5, 5, 6, 6);
