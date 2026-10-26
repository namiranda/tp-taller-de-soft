// ========================================
// SOCIOCLUB - ACTIVIDADES
// ========================================

// Clase solicitada en el TP
class Actividad {
    constructor(nombre, disciplina, profesor, dias, horario, edadMinima, edadMaxima, cupoMaximo, cuotaMensual) {
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
}

// Datos iniciales del TP
const actividades = [
    new Actividad("Fútbol infantil", "Fútbol", "Diego Ferreyra", "Lun y Mié", "18:00", 6, 12, 40, 18000),
    new Actividad("Vóley mixto", "Vóley", "Laura Ceballos", "Mar y Jue", "19:00", 13, 60, 24, 20000),
    new Actividad("Básquet juvenil", "Básquet", "Martín Oviedo", "Lun y Vie", "19:30", 12, 17, 20, 19000),
    new Actividad("Natación", "Natación", "Sofía Bustos", "Mar y Jue", "17:00", 5, 70, 30, 26000),
    new Actividad("Patín artístico", "Patín", "Carla Peralta", "Mié y Vie", "17:30", 5, 15, 25, 21000),
    new Actividad("Ajedrez", "Ajedrez", "Raúl Giménez", "Sáb", "10:00", 8, 99, 16, 9000),
    new Actividad("Funcional adultos", "Fitness", "Paula Luna", "Lun, Mié y Vie", "20:30", 18, 65, 30, 22000),
    new Actividad("Yoga", "Bienestar", "Inés Arguello", "Mar y Jue", "09:00", 16, 99, 15, 17000)
];

// Formatear importes en pesos argentinos
const formatoPesos = new Intl.NumberFormat("es-AR", {
    style: "currency",
    currency: "ARS",
    maximumFractionDigits: 0
});

// Mostrar actividades en pantalla
function mostrarActividades(lista) {
    const contenedor = document.getElementById("listaActividades");
    const contador = document.getElementById("cantidadResultados");
    const mensaje = document.getElementById("mensajeSinResultados");

    if (!contenedor) return;

    contenedor.innerHTML = "";

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
        edades.textContent = `Edades: ${actividad.edadMinima} a ${actividad.edadMaxima} años`;

        const cupo = document.createElement("p");
        cupo.textContent = `Cupo máximo: ${actividad.cupoMaximo}`;

        const cuota = document.createElement("p");
        cuota.classList.add("precio-actividad");
        cuota.textContent = `${formatoPesos.format(actividad.cuotaMensual)} / mes`;

        const enlace = document.createElement("a");
        enlace.href = "preinscripcion.html?actividad=" + encodeURIComponent(actividad.nombre);
        enlace.textContent = "Preinscribirme";

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

        contenedor.appendChild(tarjeta);
    });

    contador.textContent = `${lista.length} actividad(es) encontrada(s)`;
    mensaje.hidden = lista.length !== 0;
}

// Filtrar actividades por disciplina y edad
function filtrarActividades() {
    const disciplinaSeleccionada =
        document.getElementById("filtroDisciplina").value;

    const edadIngresada =
        document.getElementById("filtroEdad").value.trim();

    const resultado = actividades.filter((actividad) => {
        const coincideDisciplina =
            !disciplinaSeleccionada ||
            actividad.disciplina === disciplinaSeleccionada;

        const coincideEdad =
            edadIngresada === "" ||
            (
                Number(edadIngresada) >= actividad.edadMinima &&
                Number(edadIngresada) <= actividad.edadMaxima
            );

        return coincideDisciplina && coincideEdad;
    });

    mostrarActividades(resultado);
}

// Inicializar filtros cuando cargue la página
document.addEventListener("DOMContentLoaded", () => {
    const filtroDisciplina = document.getElementById("filtroDisciplina");
    const filtroEdad = document.getElementById("filtroEdad");

    if (filtroDisciplina && filtroEdad) {
        filtroDisciplina.addEventListener("change", filtrarActividades);
        filtroEdad.addEventListener("input", filtrarActividades);

        mostrarActividades(actividades);
    }
});

// ========================================
// SOCIOCLUB - VALIDACIÓN DE PREINSCRIPCIÓN
// ========================================

// Mostrar o quitar errores debajo de cada campo
function mostrarError(idCampo, idError, mensaje) {
    const campo = document.getElementById(idCampo);
    const error = document.getElementById(idError);

    error.textContent = mensaje;
    campo.classList.toggle("input-error", mensaje !== "");
    campo.setAttribute("aria-invalid", mensaje !== "" ? "true" : "false");
}

// Calcular edad a partir de la fecha de nacimiento
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

// Validar todos los campos del formulario
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
    const errorNombre = nombre === "" ? "Ingresá tu nombre." : "";
    mostrarError("nombre", "errorNombre", errorNombre);
    if (errorNombre) formularioValido = false;

    // APELLIDO
    const errorApellido = apellido === "" ? "Ingresá tu apellido." : "";
    mostrarError("apellido", "errorApellido", errorApellido);
    if (errorApellido) formularioValido = false;

    // DNI: exactamente 7 u 8 números
    let errorDni = "";

    if (dni === "") {
        errorDni = "Ingresá tu DNI.";
    } else if (!/^\d{7,8}$/.test(dni)) {
        errorDni = "El DNI debe contener 7 u 8 números.";
    }

    mostrarError("dni", "errorDni", errorDni);
    if (errorDni) formularioValido = false;

    // FECHA DE NACIMIENTO
    let errorFecha = "";

    if (fechaNacimiento === "") {
        errorFecha = "Ingresá tu fecha de nacimiento.";
    } else {
        const nacimiento = new Date(fechaNacimiento + "T00:00:00");
        const hoy = new Date();

        if (Number.isNaN(nacimiento.getTime()) || nacimiento > hoy) {
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
    const errorTelefono =
        telefono === "" ? "Ingresá tu teléfono." : "";

    mostrarError("telefono", "errorTelefono", errorTelefono);
    if (errorTelefono) formularioValido = false;

    // ACTIVIDAD
    const errorActividad =
        actividadSeleccionada === ""
            ? "Seleccioná una actividad."
            : "";

    mostrarError("actividad", "errorActividad", errorActividad);
    if (errorActividad) formularioValido = false;

    // VALIDAR EDAD PARA LA ACTIVIDAD SELECCIONADA
    if (!errorFecha && !errorActividad) {
        const actividadElegida = actividades.find(
            (actividad) => actividad.nombre === actividadSeleccionada
        );

        if (actividadElegida) {
            const edad = calcularEdad(fechaNacimiento);

            if (
                edad < actividadElegida.edadMinima ||
                edad > actividadElegida.edadMaxima
            ) {
                mostrarError(
                    "actividad",
                    "errorActividad",
                    `Esta actividad admite personas de ${actividadElegida.edadMinima} a ${actividadElegida.edadMaxima} años.`
                );

                formularioValido = false;
            }
        }
    }

    return formularioValido;
}

// Activar el formulario cuando cargue la página
document.addEventListener("DOMContentLoaded", () => {
    const formulario = document.getElementById(
        "formularioPreinscripcion"
    );

    // Evita errores en las páginas que no tienen formulario
    if (!formulario) return;
    // Quitar los mensajes de error al corregir los campos
    formulario.querySelectorAll("input, select").forEach((campo) => {
        const evento = campo.tagName === "SELECT" ? "change" : "input";

        campo.addEventListener(evento, () => {
            const mensajeError = campo.parentElement.querySelector(".mensaje-error");

            if (mensajeError) {
                mensajeError.textContent = "";
            }

            campo.classList.remove("input-error");
            campo.setAttribute("aria-invalid", "false");
        });
    });
    // Obtener la actividad seleccionada desde la página de actividades
    const parametros = new URLSearchParams(window.location.search);
    const actividadSeleccionada = parametros.get("actividad");

    if (actividadSeleccionada) {
        const selectorActividad = document.getElementById("actividad");

        const actividadExiste = actividades.some(
            (actividad) => actividad.nombre === actividadSeleccionada
        );

        if (actividadExiste) {
            selectorActividad.value = actividadSeleccionada;
        }
    }

    formulario.addEventListener("submit", (evento) => {
        // Evitar que se recargue la página
        evento.preventDefault();

        const mensaje = document.getElementById("mensajeFormulario");

        mensaje.textContent = "";
        mensaje.className = "";

        if (validarFormulario()) {
            const nombre = document.getElementById("nombre").value.trim();
            const actividad = document.getElementById("actividad").value;

            mensaje.textContent =
                `¡Gracias, ${nombre}! Los datos de tu solicitud para ${actividad} son válidos. ` +
                "Esta es una demostración: todavía no se enviaron ni guardaron.";

            mensaje.className = "exito";

            formulario.reset();
        } else {
            mensaje.textContent =
                "Revisá los campos marcados e intentá nuevamente.";

            mensaje.className = "error";
        }
    });
});