// Toda la comunicación con el backend está centralizada en este archivo (Axios).
// Endpoints: Anexo A del enunciado (Parte C).
//
// Mientras la API no esté lista se puede trabajar con datos locales:
//   REACT_APP_USAR_DATOS_LOCALES=true  (ver .env)
//
// Contrato JSON real del backend (rama Parte-B-y-C, ver backend/SocioClub/API.md):
//   Actividad:   { id, nombre, disciplina, profesor, dias, horario, edadMinima,
//                  edadMaxima, cupoMaximo, cuotaMensual, cupoDisponible }
//   Socio:       { id, dni, nombre, apellido, fechaNacimiento, estadoCuota, ... }
//   Inscripcion: { id, socioId, socio, actividadId, actividad (nombre), fechaInscripcion, estado }
//   Error 404/409: { mensaje: "mensaje de la regla" }
// Como la inscripcion viene "plana", la combinamos con la lista de actividades
// para que los componentes reciban { id, estado, actividad: { ...Actividad } }.
import axios from 'axios';
import * as apiLocal from './apiLocal';

const USAR_DATOS_LOCALES = process.env.REACT_APP_USAR_DATOS_LOCALES === 'true';

const cliente = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

// Convierte cualquier error de Axios en un Error con un mensaje listo para
// mostrar al socio (por ejemplo el mensaje de la regla en un 409).
function traducirError(error) {
  if (error.response) {
    const { status, data } = error.response;
    const mensaje =
      (data && (data.mensaje || data.error)) ||
      (status === 404 ? 'No se encontró lo que buscabas' : 'Ocurrió un error en el servidor');
    const traducido = new Error(mensaje);
    traducido.status = status;
    traducido.regla = data && data.regla;
    return traducido;
  }
  return new Error('No se pudo conectar con el servidor. Intentá de nuevo en unos minutos.');
}

async function pedir(promesa) {
  try {
    const respuesta = await promesa;
    return respuesta.data;
  } catch (error) {
    throw traducirError(error);
  }
}

// GET /api/socios/dni/{dni}
export function buscarSocioPorDni(dni) {
  if (USAR_DATOS_LOCALES) return apiLocal.buscarSocioPorDni(dni);
  return pedir(cliente.get(`/socios/dni/${dni}`));
}

// GET /api/actividades?disciplina=Fútbol
export function obtenerActividades(disciplina) {
  if (USAR_DATOS_LOCALES) return apiLocal.obtenerActividades(disciplina);
  return pedir(cliente.get('/actividades', { params: disciplina ? { disciplina } : {} }));
}

// GET /api/actividades/{id}
export function obtenerActividad(id) {
  if (USAR_DATOS_LOCALES) return apiLocal.obtenerActividad(id);
  return pedir(cliente.get(`/actividades/${id}`));
}

async function pedirInscripcionesConActividad(socioId) {
  const [inscripciones, actividades] = await Promise.all([
    pedir(cliente.get(`/socios/${socioId}/inscripciones`)),
    pedir(cliente.get('/actividades')),
  ]);
  return inscripciones.map((inscripcion) => ({
    id: inscripcion.id,
    fechaInscripcion: inscripcion.fechaInscripcion,
    estado: inscripcion.estado,
    actividad: actividades.find((a) => a.id === inscripcion.actividadId),
  }));
}

// GET /api/socios/{id}/inscripciones  (solo ACTIVAS)
export function obtenerInscripcionesDeSocio(socioId) {
  if (USAR_DATOS_LOCALES) return apiLocal.obtenerInscripcionesDeSocio(socioId);
  return pedirInscripcionesConActividad(socioId);
}

// POST /api/inscripciones  { socioId, actividadId }  -> 201 / 404 / 409
export function inscribirSocio(socioId, actividadId) {
  if (USAR_DATOS_LOCALES) return apiLocal.inscribirSocio(socioId, actividadId);
  return pedir(cliente.post('/inscripciones', { socioId, actividadId }));
}

// PUT /api/inscripciones/{id}/baja  (baja lógica)
export function darDeBaja(inscripcionId) {
  if (USAR_DATOS_LOCALES) return apiLocal.darDeBaja(inscripcionId);
  return pedir(cliente.put(`/inscripciones/${inscripcionId}/baja`));
}
