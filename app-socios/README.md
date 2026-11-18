# App "Mi Club" (Parte D)

App web para socios, hecha con React (create-react-app), React Router y Axios.

## Levantar la app

```bash
cd app-socios
npm install
npm start
```

Se abre en http://localhost:3000.

## Datos locales o API

En `.env`:

- `REACT_APP_USAR_DATOS_LOCALES=true`: usa datos en memoria (`src/servicios/apiLocal.js`), sin backend. Aplica RN1 a RN5.
- `REACT_APP_USAR_DATOS_LOCALES=false`: usa la API REST (`REACT_APP_API_URL`, por defecto `http://localhost:8080/api`) con Axios. Requiere la Parte C y CORS para `localhost:3000`.

Reiniciar `npm start` después de cambiar `.env`.

## DNI de prueba

- Socio al día: `45123401` (Lucas, 14 años), `39123409` (adulto)
- Socios morosos (RN4): `34123414`, `33123415`
- Ajedrez tiene 1 solo lugar (RN1)

## Estructura

- `src/servicios/api.js`: toda la comunicación con el backend (Axios)
- `src/contexto/SocioContext.js`: socio logueado compartido con `useContext`
- `src/componentes/`: Encabezado, Pie, TarjetaActividad, ListaActividades, DetalleActividad, MisActividades, ResumenCuota
- `src/utils/`: `calcularCuotaMensual` (RN6) y `esDniValido` (RN7)
