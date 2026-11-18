import { useEffect, useState } from 'react';
import { useSocio } from '../contexto/SocioContext';
import { inscribirSocio, obtenerActividades } from '../servicios/api';
import TarjetaActividad from './TarjetaActividad';
import '../estilos/Actividades.css';

// Ruta "/actividades": catálogo con filtro por disciplina y cupo disponible.
function ListaActividades() {
  const { socio } = useSocio();
  const [actividades, setActividades] = useState([]);
  const [disciplinas, setDisciplinas] = useState([]);
  const [disciplina, setDisciplina] = useState('');
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [exito, setExito] = useState('');
  const [recarga, setRecarga] = useState(0);

  // Se vuelve a pedir la lista cuando cambia el filtro o después de inscribirse.
  useEffect(() => {
    let vigente = true;
    setCargando(true);
    obtenerActividades(disciplina)
      .then((datos) => {
        if (!vigente) return;
        setActividades(datos);
        // La lista sin filtrar define las opciones del selector.
        if (disciplina === '') {
          setDisciplinas([...new Set(datos.map((a) => a.disciplina))]);
        }
      })
      .catch((e) => vigente && setError(e.message))
      .finally(() => vigente && setCargando(false));
    return () => {
      vigente = false;
    };
  }, [disciplina, recarga]);

  const manejarInscripcion = async (actividad) => {
    setError('');
    setExito('');
    try {
      await inscribirSocio(socio.id, actividad.id);
      setExito(`¡Te inscribiste a ${actividad.nombre}!`);
      setRecarga((n) => n + 1);
    } catch (e) {
      // Mensaje de la regla de negocio que rechazó la inscripción (409).
      setError(e.message);
    }
  };

  return (
    <section>
      <h1>Actividades del club</h1>

      <div className="filtro">
        <label htmlFor="disciplina">Disciplina</label>
        <select
          id="disciplina"
          value={disciplina}
          onChange={(e) => setDisciplina(e.target.value)}
        >
          <option value="">Todas</option>
          {disciplinas.map((d) => (
            <option key={d} value={d}>
              {d}
            </option>
          ))}
        </select>
      </div>

      {error && (
        <p className="mensaje mensaje--error" role="alert">
          {error}
        </p>
      )}
      {exito && <p className="mensaje mensaje--exito">{exito}</p>}

      {cargando ? (
        <p className="mensaje mensaje--info">Cargando actividades...</p>
      ) : actividades.length === 0 ? (
        <p className="mensaje mensaje--info">No hay actividades para esa disciplina.</p>
      ) : (
        <div className="grilla">
          {actividades.map((actividad) => (
            <TarjetaActividad
              key={actividad.id}
              actividad={actividad}
              onInscribirme={manejarInscripcion}
            />
          ))}
        </div>
      )}
    </section>
  );
}

export default ListaActividades;
