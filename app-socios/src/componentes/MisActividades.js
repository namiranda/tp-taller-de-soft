import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import { darDeBaja, obtenerInscripcionesDeSocio } from '../servicios/api';
import { formatearHorario, formatearPesos } from '../utils/formato';
import ResumenCuota from './ResumenCuota';
import '../estilos/MisActividades.css';

// Ruta "/mis-actividades": inscripciones activas, baja y cuota estimada.
function MisActividades() {
  const { socio } = useSocio();
  const [inscripciones, setInscripciones] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [exito, setExito] = useState('');

  useEffect(() => {
    let vigente = true;
    obtenerInscripcionesDeSocio(socio.id)
      .then((datos) => vigente && setInscripciones(datos))
      .catch((e) => vigente && setError(e.message))
      .finally(() => vigente && setCargando(false));
    return () => {
      vigente = false;
    };
  }, [socio.id]);

  const manejarBaja = async (inscripcion) => {
    setError('');
    setExito('');
    try {
      await darDeBaja(inscripcion.id);
      // La baja es lógica en el backend; acá la sacamos de la lista de activas.
      setInscripciones((actuales) => actuales.filter((i) => i.id !== inscripcion.id));
      setExito(`Te diste de baja de ${inscripcion.actividad.nombre}`);
    } catch (e) {
      setError(e.message);
    }
  };

  if (cargando) {
    return <p className="mensaje mensaje--info">Cargando tus actividades...</p>;
  }

  const actividades = inscripciones.map((i) => i.actividad);

  return (
    <section>
      <h1>Mis actividades</h1>

      {socio.estadoCuota === 'MOROSO' && (
        <p className="mensaje mensaje--error">
          Tenés la cuota impaga: podés darte de baja, pero para inscribirte a nuevas actividades
          debés regularizarla en secretaría.
        </p>
      )}
      {error && (
        <p className="mensaje mensaje--error" role="alert">
          {error}
        </p>
      )}
      {exito && <p className="mensaje mensaje--exito">{exito}</p>}

      {inscripciones.length === 0 ? (
        <p className="mensaje mensaje--info">
          Todavía no estás inscripto a ninguna actividad.{' '}
          <Link to="/actividades">Ver actividades</Link>
        </p>
      ) : (
        <ul className="mis-actividades">
          {inscripciones.map((inscripcion) => (
            <li key={inscripcion.id} className="mis-actividades__item">
              <div>
                <strong>{inscripcion.actividad.nombre}</strong>
                <br />
                {inscripcion.actividad.dias} · {formatearHorario(inscripcion.actividad.horario)} hs
                · {formatearPesos(inscripcion.actividad.cuotaMensual)}
              </div>
              <button
                type="button"
                className="boton boton--peligro"
                onClick={() => manejarBaja(inscripcion)}
              >
                Darme de baja
              </button>
            </li>
          ))}
        </ul>
      )}

      <ResumenCuota actividades={actividades} />
    </section>
  );
}

export default MisActividades;
