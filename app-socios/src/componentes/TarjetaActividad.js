import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { formatearHorario, formatearPesos } from '../utils/formato';
import '../estilos/Actividades.css';

function TarjetaActividad({ actividad, onInscribirme = () => {} }) {
  const sinCupo = actividad.cupoDisponible <= 0;

  // Clase dinámica: la tarjeta se ve en gris cuando no hay lugar.
  const clases = sinCupo ? 'tarjeta tarjeta--sin-cupo' : 'tarjeta';

  return (
    <article className={clases}>
      <h2>{actividad.nombre}</h2>
      <p className="tarjeta__disciplina">{actividad.disciplina}</p>
      <ul className="tarjeta__datos">
        <li>
          {actividad.dias} · {formatearHorario(actividad.horario)} hs
        </li>
        <li>
          Edades: {actividad.edadMinima} a {actividad.edadMaxima} años
        </li>
        <li>Profesor/a: {actividad.profesor}</li>
        <li>Cuota: {formatearPesos(actividad.cuotaMensual)}</li>
      </ul>

      {sinCupo ? (
        <p className="etiqueta etiqueta--sin-cupo">Sin cupo</p>
      ) : (
        <p className="etiqueta">
          Cupo disponible: {actividad.cupoDisponible} de {actividad.cupoMaximo}
        </p>
      )}

      <div className="tarjeta__acciones">
        <Link to={`/actividades/${actividad.id}`} className="boton boton--secundario">
          Ver detalle
        </Link>
        <button
          type="button"
          className="boton"
          disabled={sinCupo}
          onClick={() => onInscribirme(actividad)}
        >
          Inscribirme
        </button>
      </div>
    </article>
  );
}

TarjetaActividad.propTypes = {
  actividad: PropTypes.shape({
    id: PropTypes.number.isRequired,
    nombre: PropTypes.string.isRequired,
    disciplina: PropTypes.string.isRequired,
    profesor: PropTypes.string.isRequired,
    dias: PropTypes.string.isRequired,
    horario: PropTypes.string.isRequired,
    edadMinima: PropTypes.number.isRequired,
    edadMaxima: PropTypes.number.isRequired,
    cupoMaximo: PropTypes.number.isRequired,
    cuotaMensual: PropTypes.number.isRequired,
    cupoDisponible: PropTypes.number.isRequired,
  }).isRequired,
  onInscribirme: PropTypes.func,
};


export default TarjetaActividad;
