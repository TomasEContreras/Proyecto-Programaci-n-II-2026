-- =====================================================================
-- JTEducation - Estructura de la base de datos (MySQL 8)
--
-- Uso: ejecutar este script y después datos_prueba.sql.
-- ATENCIÓN: borra la base jt_education_db_red si ya existe y la crea de nuevo vacía.
--
-- Relaciones (ver docs/DER.md):
--   1:1  alumno - usuario    (alumno.usuario_id_usuario es UNIQUE)
--   1:1  alumno - domicilio  (cada alumno tiene su propio domicilio, lo garantiza la aplicación)
--   1:N  curso  - alumno
--   N:M  curso  - materia    (tabla curso_materia: el plan de materias de cada curso)
--   N:M  alumno - materia    (tabla alumno_materia: las notas)
-- =====================================================================

DROP SCHEMA IF EXISTS `jt_education_db_red`;
CREATE SCHEMA `jt_education_db_red` DEFAULT CHARACTER SET utf8mb4;
USE `jt_education_db_red`;


-- -----------------------------------------------------
-- Tabla `usuario`: cuenta de acceso (alumnos y administradores)
-- -----------------------------------------------------
CREATE TABLE `usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,      -- contraseña hasheada con BCrypt, nunca en texto plano
  `rol` ENUM('ALUMNO', 'ADMIN') NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla `domicilio`
-- -----------------------------------------------------
CREATE TABLE `domicilio` (
  `id_domicilio` INT NOT NULL AUTO_INCREMENT,
  `ciudad` VARCHAR(45) NOT NULL,
  `calle` VARCHAR(45) NOT NULL,
  `altura` INT NOT NULL,
  PRIMARY KEY (`id_domicilio`),
  CONSTRAINT `chk_domicilio_altura` CHECK (`altura` > 0))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla `curso`: un año y división de un ciclo lectivo (ej.: 4° A 2026)
-- -----------------------------------------------------
CREATE TABLE `curso` (
  `id_curso` INT NOT NULL AUTO_INCREMENT,
  `anio` INT NOT NULL,
  `division` ENUM('A', 'B', 'C') NOT NULL,
  `ciclo_lectivo` INT NOT NULL,               -- lo asigna el sistema: el año en que se crea el curso
  PRIMARY KEY (`id_curso`),
  UNIQUE INDEX `curso_UNIQUE` (`anio` ASC, `division` ASC, `ciclo_lectivo` ASC),
  CONSTRAINT `chk_curso_anio` CHECK (`anio` BETWEEN 1 AND 6),
  CONSTRAINT `chk_curso_ciclo` CHECK (`ciclo_lectivo` >= 2000))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla `materia`
-- -----------------------------------------------------
CREATE TABLE `materia` (
  `id_materia` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_materia`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC))   -- la intercalación ignora mayúsculas y acentos
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla `alumno`
-- -----------------------------------------------------
CREATE TABLE `alumno` (
  `id_alumno` INT NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(8) NOT NULL,                  -- 7 u 8 dígitos, como texto para no perder ceros a la izquierda
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `fecha_inscripcion` DATE NOT NULL,
  `usuario_id_usuario` INT NOT NULL,
  `curso_id_curso` INT NOT NULL,
  `domicilio_id_domicilio` INT NOT NULL,
  PRIMARY KEY (`id_alumno`),
  UNIQUE INDEX `dni_UNIQUE` (`dni` ASC),
  UNIQUE INDEX `usuario_id_usuario_UNIQUE` (`usuario_id_usuario` ASC),
  INDEX `fk_alumno_curso1_idx` (`curso_id_curso` ASC),
  INDEX `fk_alumno_domicilio1_idx` (`domicilio_id_domicilio` ASC),
  CONSTRAINT `fk_alumno_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_curso1`
    FOREIGN KEY (`curso_id_curso`)
    REFERENCES `curso` (`id_curso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_domicilio1`
    FOREIGN KEY (`domicilio_id_domicilio`)
    REFERENCES `domicilio` (`id_domicilio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla `curso_materia`: relación N:M, el plan de materias de cada curso.
-- No tiene datos propios, por eso la clave primaria es la combinación de las dos claves foráneas.
-- -----------------------------------------------------
CREATE TABLE `curso_materia` (
  `curso_id_curso` INT NOT NULL,
  `materia_id_materia` INT NOT NULL,
  PRIMARY KEY (`curso_id_curso`, `materia_id_materia`),
  INDEX `fk_curso_materia_materia1_idx` (`materia_id_materia` ASC),
  CONSTRAINT `fk_curso_materia_curso1`
    FOREIGN KEY (`curso_id_curso`)
    REFERENCES `curso` (`id_curso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_curso_materia_materia1`
    FOREIGN KEY (`materia_id_materia`)
    REFERENCES `materia` (`id_materia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla `alumno_materia`: relación N:M con datos propios (las notas de un alumno en una materia).
-- Las notas pueden ser NULL mientras no se cargan.
-- -----------------------------------------------------
CREATE TABLE `alumno_materia` (
  `id_al_mat` INT NOT NULL AUTO_INCREMENT,
  `primer_nota` DECIMAL(4,2) NULL,
  `segunda_nota` DECIMAL(4,2) NULL,
  `nota_final` DECIMAL(4,2) NULL,
  `alumno_id_alumno` INT NOT NULL,
  `materia_id_materia` INT NOT NULL,
  PRIMARY KEY (`id_al_mat`),
  UNIQUE INDEX `alumno_materia_UNIQUE` (`alumno_id_alumno` ASC, `materia_id_materia` ASC),
  INDEX `fk_alumno_materia_materia1_idx` (`materia_id_materia` ASC),
  CONSTRAINT `fk_alumno_materia_alumno1`
    FOREIGN KEY (`alumno_id_alumno`)
    REFERENCES `alumno` (`id_alumno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_materia_materia1`
    FOREIGN KEY (`materia_id_materia`)
    REFERENCES `materia` (`id_materia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_primer_nota` CHECK (`primer_nota` IS NULL OR `primer_nota` BETWEEN 1 AND 10),
  CONSTRAINT `chk_segunda_nota` CHECK (`segunda_nota` IS NULL OR `segunda_nota` BETWEEN 1 AND 10),
  CONSTRAINT `chk_nota_final` CHECK (`nota_final` IS NULL OR `nota_final` BETWEEN 1 AND 10))
ENGINE = InnoDB;
