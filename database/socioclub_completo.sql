-- SocioClub - Script de creacion y datos iniciales
-- MySQL 8.4 - ejecutar sobre una base nueva/vacia.
-- No ejecutar sobre la base compartida ya poblada: los INSERT usan IDs fijos.

CREATE DATABASE IF NOT EXISTS `socioclub`
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `socioclub`;

CREATE TABLE `socio` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(8) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `email` VARCHAR(150) DEFAULT NULL,
  `telefono` VARCHAR(30) DEFAULT NULL,
  `estado_cuota` ENUM('AL_DIA','MOROSO') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_socio_dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `actividad` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `disciplina` VARCHAR(100) NOT NULL,
  `profesor` VARCHAR(100) NOT NULL,
  `dias` VARCHAR(100) NOT NULL,
  `horario` TIME NOT NULL,
  `edad_minima` INT NOT NULL,
  `edad_maxima` INT NOT NULL,
  `cupo_maximo` INT NOT NULL,
  `cuota_mensual` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `inscripcion` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `socio_id` BIGINT NOT NULL,
  `actividad_id` BIGINT NOT NULL,
  `fecha_inscripcion` DATE NOT NULL,
  `estado` ENUM('ACTIVA','BAJA') NOT NULL DEFAULT 'ACTIVA',
  PRIMARY KEY (`id`),
  KEY `fk_inscripcion_socio` (`socio_id`),
  KEY `fk_inscripcion_actividad` (`actividad_id`),
  CONSTRAINT `fk_inscripcion_socio` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id`),
  CONSTRAINT `fk_inscripcion_actividad` FOREIGN KEY (`actividad_id`) REFERENCES `actividad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `actividad`
 (`id`,`nombre`,`disciplina`,`profesor`,`dias`,`horario`,`edad_minima`,`edad_maxima`,`cupo_maximo`,`cuota_mensual`)
VALUES
 (1,'Fútbol infantil','Fútbol','Diego Ferreyra','Lun y Mié','18:00:00',6,12,40,18000.00),
 (2,'Vóley mixto','Vóley','Laura Ceballos','Mar y Jue','19:00:00',13,60,24,20000.00),
 (3,'Básquet juvenil','Básquet','Martín Oviedo','Lun y Vie','19:30:00',12,17,20,19000.00),
 (4,'Natación','Natación','Sofía Bustos','Mar y Jue','17:00:00',5,70,30,26000.00),
 (5,'Patín artístico','Patín','Carla Peralta','Mié y Vie','17:30:00',5,15,25,21000.00),
 (6,'Ajedrez','Ajedrez','Raúl Giménez','Sáb','10:00:00',8,99,16,9000.00),
 (7,'Funcional adultos','Fitness','Paula Luna','Lun, Mié y Vie','20:30:00',18,65,30,22000.00),
 (8,'Yoga','Bienestar','Inés Arguello','Mar y Jue','09:00:00',16,99,15,17000.00);

INSERT INTO `socio`
 (`id`,`dni`,`nombre`,`apellido`,`fecha_nacimiento`,`email`,`telefono`,`estado_cuota`)
VALUES
 (1,'45123401','Lucas','Gómez','2012-05-14','lucas@example.com','3510000001','AL_DIA'),
 (2,'46123402','Martina','Pérez','2014-08-22','martina@example.com','3510000002','AL_DIA'),
 (3,'47123403','Tomás','Fernández','2016-03-10','tomas@example.com','3510000003','AL_DIA'),
 (4,'44123404','Valentina','Rodríguez','2010-11-05','valentina@example.com','3510000004','AL_DIA'),
 (5,'43123405','Santiago','López','2008-07-18','santiago@example.com','3510000005','AL_DIA'),
 (6,'42123406','Camila','Martínez','2005-02-27','camila@example.com','3510000006','AL_DIA'),
 (7,'41123407','Mateo','Sánchez','2002-09-12','mateo@example.com','3510000007','AL_DIA'),
 (8,'40123408','Sofía','Romero','1999-04-30','sofia@example.com','3510000008','AL_DIA'),
 (9,'39123409','Joaquín','Torres','1996-06-15','joaquin@example.com','3510000009','AL_DIA'),
 (10,'38123410','Lucía','Díaz','1993-12-08','lucia@example.com','3510000010','AL_DIA'),
 (11,'37123411','Nicolás','Castro','1990-01-20','nicolas@example.com','3510000011','AL_DIA'),
 (12,'36123412','Florencia','Ruiz','1985-10-03','florencia@example.com','3510000012','AL_DIA'),
 (13,'35123413','Agustín','Morales','1980-05-25','agustin@example.com','3510000013','AL_DIA'),
 (14,'34123414','Carolina','Herrera','1975-08-17','carolina@example.com','3510000014','MOROSO'),
 (15,'33123415','Federico','Vargas','1968-03-09','federico@example.com','3510000015','MOROSO');

-- Inscripciones preexistentes para demostrar cupo casi lleno.
-- Los socios MOROSOS conservan inscripciones preexistentes; RN4 bloquea nuevas altas.
INSERT INTO `inscripcion`
 (`id`,`socio_id`,`actividad_id`,`fecha_inscripcion`,`estado`)
VALUES
 (1,15,6,'2026-10-26','ACTIVA'),
 (2,14,6,'2026-10-26','ACTIVA'),
 (3,13,6,'2026-10-26','ACTIVA'),
 (4,12,6,'2026-10-26','ACTIVA'),
 (5,11,6,'2026-10-26','ACTIVA'),
 (6,10,6,'2026-10-26','ACTIVA'),
 (7,9,6,'2026-10-26','ACTIVA'),
 (8,8,6,'2026-10-26','ACTIVA'),
 (9,7,6,'2026-10-26','ACTIVA'),
 (10,6,6,'2026-10-26','ACTIVA'),
 (11,5,6,'2026-10-26','ACTIVA'),
 (12,4,6,'2026-10-26','ACTIVA'),
 (13,1,6,'2026-10-26','ACTIVA'),
 (14,2,6,'2026-10-26','ACTIVA'),
 (15,3,6,'2026-10-26','ACTIVA');

-- Consultas de verificacion (no modifican datos).
SELECT 'actividades' AS tipo, COUNT(*) AS cantidad FROM `actividad`
UNION ALL SELECT 'socios', COUNT(*) FROM `socio`
UNION ALL SELECT 'morosos', COUNT(*) FROM `socio` WHERE estado_cuota = 'MOROSO'
UNION ALL SELECT 'inscripciones_activas', COUNT(*) FROM `inscripcion` WHERE estado = 'ACTIVA';

SELECT a.nombre, a.cupo_maximo, COUNT(i.id) AS inscriptos_activos,
       a.cupo_maximo - COUNT(i.id) AS cupos_disponibles
FROM actividad a
LEFT JOIN inscripcion i ON i.actividad_id = a.id AND i.estado = 'ACTIVA'
WHERE a.id = 6
GROUP BY a.id, a.nombre, a.cupo_maximo;
