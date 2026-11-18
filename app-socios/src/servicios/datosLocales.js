// Datos locales (un arreglo en el código) que replican los datos iniciales de la
// sección 3.3 del enunciado y de database/socioclub_completo.sql.
// Se usan mientras la API REST no esté lista (REACT_APP_USAR_DATOS_LOCALES=true).

export const actividadesIniciales = [
  { id: 1, nombre: 'Fútbol infantil', disciplina: 'Fútbol', profesor: 'Diego Ferreyra', dias: 'Lun y Mié', horario: '18:00:00', edadMinima: 6, edadMaxima: 12, cupoMaximo: 40, cuotaMensual: 18000 },
  { id: 2, nombre: 'Vóley mixto', disciplina: 'Vóley', profesor: 'Laura Ceballos', dias: 'Mar y Jue', horario: '19:00:00', edadMinima: 13, edadMaxima: 60, cupoMaximo: 24, cuotaMensual: 20000 },
  { id: 3, nombre: 'Básquet juvenil', disciplina: 'Básquet', profesor: 'Martín Oviedo', dias: 'Lun y Vie', horario: '19:30:00', edadMinima: 12, edadMaxima: 17, cupoMaximo: 20, cuotaMensual: 19000 },
  { id: 4, nombre: 'Natación', disciplina: 'Natación', profesor: 'Sofía Bustos', dias: 'Mar y Jue', horario: '17:00:00', edadMinima: 5, edadMaxima: 70, cupoMaximo: 30, cuotaMensual: 26000 },
  { id: 5, nombre: 'Patín artístico', disciplina: 'Patín', profesor: 'Carla Peralta', dias: 'Mié y Vie', horario: '17:30:00', edadMinima: 5, edadMaxima: 15, cupoMaximo: 25, cuotaMensual: 21000 },
  { id: 6, nombre: 'Ajedrez', disciplina: 'Ajedrez', profesor: 'Raúl Giménez', dias: 'Sáb', horario: '10:00:00', edadMinima: 8, edadMaxima: 99, cupoMaximo: 16, cuotaMensual: 9000 },
  { id: 7, nombre: 'Funcional adultos', disciplina: 'Fitness', profesor: 'Paula Luna', dias: 'Lun, Mié y Vie', horario: '20:30:00', edadMinima: 18, edadMaxima: 65, cupoMaximo: 30, cuotaMensual: 22000 },
  { id: 8, nombre: 'Yoga', disciplina: 'Bienestar', profesor: 'Inés Arguello', dias: 'Mar y Jue', horario: '09:00:00', edadMinima: 16, edadMaxima: 99, cupoMaximo: 15, cuotaMensual: 17000 },
];

export const sociosIniciales = [
  { id: 1, dni: '45123401', nombre: 'Lucas', apellido: 'Gómez', fechaNacimiento: '2012-05-14', estadoCuota: 'AL_DIA' },
  { id: 2, dni: '46123402', nombre: 'Martina', apellido: 'Pérez', fechaNacimiento: '2014-08-22', estadoCuota: 'AL_DIA' },
  { id: 3, dni: '47123403', nombre: 'Tomás', apellido: 'Fernández', fechaNacimiento: '2016-03-10', estadoCuota: 'AL_DIA' },
  { id: 4, dni: '44123404', nombre: 'Valentina', apellido: 'Rodríguez', fechaNacimiento: '2010-11-05', estadoCuota: 'AL_DIA' },
  { id: 5, dni: '43123405', nombre: 'Santiago', apellido: 'López', fechaNacimiento: '2008-07-18', estadoCuota: 'AL_DIA' },
  { id: 6, dni: '42123406', nombre: 'Camila', apellido: 'Martínez', fechaNacimiento: '2005-02-27', estadoCuota: 'AL_DIA' },
  { id: 7, dni: '41123407', nombre: 'Mateo', apellido: 'Sánchez', fechaNacimiento: '2002-09-12', estadoCuota: 'AL_DIA' },
  { id: 8, dni: '40123408', nombre: 'Sofía', apellido: 'Romero', fechaNacimiento: '1999-04-30', estadoCuota: 'AL_DIA' },
  { id: 9, dni: '39123409', nombre: 'Joaquín', apellido: 'Torres', fechaNacimiento: '1996-06-15', estadoCuota: 'AL_DIA' },
  { id: 10, dni: '38123410', nombre: 'Lucía', apellido: 'Díaz', fechaNacimiento: '1993-12-08', estadoCuota: 'AL_DIA' },
  { id: 11, dni: '37123411', nombre: 'Nicolás', apellido: 'Castro', fechaNacimiento: '1990-01-20', estadoCuota: 'AL_DIA' },
  { id: 12, dni: '36123412', nombre: 'Florencia', apellido: 'Ruiz', fechaNacimiento: '1985-10-03', estadoCuota: 'AL_DIA' },
  { id: 13, dni: '35123413', nombre: 'Agustín', apellido: 'Morales', fechaNacimiento: '1980-05-25', estadoCuota: 'AL_DIA' },
  { id: 14, dni: '34123414', nombre: 'Carolina', apellido: 'Herrera', fechaNacimiento: '1975-08-17', estadoCuota: 'MOROSO' },
  { id: 15, dni: '33123415', nombre: 'Federico', apellido: 'Vargas', fechaNacimiento: '1968-03-09', estadoCuota: 'MOROSO' },
];

// Los 15 socios están inscriptos en Ajedrez (cupo 16): queda 1 lugar.
export const inscripcionesIniciales = sociosIniciales.map((socio, indice) => ({
  id: indice + 1,
  socioId: socio.id,
  actividadId: 6,
  fechaInscripcion: '2026-10-26',
  estado: 'ACTIVA',
}));
