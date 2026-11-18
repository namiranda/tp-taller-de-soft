import { Navigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useSocio } from '../contexto/SocioContext';

// Si nadie ingresó con su DNI, se redirige a la pantalla de ingreso.
function RutaProtegida({ children }) {
  const { socio } = useSocio();
  return socio ? children : <Navigate to="/" replace />;
}

RutaProtegida.propTypes = {
  children: PropTypes.node.isRequired,
};

export default RutaProtegida;
