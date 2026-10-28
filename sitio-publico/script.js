// ========================================
// SOCIOCLUB - ACTIVIDADES
// ========================================

// Clase Actividad
class Actividad {
    constructor(
        nombre,
        disciplina,
        profesor,
        dias,
        horario,
        edadMinima,
        edadMaxima,
        cupoMaximo,
        cuotaMensual
    ) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.profesor = profesor;
        this.dias = dias;
        this.horario = horario;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.cupoMaximo = cupoMaximo;
        this.cuotaMensual = cuotaMensual;
    }

    // Verifica si la edad está dentro del rango permitido
    esAptaParaEdad(edad) {
        return edad >= this.edadMinima &&
               edad <= this.edadMaxima;
    }
}

// ========================================
// DATOS INICIALES DEL CLUB
// ========================================

const actividades = [
    new Actividad(
        "Fútbol infantil",
        "Fútbol",
        "Diego Ferreyra",
        "Lun y Mié",
        "18:00",
        6, 12, 40, 18000
    ),

    new Actividad(
        "Vóley mixto",
        "Vóley",
        "Laura Ceballos",
        "Mar y Jue",
        "19:00",
        13, 60, 24, 20000
    ),

    new Actividad(
        "Básquet juvenil",
        "Básquet",
        "Martín Oviedo",
        "Lun y Vie",
        "19:30",
        12, 17, 20, 19000
    ),

    new Actividad(
        "Natación",
        "Natación",
        "Sofía Bustos",
        "Mar y Jue",
        "17:00",
        5, 70, 30, 26000
    ),

    new Actividad(
        "Patín artístico",
        "Patín",
        "Carla Peralta",
        "Mié y Vie",
        "17:30",
        5, 15, 25, 21000
    ),

    new Actividad(
        "Ajedrez",
        "Ajedrez",
        "Raúl Giménez",
        "Sáb",
        "10:00",
        8, 99, 16, 9000
    ),

    new Actividad(
        "Funcional adultos",
        "Fitness",
        "Paula Luna",
        "Lun, Mié y Vie",
        "20:30",
        18, 65, 30, 22000
    ),

    new Actividad(
        "Yoga",
        "Bienestar",
        "Inés Arguello",
        "Mar y Jue",
        "09:00",
        16, 99, 15, 17000
    )
];

// ========================================
// FORMATO DE PRECIOS
// ========================================

const formatoPesos = new Intl.NumberFormat("es-AR", {
    style: "currency",
    currency: "ARS",
    maximumFractionDigits: 0
});

// ========================================
// MOSTRAR ACTIVIDADES EN PANTALLA
// ========================================

function mostrarActividades(lista) {
    const contenedor = document.getElementById("listaActividades");
    const contador = document.getElementById("cantidadResultados");
    const mensaje = document.getElementById("mensajeSinResultados");

    if (!contenedor) return;

    // Limpiar las tarjetas anteriores
    contenedor.innerHTML = "";

    // Crear una tarjeta por cada actividad
    lista.forEach((actividad) => {
        const tarjeta = document.createElement("article");
        tarjeta.classList.add("tarjeta-actividad");

        const titulo = document.createElement("h3");
        titulo.textContent = actividad.nombre;

        const disciplina = document.createElement("p");
        disciplina.textContent = `Disciplina: ${actividad.disciplina}`;

        const profesor = document.createElement("p");
        profesor.textContent = `Profesor/a: ${actividad.profesor}`;

        const dias = document.createElement("p");
        dias.textContent = `Días: ${actividad.dias}`;

        const horario = document.createElement("p");
        horario.textContent = `Horario: ${actividad.horario}`;

        const edades = document.createElement("p");
        edades.textContent =
            `Edades: ${actividad.edadMinima} a ${actividad.edadMaxima} años`;

        const cupo = document.createElement("p");
        cupo.textContent = `Cupo máximo: ${actividad.cupoMaximo}`;

        const cuota = document.createElement("p");
        cuota.classList.add("precio-actividad");
        cuota.textContent =
            `${formatoPesos.format(actividad.cuotaMensual)} / mes`;

        // Botón de preinscripción
        const enlace = document.createElement("a");

        enlace.href =
            "preinscripcion.html?actividad=" +
            encodeURIComponent(actividad.nombre);

        enlace.textContent = "Preinscribirme";

        // Agregar elementos a la tarjeta
        tarjeta.append(
            titulo,
            disciplina,
            profesor,
            dias,
            horario,
            edades,
            cupo,
            cuota,
            enlace
        );

        // Agregar tarjeta a la página
        contenedor.appendChild(tarjeta);
    });

    // Actualizar cantidad de resultados
    if (contador) {
        contador.textContent =
            `${lista.length} actividad(es) encontrada(s)`;
    }

    // Mostrar mensaje cuando no hay resultados
    if (mensaje) {
        mensaje.hidden = lista.length !== 0;
    }
}

// ========================================
// FILTRAR ACTIVIDADES
// ========================================

function filtrarActividades() {
    const disciplinaSeleccionada =
        document.getElementById("filtroDisciplina").value;

    const edadIngresada =
        document.getElementById("filtroEdad").value.trim();

    const resultado = actividades.filter((actividad) => {

        // Verificar disciplina
        const coincideDisciplina =
            disciplinaSeleccionada === "" ||
            actividad.disciplina === disciplinaSeleccionada;

        // Verificar edad usando el método de la clase
        const coincideEdad =
            edadIngresada === "" ||
            actividad.esAptaParaEdad(Number(edadIngresada));

        return coincideDisciplina && coincideEdad;
    });

    mostrarActividades(resultado);
}

// ========================================
// ACTIVAR FILTROS
// ========================================

document.addEventListener("DOMContentLoaded", () => {
    const filtroDisciplina = document.getElementById("filtroDisciplina");
    const filtroEdad = document.getElementById("filtroEdad");

    // Ejecutar solamente en actividades.html
    if (filtroDisciplina && filtroEdad) {
        filtroDisciplina.addEventListener("change", filtrarActividades);
        filtroEdad.addEventListener("input", filtrarActividades);

        // Mostrar todas las actividades al ingresar
        mostrarActividades(actividades);
    }
});

// ========================================
// SOCIOCLUB - VALIDACIÓN DEL FORMULARIO
// ========================================

// Mostrar errores debajo de cada campo
function mostrarError(idCampo, idError, mensaje) {
    const campo = document.getElementById(idCampo);
    const error = document.getElementById(idError);

    error.textContent = mensaje;

    campo.classList.toggle("input-error", mensaje !== "");

    campo.setAttribute(
        "aria-invalid",
        mensaje !== "" ? "true" : "false"
    );
}

// ========================================
// CALCULAR EDAD
// ========================================

function calcularEdad(fechaNacimiento) {
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento + "T00:00:00");

    let edad = hoy.getFullYear() - nacimiento.getFullYear();

    const mes = hoy.getMonth() - nacimiento.getMonth();

    if (
        mes < 0 ||
        (mes === 0 && hoy.getDate() < nacimiento.getDate())
    ) {
        edad--;
    }

    return edad;
}

// ========================================
// VALIDAR FORMULARIO COMPLETO
// ========================================

function validarFormulario() {
    const nombre = document.getElementById("nombre").value.trim();
    const apellido = document.getElementById("apellido").value.trim();
    const dni = document.getElementById("dni").value.trim();
    const fechaNacimiento = document.getElementById("fechaNacimiento").value;
    const email = document.getElementById("email").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const actividadSeleccionada = document.getElementById("actividad").value;

    let formularioValido = true;

    // NOMBRE
    const errorNombre = nombre === ""
        ? "Ingresá tu nombre."
        : "";

    mostrarError("nombre", "errorNombre", errorNombre);

    if (errorNombre) formularioValido = false;

    // APELLIDO
    const errorApellido = apellido === ""
        ? "Ingresá tu apellido."
        : "";

    mostrarError("apellido", "errorApellido", errorApellido);

    if (errorApellido) formularioValido = false;

    // DNI: 7 u 8 dígitos numéricos, sin puntos
    let errorDni = "";

    if (dni === "") {
        errorDni = "Ingresá tu DNI.";
    } else if (!/^\d{7,8}$/.test(dni)) {
        errorDni = "El DNI debe contener 7 u 8 números, sin puntos.";
    }

    mostrarError("dni", "errorDni", errorDni);

    if (errorDni) formularioValido = false;

    // FECHA DE NACIMIENTO
    let errorFecha = "";

    if (fechaNacimiento === "") {
        errorFecha = "Ingresá tu fecha de nacimiento.";
    } else {
        const nacimiento = new Date(
            fechaNacimiento + "T00:00:00"
        );

        const hoy = new Date();

        if (
            Number.isNaN(nacimiento.getTime()) ||
            nacimiento > hoy
        ) {
            errorFecha = "Ingresá una fecha de nacimiento válida.";
        }
    }

    mostrarError(
        "fechaNacimiento",
        "errorFechaNacimiento",
        errorFecha
    );

    if (errorFecha) formularioValido = false;

    // CORREO ELECTRÓNICO
    let errorEmail = "";

    if (email === "") {
        errorEmail = "Ingresá tu correo electrónico.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        errorEmail = "Ingresá un correo electrónico válido.";
    }

    mostrarError("email", "errorEmail", errorEmail);

    if (errorEmail) formularioValido = false;

    // TELÉFONO
    const errorTelefono = telefono === ""
        ? "Ingresá tu teléfono."
        : "";

    mostrarError("telefono", "errorTelefono", errorTelefono);

    if (errorTelefono) formularioValido = false;

    // ACTIVIDAD
    let errorActividad = actividadSeleccionada === ""
        ? "Seleccioná una actividad."
        : "";

    // VALIDAR EDAD SEGÚN LA ACTIVIDAD
    if (!errorFecha && !errorActividad) {
        const actividadElegida = actividades.find(
            (actividad) =>
                actividad.nombre === actividadSeleccionada
        );

        if (!actividadElegida) {
            errorActividad = "Seleccioná una actividad válida.";
        } else {
            const edad = calcularEdad(fechaNacimiento);

            // Reutilizamos el método de la clase Actividad
            if (!actividadElegida.esAptaParaEdad(edad)) {
                errorActividad =
                    `Esta actividad admite personas de ` +
                    `${actividadElegida.edadMinima} a ` +
                    `${actividadElegida.edadMaxima} años.`;
            }
        }
    }

    mostrarError(
        "actividad",
        "errorActividad",
        errorActividad
    );

    if (errorActividad) formularioValido = false;

    return formularioValido;
}

// ========================================
// ACTIVAR FORMULARIO DE PREINSCRIPCIÓN
// ========================================

document.addEventListener("DOMContentLoaded", () => {

    const formulario = document.getElementById(
        "formularioPreinscripcion"
    );

    // Ejecutar solamente en preinscripcion.html
    if (!formulario) return;

    // ========================================
    // QUITAR ERRORES AL CORREGIR LOS CAMPOS
    // ========================================

    formulario.querySelectorAll("input, select").forEach((campo) => {

        const evento = campo.tagName === "SELECT"
            ? "change"
            : "input";

        campo.addEventListener(evento, () => {

            const mensajeError = campo.parentElement.querySelector(
                ".mensaje-error"
            );

            if (mensajeError) {
                mensajeError.textContent = "";
            }

            campo.classList.remove("input-error");

            campo.setAttribute("aria-invalid", "false");
        });
    });

    // ========================================
    // SELECCIONAR ACTIVIDAD AUTOMÁTICAMENTE
    // ========================================

    // Obtener los parámetros de la URL
    const parametros = new URLSearchParams(
        window.location.search
    );

    const actividadSeleccionada = parametros.get("actividad");

    if (actividadSeleccionada) {

        const selectorActividad = document.getElementById(
            "actividad"
        );

        // Comprobar que la actividad exista
        const actividadExiste = actividades.some(
            (actividad) =>
                actividad.nombre === actividadSeleccionada
        );

        if (actividadExiste) {
            selectorActividad.value = actividadSeleccionada;
        }
    }

    // ========================================
    // ENVIAR Y VALIDAR FORMULARIO
    // ========================================

    formulario.addEventListener("submit", (evento) => {

        // Evitar que se recargue la página
        evento.preventDefault();

        const mensaje = document.getElementById(
            "mensajeFormulario"
        );

        // Limpiar mensajes anteriores
        mensaje.textContent = "";
        mensaje.className = "";

        // Validar todos los campos
        if (validarFormulario()) {

            const nombre = document.getElementById(
                "nombre"
            ).value.trim();

            const actividad = document.getElementById(
                "actividad"
            ).value;

            // Mostrar confirmación
            mensaje.textContent =
                `¡Gracias, ${nombre}! ` +
                `Los datos de tu solicitud para ${actividad} son válidos. ` +
                "Esta es una demostración: todavía no se enviaron ni guardaron.";

            mensaje.className = "exito";

            // Limpiar formulario
            formulario.reset();

        } else {

            // Mostrar mensaje si hay errores
            mensaje.textContent =
                "Revisá los campos marcados e intentá nuevamente.";

            mensaje.className = "error";
        }
    });
});