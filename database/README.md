# Base de datos - SocioClub

Base de datos MySQL 8.4 del TP Integrador SocioClub.

## Estructura
- socio: información de los socios.
- actividad: actividades deportivas disponibles.
- inscripcion: inscripciones de los socios a las actividades.

## Datos iniciales
El script `socioclub_completo.sql` crea la estructura y carga 15 socios (2 morosos),
8 actividades y 15 inscripciones activas. La aplicación también incluye el mismo
conjunto inicial en `backend/SocioClub/src/main/resources/data.sql`; lo carga al
arrancar y no duplica filas ya existentes.

## Configuración
La aplicación se conecta a MySQL usando `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.
Si no se definen, usa `localhost:3306/socioclub` con el usuario `root` y contraseña
vacía. La base configurada debe existir y el usuario debe poder crear tablas.

El archivo `socioclub_completo.sql` permite crear la estructura y cargar los datos
iniciales manualmente en una base vacía. No ejecutar en una base ya poblada: sus
INSERT usan IDs fijos.
