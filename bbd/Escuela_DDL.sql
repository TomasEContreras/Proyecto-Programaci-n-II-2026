-- Script DDL generado a partir de EER_Escuela.mwb
-- Orden de creación respeta las dependencias de FK

CREATE DATABASE IF NOT EXISTS JT_Education_DB;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS escuela;
CREATE TABLE escuela (
  numero INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  direccion VARCHAR(100) NOT NULL,
  telefono VARCHAR(45) NOT NULL,
  PRIMARY KEY (numero)
);

DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  email VARCHAR(45) NOT NULL,
  password_hash VARCHAR(45) NOT NULL,
  rol VARCHAR(45) NOT NULL,
  escuela_numero INT NOT NULL,
  PRIMARY KEY (id_usuario),
  CONSTRAINT fk_usuario_escuela FOREIGN KEY (escuela_numero) REFERENCES escuela (numero)
);

DROP TABLE IF EXISTS alumno;
CREATE TABLE alumno (
  id_alumno INT NOT NULL AUTO_INCREMENT,
  dni VARCHAR(45) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  apellido VARCHAR(45) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  usuario_id_usuario INT NOT NULL,
  PRIMARY KEY (id_alumno),
  CONSTRAINT fk_alumno_usuario1 FOREIGN KEY (usuario_id_usuario) REFERENCES usuario (id_usuario)
);

DROP TABLE IF EXISTS anio;
CREATE TABLE anio (
  id_anio INT NOT NULL AUTO_INCREMENT,
  numero INT NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_anio)
);

DROP TABLE IF EXISTS curso;
CREATE TABLE curso (
  id_curso INT NOT NULL AUTO_INCREMENT,
  division VARCHAR(45) NOT NULL,
  ciclo_lectivo VARCHAR(45) NOT NULL,
  escuela_numero INT NOT NULL,
  anio_id_anio INT NOT NULL,
  PRIMARY KEY (id_curso),
  CONSTRAINT fk_curso_escuela1 FOREIGN KEY (escuela_numero) REFERENCES escuela (numero),
  CONSTRAINT fk_curso_anio1 FOREIGN KEY (anio_id_anio) REFERENCES anio (id_anio)
);

DROP TABLE IF EXISTS inscripcion;
CREATE TABLE inscripcion (
  id_inscripcion INT NOT NULL AUTO_INCREMENT,
  fecha_inscripcion DATE NOT NULL,
  curso_id_curso INT NOT NULL,
  alumno_id_alumno INT NOT NULL,
  PRIMARY KEY (id_inscripcion),
  CONSTRAINT fk_inscripcion_curso1 FOREIGN KEY (curso_id_curso) REFERENCES curso (id_curso),
  CONSTRAINT fk_inscripcion_alumno1 FOREIGN KEY (alumno_id_alumno) REFERENCES alumno (id_alumno)
);

DROP TABLE IF EXISTS materia;
CREATE TABLE materia (
  id_materia INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_materia)
);

DROP TABLE IF EXISTS nota;
CREATE TABLE nota (
  id_nota INT NOT NULL AUTO_INCREMENT,
  periodo VARCHAR(45) NOT NULL,
  tipo VARCHAR(45) NOT NULL,
  valor INT NOT NULL,
  fecha DATE NOT NULL,
  inscripcion_id_inscripcion INT NOT NULL,
  materia_id_materia INT NOT NULL,
  PRIMARY KEY (id_nota),
  CONSTRAINT fk_nota_inscripcion1 FOREIGN KEY (inscripcion_id_inscripcion) REFERENCES inscripcion (id_inscripcion),
  CONSTRAINT fk_nota_materia1 FOREIGN KEY (materia_id_materia) REFERENCES materia (id_materia)
);

DROP TABLE IF EXISTS anio_materia;
CREATE TABLE anio_materia (
  carga_horaria DATETIME NOT NULL,
  anio_id_anio INT NOT NULL,
  materia_id_materia INT NOT NULL,
  PRIMARY KEY (anio_id_anio, materia_id_materia),
  CONSTRAINT fk_anio_materia_anio1 FOREIGN KEY (anio_id_anio) REFERENCES anio (id_anio),
  CONSTRAINT fk_anio_materia_materia1 FOREIGN KEY (materia_id_materia) REFERENCES materia (id_materia)
);

SET FOREIGN_KEY_CHECKS = 1;