// Implementación en memoria de las mismas funciones que expone api.js.
// Aplica las reglas RN1 a RN5 para poder demostrarlas sin backend.
import { actividadesIniciales, sociosIniciales, inscripcionesIniciales } from './datosLocales';

const MAX_ACTIVIDADES = 3;

const actividades = actividadesIniciales.map((a) => ({ ...a }));
const socios = sociosIniciales.map((s) => ({ ...s }));
const inscripciones = inscripcionesIniciales.map((i) => ({ ...i }));
let proximoIdInscripcion = inscripciones.length + 1;

function respuesta(valor) {
  return new Promise((resolver) => setTimeout(() => resolver(valor), 150));
}

function rechazo(mensaje, regla) {
  const error = new Error(mensaje);
  error.regla = regla;
  return Promise.reject(error);
}

function calcularEdad(fechaNacimiento) {
  const hoy = new Date();
  const nacimiento = new Date(fechaNacimiento);
  let edad = hoy.getFullYear() - nacimiento.getFullYear();
  const yaCumplio =
    hoy.getMonth() > nacimiento.getMonth() ||
    (hoy.getMonth() === nacimiento.getMonth() && hoy.getDate() >= nacimiento.getDate());
  if (!yaCumplio) edad -= 1;
  return edad;
}

function activasDe(actividadId) {
  return inscripciones.filter((i) => i.actividadId === actividadId && i.estado === 'ACTIVA');
}

function conCupo(actividad) {
  return { ...actividad, cupoDisponible: actividad.cupoMaximo - activasDe(actividad.id).length };
}

function conActividad(inscripcion) {
  const actividad = actividades.find((a) => a.id === inscripcion.actividadId);
  return {
    id: inscripcion.id,
    fechaInscripcion: inscripcion.fechaInscripcion,
    estado: inscripcion.estado,
    actividad: conCupo(actividad),
  };
}

export function buscarSocioPorDni(dni) {
  const socio = socios.find((s) => s.dni === dni);
  return socio ? respuesta({ ...socio }) : rechazo('No encontramos un socio con ese DNI', null);
}

export function obtenerActividades(disciplina) {
  const lista = actividades
    .filter((a) => !disciplina || a.disciplina === disciplina)
    .map(conCupo);
  return respuesta(lista);
}

export function obtenerActividad(id) {
  const actividad = actividades.find((a) => a.id === Number(id));
  return actividad ? respuesta(conCupo(actividad)) : rechazo('La actividad no existe', null);
}

export function obtenerInscripcionesDeSocio(socioId) {
  const lista = inscripciones
    .filter((i) => i.socioId === socioId && i.estado === 'ACTIVA')
    .map(conActividad);
  return respuesta(lista);
}

export function inscribirSocio(socioId, actividadId) {
  const socio = socios.find((s) => s.id === socioId);
  const actividad = actividades.find((a) => a.id === actividadId);
  if (!socio || !actividad) return rechazo('Socio o actividad inexistente', null);

  if (activasDe(actividadId).length >= actividad.cupoMaximo) {
    return rechazo('La actividad no tiene cupo disponible', 'RN1');
  }
  if (activasDe(actividadId).some((i) => i.socioId === socioId)) {
    return rechazo('El socio ya está inscripto en esta actividad', 'RN2');
  }
  const edad = calcularEdad(socio.fechaNacimiento);
  if (edad < actividad.edadMinima || edad > actividad.edadMaxima) {
    return rechazo(
      `La edad permitida es de ${actividad.edadMinima} a ${actividad.edadMaxima} años`,
      'RN3'
    );
  }
  if (socio.estadoCuota === 'MOROSO') {
    return rechazo('Debe regularizar su cuota en secretaría', 'RN4');
  }
  const activasDelSocio = inscripciones.filter((i) => i.socioId === socioId && i.estado === 'ACTIVA');
  if (activasDelSocio.length >= MAX_ACTIVIDADES) {
    return rechazo('Alcanzó el máximo de 3 actividades', 'RN5');
  }

  const nueva = {
    id: proximoIdInscripcion++,
    socioId,
    actividadId,
    fechaInscripcion: new Date().toISOString().slice(0, 10),
    estado: 'ACTIVA',
  };
  inscripciones.push(nueva);
  return respuesta(conActividad(nueva));
}

export function darDeBaja(inscripcionId) {
  const inscripcion = inscripciones.find((i) => i.id === inscripcionId);
  if (!inscripcion) return rechazo('La inscripción no existe', null);
  inscripcion.estado = 'BAJA';
  return respuesta(conActividad(inscripcion));
}
