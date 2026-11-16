# API REST - SocioClub (Anexo A)

La API JSON se ejecuta en el mismo servidor Spring Boot y permite CORS para
`http://localhost:3000`.

| Verbo | Ruta | Respuesta |
|---|---|---|
| GET | `/api/actividades[?disciplina=Fútbol]` | 200; incluye `cupoDisponible` |
| GET | `/api/actividades/{id}` | 200 / 404; incluye `cupoDisponible` |
| POST | `/api/actividades` | 201 |
| PUT | `/api/actividades/{id}` | 200 / 404 |
| DELETE | `/api/actividades/{id}` | 204 / 404 / 409 si tiene inscripciones activas |
| GET | `/api/socios/dni/{dni}` | 200 / 404 |
| GET | `/api/socios/{id}/inscripciones` | 200 (solo activas) / 404 |
| POST | `/api/inscripciones` | 201 / 404 / 409 |
| PUT | `/api/inscripciones/{id}/baja` | 200 / 404; cambia el estado a `BAJA` |

Los conflictos de reglas de negocio responden `409 Conflict` con un objeto JSON
`{"mensaje":"..."}`. Los recursos inexistentes responden `404` con el mismo formato.
Las respuestas de inscripciones incluyen IDs y nombres relacionados, no entidades
JPA, para evitar ciclos en JSON.

## Configuración

Define `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` para conectarse a MySQL. Los valores
por defecto apuntan a `localhost:3306/socioclub`, usuario `root` y contraseña vacía.

## Postman

Importa `postman/SocioClub.postman_collection.json` y ejecuta la colección completa
con Runner. Las pruebas de reglas usan el dataset inicial de `database/socioclub_completo.sql`
(Socios 1, 4, 10 y 14; actividades 2, 3, 4, 6, 7 y 8). Las pruebas de cupo, máximo
de actividades y alta de ejemplo tienen pasos de limpieza/restauración incluidos;
no interrumpas el Runner entre esos pasos.
