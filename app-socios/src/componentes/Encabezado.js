import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import '../estilos/Encabezado.css';

function Encabezado() {
  const { socio, salir } = useSocio();
  const navegar = useNavigate();

  const manejarSalir = () => {
    salir();
    navegar('/');
  };

  return (
    <header className="encabezado">
      <Link to={socio ? '/actividades' : '/'} className="encabezado__marca">
        SocioClub<span>Mi Club</span>
      </Link>

      {socio && (
        <nav className="encabezado__nav" aria-label="Navegación principal">
          <NavLink to="/actividades">Actividades</NavLink>
          <NavLink to="/mis-actividades">Mis actividades</NavLink>
          <span className="encabezado__saludo">Hola, {socio.nombre}</span>
          <button type="button" className="boton boton--secundario" onClick={manejarSalir}>
            Salir
          </button>
        </nav>
      )}
    </header>
  );
}

export default Encabezado;
