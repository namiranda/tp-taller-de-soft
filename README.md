# SocioClub

Trabajo Práctico Integrador de Taller de Construcción de Software (Universidad Siglo 21).
Sistema web de gestión de socios, actividades e inscripciones del Club Social y Deportivo
Los Algarrobos (Córdoba).

## Integrantes

Grupo N.º: 1

Integrantes

- Santiago Villalba Palmieri 
- Santiago Basualdo
- Noelia Miranda
- Mateo Nicolás Rojas 
- Agustina Colombero 

## Estructura del repositorio

| Carpeta | Contenido | Tecnología |
|---|---|---|
| `sitio-publico/` | A. Sitio público del club (catálogo, filtros, preinscripción) | HTML5, CSS3, JavaScript |
| `backend/SocioClub/` | B. Sistema de secretaría (Thymeleaf) y C. API REST | Spring Boot, Thymeleaf, JPA, MySQL |
| `app-socios/` | D. App "Mi Club" para socios | React, React Router, Axios |
| `database/` | Script de creación y datos iniciales | MySQL |
| `PRUEBAS.md` | E. Pruebas: Jest, Postman y prueba de sistema | |

Los tres frentes comparten una sola base MySQL y un solo backend: las reglas de negocio
(RN1 a RN5) están en la capa de servicio y se aplican igual desde la secretaría y desde la app.

## Requisitos

- JDK 17 o superior
- MySQL 8.x (solo si se usa una base local)
- Node.js 18 o superior y npm

## 1. Base de datos

La aplicación se conecta por defecto a la **base compartida del equipo en Aiven**
(`socioclub-socioclub.a.aivencloud.com:24898`, usuario `equipo_socioclub`). Solo falta
definir la contraseña en la variable de entorno `DB_PASSWORD` (no se guarda en el repositorio).

Para usar una **base local** en su lugar:

1. Crearla y cargar los datos iniciales (en una base nueva y vacía, porque los `INSERT`
   usan IDs fijos):

   ```bash
   mysql -u root -p < database/socioclub_completo.sql
   ```

2. Apuntar el backend a ella con variables de entorno antes de iniciarlo
   (PowerShell):

   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/socioclub?sslMode=DISABLED&serverTimezone=UTC"
   $env:SPRING_DATASOURCE_USERNAME = "root"
   $env:SPRING_DATASOURCE_PASSWORD = "<contraseña>"
   ```

El script carga 8 actividades, 15 socios (2 morosos) y 15 inscripciones activas. Ajedrez
queda con 1 solo lugar de 16, para poder demostrar la regla de cupo.

## 2. Backend (Spring Boot)

```powershell
$env:DB_PASSWORD = "<contraseña de la base>"
cd backend\SocioClub
.\mvnw.cmd spring-boot:run
```

Queda en http://localhost:8080:

- Sistema de secretaría (Thymeleaf): http://localhost:8080/actividades
- API REST: http://localhost:8080/api (ver [`backend/SocioClub/API.md`](backend/SocioClub/API.md))

Tests del backend: `.\mvnw.cmd test` (usan una base H2 en memoria, no tocan la base real).

## 3. App React "Mi Club"

Con el backend levantado (la API permite CORS para `localhost:3000`):

```bash
cd app-socios
npm install
npm start
```

Se abre en http://localhost:3000. La URL de la API se configura en `app-socios/.env`
(`REACT_APP_API_URL`, por defecto `http://localhost:8080/api`).

Tests de la app: `npm test -- --watchAll=false`.

## 4. Sitio público

Es HTML estático: abrir `sitio-publico/index.html` en el navegador (o usar una extensión
tipo Live Server).

## Usuarios y DNI de prueba

El ingreso a la app de socios es solo con DNI.

| DNI | Socio | Para probar |
|---|---|---|
| `39123409` | Joaquín Torres (adulto, al día) | Inscripción exitosa, RN2 (ya está en Ajedrez) |
| `45123401` | Lucas Gómez (14 años, al día) | RN3: Funcional adultos admite de 18 a 65 años |
| `44123404` | Valentina Rodríguez (al día) | RN5: máximo de 3 actividades |
| `34123414` | Carolina Herrera (**moroso**) | RN4: debe regularizar su cuota |
| `33123415` | Federico Vargas (**moroso**) | RN4 |

Para RN1 (sin cupo): Ajedrez tiene 16 de cupo y 15 inscriptos; al completar el último lugar,
la tarjeta pasa a "Sin cupo" y el botón se deshabilita.

## Pruebas

Ver [`PRUEBAS.md`](PRUEBAS.md): pruebas unitarias y de componente (Jest y React Testing
Library), colección de Postman (`backend/SocioClub/postman/`) y tabla de prueba de sistema.

> Importante: la colección de Postman y varios casos de prueba modifican datos. No ejecutarlos
> sobre la base compartida sin restaurar los cambios.
