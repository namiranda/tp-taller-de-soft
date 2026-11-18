# App "Mi Club" (Parte D)

App web para socios, hecha con React (create-react-app), React Router y Axios.

## Levantar la app

```bash
cd app-socios
npm install
npm start
```

Se abre en http://localhost:3000.

## Conexión con el backend

La app consume la API REST con Axios (`REACT_APP_API_URL` en `.env`, por defecto `http://localhost:8080/api`). El backend debe estar levantado y permitir CORS para `localhost:3000`.

## DNI de prueba

- Socio al día: `45123401` (Lucas, 14 años), `39123409` (adulto)
- Socios morosos (RN4): `34123414`, `33123415`
- Ajedrez tiene 1 solo lugar (RN1)

## Estructura

- `src/servicios/api.js`: toda la comunicación con el backend (Axios)
- `src/contexto/SocioContext.js`: socio logueado compartido con `useContext`
- `src/componentes/`: Encabezado, Pie, TarjetaActividad, ListaActividades, DetalleActividad, MisActividades, ResumenCuota
- `src/utils/`: `calcularCuotaMensual` (RN6) y `esDniValido` (RN7)
