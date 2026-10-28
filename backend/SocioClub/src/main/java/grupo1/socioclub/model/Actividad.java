package grupo1.socioclub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actividad")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "La disciplina es obligatoria")
    private String disciplina;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El profesor o profesora es obligatorio")
    private String profesor;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Los días son obligatorios")
    private String dias;

    @Column(nullable = false)
    @NotNull(message = "El horario es obligatorio")
    private LocalTime horario;

    @Column(name = "edad_minima", nullable = false)
    @NotNull(message = "La edad mínima es obligatoria")
    @Min(value = 0, message = "La edad mínima no puede ser menor a 0")
    @Max(value = 120, message = "La edad mínima no puede superar 120")
    private Integer edadMinima;

    @Column(name = "edad_maxima", nullable = false)
    @NotNull(message = "La edad máxima es obligatoria")
    @Min(value = 0, message = "La edad máxima no puede ser menor a 0")
    @Max(value = 120, message = "La edad máxima no puede superar 120")
    private Integer edadMaxima;

    @Column(name = "cupo_maximo", nullable = false)
    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
    @Max(value = 500, message = "El cupo máximo no puede superar 500")
    private Integer cupoMaximo;

    @Column(name = "cuota_mensual", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "La cuota mensual es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cuota mensual debe ser mayor a 0")
    private BigDecimal cuotaMensual;

    // Lado inverso de la relacion: la clave foranea esta en Inscripcion.actividad
    @OneToMany(mappedBy = "actividad")
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Actividad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(Integer edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

}
