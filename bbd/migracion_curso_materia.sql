-- MIGRACIÓN para las bases locales que ya existían antes de agregar la tabla curso_materia.
-- (Si recreás la base desde cero con Escuela_DDL.sql + datos_prueba.sql, NO hace falta correr esto.)
-- Se puede ejecutar más de una vez sin romper nada: usa IF NOT EXISTS e INSERT IGNORE.
USE jt_education_db_red;

CREATE TABLE IF NOT EXISTS `curso_materia` (
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- Arma el plan de cada curso a partir de las notas que ya existen: si un alumno del curso tiene
-- nota en una materia, esa materia pasa a ser del curso. Así ninguna nota queda "fuera del plan".
INSERT IGNORE INTO curso_materia (curso_id_curso, materia_id_materia)
SELECT DISTINCT a.curso_id_curso, am.materia_id_materia
FROM alumno_materia am
JOIN alumno a ON a.id_alumno = am.alumno_id_alumno;
