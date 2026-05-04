const regex = {
    nombre: /^[A-Za-z0-9 ]{1,30}$/,
    descripcion: /^[\s\S]{1,100}$/,
    salon: /^[1-5]0[1-3]$/,
    diaSemana: /^(LUNES|MARTES|MIERCOLES|JUEVES|VIERNES)$/,
    hora: /^([01]\d|2[0-3]):[0-5]\d$/
};

function limpiarErrores(formulario) {
    formulario.querySelectorAll(".is-invalid").forEach(campo => campo.classList.remove("is-invalid"));
    formulario.querySelectorAll(".invalid-feedback").forEach(error => error.textContent = "");
}

function marcarError(campo, mensaje) {
    campo.classList.add("is-invalid");
    const contenedor = campo.closest(".mb-3") || campo.parentElement;
    const error = contenedor.querySelector(".invalid-feedback");

    if (error) {
        error.textContent = mensaje;
    }
}

function validarHorario(horaInicio, horaFin, campoFin) {
    if (!regex.hora.test(horaInicio)) {
        return "La hora de inicio debe tener formato HH:mm.";
    }

    if (!regex.hora.test(horaFin)) {
        return "La hora de finalizacion debe tener formato HH:mm.";
    }

    if (horaFin <= horaInicio) {
        marcarError(campoFin, "La hora de finalizacion debe ser mayor a la hora de inicio.");
        return "La hora de finalizacion debe ser mayor a la hora de inicio.";
    }

    return "";
}

function cuerpoDesdeFormularioAsignatura(formulario) {
    return {
        nombre: formulario.nombre.value.trim(),
        descripcion: formulario.descripcion.value.trim(),
        salon: Number(formulario.salon.value),
        diaSemana: formulario.diaSemana.value,
        horaInicio: formulario.horaInicio.value,
        horaFin: formulario.horaFin.value,
        docenteId: Number(formulario.docenteId.value)
    };
}

function validarAsignatura(formulario) {
    limpiarErrores(formulario);

    const datos = cuerpoDesdeFormularioAsignatura(formulario);
    let valido = true;

    if (!regex.nombre.test(datos.nombre)) {
        marcarError(formulario.nombre, "Use solo letras, numeros y espacios. Maximo 30 caracteres.");
        valido = false;
    }

    if (!regex.descripcion.test(datos.descripcion)) {
        marcarError(formulario.descripcion, "La descripcion es obligatoria y maximo de 100 caracteres.");
        valido = false;
    }

    if (!regex.salon.test(String(datos.salon))) {
        marcarError(formulario.salon, "Seleccione un salon entre 101-103, 201-203, 301-303, 401-403 o 501-503.");
        valido = false;
    }

    if (!regex.diaSemana.test(datos.diaSemana)) {
        marcarError(formulario.diaSemana, "Seleccione un dia valido.");
        valido = false;
    }

    if (!regex.hora.test(datos.horaInicio)) {
        marcarError(formulario.horaInicio, "La hora de inicio debe tener formato HH:mm.");
        valido = false;
    }

    if (!regex.hora.test(datos.horaFin)) {
        marcarError(formulario.horaFin, "La hora de finalizacion debe tener formato HH:mm.");
        valido = false;
    }

    if (regex.hora.test(datos.horaInicio) && regex.hora.test(datos.horaFin) && datos.horaFin <= datos.horaInicio) {
        marcarError(formulario.horaFin, "La hora de finalizacion debe ser mayor a la hora de inicio.");
        valido = false;
    }

    if (!datos.docenteId) {
        marcarError(formulario.docenteId, "Seleccione un docente encargado.");
        valido = false;
    }

    return valido ? datos : null;
}

async function enviarJson(url, metodo, datos) {
    const respuesta = await fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: datos ? JSON.stringify(datos) : undefined
    });

    if (!respuesta.ok) {
        let mensaje = "No fue posible completar la solicitud.";

        try {
            const error = await respuesta.json();
            mensaje = error.mensaje || mensaje;
        } catch (e) {
            mensaje = respuesta.statusText || mensaje;
        }

        throw new Error(mensaje);
    }

    if (respuesta.status === 204) {
        return null;
    }

    return respuesta.json();
}

function configurarFormularioAsignatura() {
    const formulario = document.getElementById("formAsignatura");
    const modal = document.getElementById("modalAsignatura");

    if (!formulario || !modal) {
        return;
    }

    modal.addEventListener("show.bs.modal", event => {
        limpiarErrores(formulario);
        formulario.reset();
        formulario.elements["id"].value = "";

        const boton = event.relatedTarget;

        if (!boton || !boton.classList.contains("btn-editar-asignatura")) {
            modal.querySelector(".modal-title").textContent = "Crear Asignatura";
            return;
        }

        modal.querySelector(".modal-title").textContent = "Editar Asignatura";
        formulario.elements["id"].value = boton.dataset.id;
        formulario.nombre.value = boton.dataset.nombre;
        formulario.descripcion.value = boton.dataset.descripcion;
        formulario.salon.value = boton.dataset.salon;
        formulario.diaSemana.value = boton.dataset.dia;
        formulario.horaInicio.value = boton.dataset.inicio;
        formulario.horaFin.value = boton.dataset.fin;
        formulario.docenteId.value = boton.dataset.docente;
    });

    formulario.addEventListener("submit", async event => {
        event.preventDefault();

        const datos = validarAsignatura(formulario);
        if (!datos) {
            return;
        }

        const id = formulario.elements["id"].value;
        const url = id ? `/api/asignaturas/${id}` : "/api/asignaturas";
        const metodo = id ? "PUT" : "POST";

        try {
            await enviarJson(url, metodo, datos);
            window.location.reload();
        } catch (error) {
            alert(error.message);
        }
    });
}

function configurarEliminacionAsignatura() {
    document.querySelectorAll(".btn-eliminar-asignatura").forEach(boton => {
        boton.addEventListener("click", async () => {
            const confirmado = confirm("Desea eliminar esta asignatura?");

            if (!confirmado) {
                return;
            }

            try {
                await enviarJson(`/api/asignaturas/${boton.dataset.id}`, "DELETE");
                window.location.reload();
            } catch (error) {
                alert(error.message);
            }
        });
    });
}

function configurarFormularioHorario() {
    const formulario = document.getElementById("formHorario");

    if (!formulario) {
        return;
    }

    formulario.addEventListener("submit", async event => {
        event.preventDefault();
        limpiarErrores(formulario);

        const id = formulario.elements["id"].value;
        const datos = {
            diaSemana: formulario.diaSemana.value,
            horaInicio: formulario.horaInicio.value,
            horaFin: formulario.horaFin.value
        };

        if (!regex.diaSemana.test(datos.diaSemana)) {
            marcarError(formulario.diaSemana, "Seleccione un dia valido.");
            return;
        }

        const errorHorario = validarHorario(datos.horaInicio, datos.horaFin, formulario.horaFin);
        if (errorHorario) {
            if (!formulario.horaInicio.classList.contains("is-invalid") && !formulario.horaFin.classList.contains("is-invalid")) {
                marcarError(formulario.horaInicio, errorHorario);
            }
            return;
        }

        try {
            await enviarJson(`/api/asignaturas/${id}/horario`, "PUT", datos);
            window.location.reload();
        } catch (error) {
            alert(error.message);
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    configurarFormularioAsignatura();
    configurarEliminacionAsignatura();
    configurarFormularioHorario();
});
