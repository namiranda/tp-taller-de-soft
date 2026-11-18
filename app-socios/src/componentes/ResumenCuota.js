import PropTypes from 'prop-types';
import { calcularCuotaMensual } from '../utils/calcularCuotaMensual';
import { formatearPesos } from '../utils/formato';
import '../estilos/MisActividades.css';

// RN6: muestra la cuota mensual estimada con la bonificación por cantidad.
function ResumenCuota({ actividades }) {
  const subtotal = actividades.reduce((suma, a) => suma + a.cuotaMensual, 0);
  const total = calcularCuotaMensual(actividades);
  const descuento = subtotal - total;

  return (
    <aside className="resumen-cuota">
      <h2>Cuota mensual estimada</h2>
      <p>
        Actividades: {actividades.length} · Subtotal: {formatearPesos(subtotal)}
      </p>
      {descuento > 0 && <p>Bonificación: -{formatearPesos(descuento)}</p>}
      <p className="resumen-cuota__total">Total: {formatearPesos(total)}</p>
    </aside>
  );
}

ResumenCuota.propTypes = {
  actividades: PropTypes.arrayOf(
    PropTypes.shape({
      cuotaMensual: PropTypes.number.isRequired,
    })
  ).isRequired,
};

export default ResumenCuota;
