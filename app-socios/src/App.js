import { Routes, Route } from 'react-router-dom';
import Encabezado from './componentes/Encabezado';
import Pie from './componentes/Pie';
import RutaProtegida from './componentes/RutaProtegida';
import Ingreso from './paginas/Ingreso';
import ListaActividades from './componentes/ListaActividades';
import DetalleActividad from './componentes/DetalleActividad';
import MisActividades from './componentes/MisActividades';

function App() {
  return (
    <div className="app">
      <Encabezado />
      <main className="contenido">
        <Routes>
          <Route path="/" element={<Ingreso />} />
          <Route
            path="/actividades"
            element={
              <RutaProtegida>
                <ListaActividades />
              </RutaProtegida>
            }
          />
          <Route
            path="/actividades/:id"
            element={
              <RutaProtegida>
                <DetalleActividad />
              </RutaProtegida>
            }
          />
          <Route
            path="/mis-actividades"
            element={
              <RutaProtegida>
                <MisActividades />
              </RutaProtegida>
            }
          />
          <Route path="*" element={<p>La página que buscás no existe.</p>} />
        </Routes>
      </main>
      <Pie />
    </div>
  );
}

export default App;
