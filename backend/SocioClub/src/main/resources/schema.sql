CREATE TABLE IF NOT EXISTS socio (
  id BIGINT NOT NULL AUTO_INCREMENT,
  dni VARCHAR(8) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  email VARCHAR(150) DEFAULT NULL,
  telefono VARCHAR(30) DEFAULT NULL,
  estado_cuota ENUM('AL_DIA','MOROSO') NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_socio_dni (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS actividad (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  disciplina VARCHAR(100) NOT NULL,
  profesor VARCHAR(100) NOT NULL,
  dias VARCHAR(100) NOT NULL,
  horario TIME NOT NULL,
  edad_minima INT NOT NULL,
  edad_maxima INT NOT NULL,
  cupo_maximo INT NOT NULL,
  cuota_mensual DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS inscripcion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  actividad_id BIGINT NOT NULL,
  fecha_inscripcion DATE NOT NULL,
  estado ENUM('ACTIVA','BAJA') NOT NULL DEFAULT 'ACTIVA',
  PRIMARY KEY (id),
  KEY fk_inscripcion_socio (socio_id),
  KEY fk_inscripcion_actividad (actividad_id),
  CONSTRAINT fk_inscripcion_socio FOREIGN KEY (socio_id) REFERENCES socio (id),
  CONSTRAINT fk_inscripcion_actividad FOREIGN KEY (actividad_id) REFERENCES actividad (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
