import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import { buscarSocioPorDni } from '../servicios/api';
import { esDniValido } from '../utils/esDniValido';
import '../estilos/Ingreso.css';

// Ruta "/": el socio escribe su DNI y la app lo busca en la API.
function Ingreso() {
  const [dni, setDni] = useState('');
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);
  const { ingresar } = useSocio();
  const navegar = useNavigate();

  const manejarEnvio = async (evento) => {
    evento.preventDefault();

    // RN7: se valida antes de llamar a la API.
    if (!esDniValido(dni)) {
      setError('El DNI debe tener 7 u 8 dígitos, sin puntos');
      return;
    }

    setError('');
    setCargando(true);
    try {
      const socio = await buscarSocioPorDni(dni);
      ingresar(socio);
      navegar('/actividades');
    } catch (e) {
      setError(e.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <section className="ingreso">
      <h1>Bienvenido a Mi Club</h1>
      <p>Ingresá con tu DNI para ver tus actividades y anotarte.</p>

      <form onSubmit={manejarEnvio} noValidate>
        <label htmlFor="dni">DNI</label>
        <input
          id="dni"
          type="text"
          inputMode="numeric"
          placeholder="Ej: 45123401"
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          aria-invalid={error ? 'true' : 'false'}
        />
        {error && (
          <p className="mensaje mensaje--error" role="alert">
            {error}
          </p>
        )}
        <button type="submit" className="boton" disabled={cargando}>
          {cargando ? 'Buscando...' : 'Ingresar'}
        </button>
      </form>
    </section>
  );
}

export default Ingreso;
