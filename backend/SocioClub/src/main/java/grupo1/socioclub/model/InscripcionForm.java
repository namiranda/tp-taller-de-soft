package grupo1.socioclub.model;

import jakarta.validation.constraints.NotNull;

public class InscripcionForm {

    @NotNull(message = "El socio es obligatorio")
    private Long socioId;
    @NotNull(message = "La actividad es obligatoria")
    private Long actividadId;

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }
}
