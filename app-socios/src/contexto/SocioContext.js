import { createContext, useContext, useState } from 'react';
import PropTypes from 'prop-types';

// El socio que ingresó se comparte entre todas las pantallas con un contexto,
// en lugar de pasarlo por props de componente en componente.
const SocioContext = createContext(null);

export function SocioProvider({ children }) {
  const [socio, setSocio] = useState(null);

  const ingresar = (socioEncontrado) => setSocio(socioEncontrado);
  const salir = () => setSocio(null);

  return (
    <SocioContext.Provider value={{ socio, ingresar, salir }}>
      {children}
    </SocioContext.Provider>
  );
}

SocioProvider.propTypes = {
  children: PropTypes.node.isRequired,
};

export function useSocio() {
  const contexto = useContext(SocioContext);
  if (contexto === null) {
    throw new Error('useSocio debe usarse dentro de un SocioProvider');
  }
  return contexto;
}
