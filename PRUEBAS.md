# Parte E — Pruebas

## Pruebas unitarias y de componentes

En `app-socios`, ejecutar `npm install` y luego `npm test -- --watchAll=false`. Las pruebas cubren RN6, RN7 y `TarjetaActividad`.

## Pruebas de API

Con el backend iniciado y una base local de prueba cargada con `database/socioclub_completo.sql`, importar `backend/SocioClub/postman/SocioClub.postman_collection.json` en Postman y ejecutar toda la colección en orden desde Runner. Para apuntar el backend a la base local, configurar `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` antes de iniciarlo. La variable `baseUrl` de Postman apunta por defecto a `http://localhost:8080`. Registrar en Runner las pruebas aprobadas y fallidas. La colección modifica datos y realiza limpiezas: no ejecutarla sobre una base compartida ni interrumpirla entre los pasos de preparación y restauración.

Resultado obtenido: ejecución en Postman Runner el 18/11/2026: **40 pruebas aprobadas, 0 fallidas y 0 omitidas**.

## Prueba de sistema

Preparación: usar una base local de prueba con los datos iniciales de `database/socioclub_completo.sql`; iniciar backend y app React. Ejecutar cada caso con los datos iniciales restaurados o revertir los cambios del caso anterior. Los DNI y actividades corresponden a ese conjunto de datos. Anotar el resultado observado después de probar la interfaz completa.

| Caso | Pasos | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| Inscripción exitosa | Ingresar con DNI `39123409`; buscar Vóley mixto; seleccionar «Inscribirme»; abrir «Mis actividades». | Se confirma la inscripción y Vóley aparece entre las actividades activas. | Aprobado: el usuario confirmó el mensaje de inscripción y que Vóley apareció en «Mis actividades»; luego lo dio de baja. |
| RN1: sin cupo | Desde la administración, editar Ajedrez y fijar su cupo máximo en 15, igual a sus inscripciones activas. Ingresar con DNI `39123409` y buscar Ajedrez. Restaurar luego el cupo a 16. | Se muestra «Sin cupo» y el botón de inscripción está deshabilitado. | Aprobado: el usuario confirmó el aviso y el botón deshabilitado; restauró el cupo a 16. |
| RN2: inscripción duplicada | Ingresar con DNI `45123401`; buscar Ajedrez, donde ya está inscripto, y seleccionar «Inscribirme». | Se informa «El socio ya está inscripto en esta actividad» y no se crea otra inscripción. | Aprobado: se informó el mensaje esperado y el usuario confirmó que solo figura una inscripción a Ajedrez. |
| RN3: edad fuera de rango | Ingresar con DNI `45123401`; buscar Funcional adultos y seleccionar «Inscribirme». | Se informa que la actividad admite socios de entre 18 y 65 años y no se registra la inscripción. | Aprobado: se informó el rango de edad y el usuario confirmó que solo figura Ajedrez en sus actividades. |
| RN4: cuota morosa | Ingresar con DNI `34123414`; buscar Yoga y seleccionar «Inscribirme». | Se informa «Debe regularizar su cuota en secretaría» y no se registra la inscripción. | Aprobado: se informó el mensaje esperado y Yoga no aparece entre las actividades del socio. |
| RN5: máximo de actividades | Ingresar con DNI `44123404`, ya inscripto en Ajedrez; inscribirse en Vóley mixto y Básquet juvenil; intentar inscribirse en Natación. | Las dos primeras altas se confirman; la cuarta actividad se rechaza con «Alcanzó el máximo de 3 actividades». | Aprobado: apareció el mensaje esperado y se observaron Ajedrez, Vóley y Básquet, sin Natación. El usuario dio de baja las dos altas de prueba. |
