import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import { inscribirSocio, obtenerActividad } from '../servicios/api';
import { formatearHorario, formatearPesos } from '../utils/formato';
import '../estilos/Actividades.css';

// Ruta "/actividades/:id": detalle con el botón "Inscribirme".
function DetalleActividad() {
  const { id } = useParams();
  const { socio } = useSocio();
  const [actividad, setActividad] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [exito, setExito] = useState('');

  useEffect(() => {
    let vigente = true;
    setCargando(true);
    obtenerActividad(id)
      .then((datos) => vigente && setActividad(datos))
      .catch((e) => vigente && setError(e.message))
      .finally(() => vigente && setCargando(false));
    return () => {
      vigente = false;
    };
  }, [id]);

  const manejarInscripcion = async () => {
    setError('');
    setExito('');
    try {
      await inscribirSocio(socio.id, actividad.id);
      setExito(`¡Te inscribiste a ${actividad.nombre}!`);
      // Se vuelve a pedir la actividad para actualizar el cupo disponible.
      setActividad(await obtenerActividad(id));
    } catch (e) {
      setError(e.message);
    }
  };

  if (cargando) {
    return <p className="mensaje mensaje--info">Cargando actividad...</p>;
  }

  if (!actividad) {
    return (
      <section>
        <p className="mensaje mensaje--error" role="alert">
          {error || 'La actividad no existe'}
        </p>
        <Link to="/actividades">Volver al catálogo</Link>
      </section>
    );
  }

  const sinCupo = actividad.cupoDisponible <= 0;

  return (
    <section className="detalle">
      <h1>{actividad.nombre}</h1>
      <dl>
        <dt>Disciplina</dt>
        <dd>{actividad.disciplina}</dd>
        <dt>Días y horario</dt>
        <dd>
          {actividad.dias} · {formatearHorario(actividad.horario)} hs
        </dd>
        <dt>Edades</dt>
        <dd>
          {actividad.edadMinima} a {actividad.edadMaxima} años
        </dd>
        <dt>Profesor/a</dt>
        <dd>{actividad.profesor}</dd>
        <dt>Cuota mensual</dt>
        <dd>{formatearPesos(actividad.cuotaMensual)}</dd>
        <dt>Cupo</dt>
        <dd>
          {sinCupo ? (
            <span className="etiqueta etiqueta--sin-cupo">Sin cupo</span>
          ) : (
            `${actividad.cupoDisponible} lugares disponibles de ${actividad.cupoMaximo}`
          )}
        </dd>
      </dl>

      {error && (
        <p className="mensaje mensaje--error" role="alert">
          {error}
        </p>
      )}
      {exito && <p className="mensaje mensaje--exito">{exito}</p>}

      <div className="detalle__acciones">
        <button type="button" className="boton" disabled={sinCupo} onClick={manejarInscripcion}>
          Inscribirme
        </button>
        <Link to="/actividades" className="boton boton--secundario">
          Volver
        </Link>
      </div>
    </section>
  );
}

export default DetalleActividad;
