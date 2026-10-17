-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema jt_education_db_red
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema jt_education_db_red
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jt_education_db_red` DEFAULT CHARACTER SET utf8 ;
USE `jt_education_db_red` ;

-- -----------------------------------------------------
-- Table `jt_education_db_red`.`curso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jt_education_db_red`.`curso` (
  `id_curso` INT NOT NULL AUTO_INCREMENT,
  `anio` INT(11) NOT NULL,
  `division` ENUM('A', 'B', 'C') NOT NULL,
  `ciclo_lectivo` INT(11) NOT NULL,
  PRIMARY KEY (`id_curso`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jt_education_db_red`.`domicilio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jt_education_db_red`.`domicilio` (
  `id_domicilio` INT NOT NULL AUTO_INCREMENT,
  `ciudad` VARCHAR(45) NOT NULL,
  `calle` VARCHAR(45) NOT NULL,
  `altura` INT(11) NOT NULL,
  PRIMARY KEY (`id_domicilio`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jt_education_db_red`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jt_education_db_red`.`usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `rol` ENUM('STD', 'ADMIN') NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jt_education_db_red`.`alumno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jt_education_db_red`.`alumno` (
  `id_alumno` INT NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(45) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `fecha_inscripcion` DATE NOT NULL,
  `usuario_id_usuario` INT NOT NULL,
  `curso_id_curso` INT NOT NULL,
  `domicilio_id_domicilio` INT NOT NULL,
  PRIMARY KEY (`id_alumno`),
  UNIQUE INDEX `dni_UNIQUE` (`dni` ASC) ,
  INDEX `fk_alumno_curso1_idx` (`curso_id_curso` ASC) ,
  INDEX `fk_alumno_domicilio1_idx` (`domicilio_id_domicilio` ASC) ,
  INDEX `fk_alumno_usuario1_idx` (`usuario_id_usuario` ASC) ,
  UNIQUE INDEX `usuario_id_usuario_UNIQUE` (`usuario_id_usuario` ASC) ,
  CONSTRAINT `fk_alumno_curso1`
    FOREIGN KEY (`curso_id_curso`)
    REFERENCES `jt_education_db_red`.`curso` (`id_curso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_domicilio1`
    FOREIGN KEY (`domicilio_id_domicilio`)
    REFERENCES `jt_education_db_red`.`domicilio` (`id_domicilio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `jt_education_db_red`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jt_education_db_red`.`materia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jt_education_db_red`.`materia` (
  `id_materia` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_materia`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jt_education_db_red`.`alumno_materia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jt_education_db_red`.`alumno_materia` (
  `id_al_mat` INT NOT NULL AUTO_INCREMENT,
  `primer_nota` DECIMAL(4,2) NOT NULL,
  `segunda_nota` DECIMAL(4,2) NOT NULL,
  `nota_final` DECIMAL(4,2) NOT NULL,
  `alumno_id_alumno` INT NOT NULL,
  `materia_id_materia` INT NOT NULL,
  PRIMARY KEY (`id_al_mat`),
  INDEX `fk_alumno_materia_materia1_idx` (`materia_id_materia` ASC),
  UNIQUE INDEX `alumno_materia_UNIQUE` (`alumno_id_alumno` ASC, `materia_id_materia` ASC),
  CONSTRAINT `fk_alumno_materia_alumno1`
    FOREIGN KEY (`alumno_id_alumno`)
    REFERENCES `jt_education_db_red`.`alumno` (`id_alumno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_alumno_materia_materia1`
    FOREIGN KEY (`materia_id_materia`)
    REFERENCES `jt_education_db_red`.`materia` (`id_materia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_notas` CHECK (
      `primer_nota` BETWEEN 1 AND 10
      AND `segunda_nota` BETWEEN 1 AND 10
      AND `nota_final` BETWEEN 1 AND 10))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
