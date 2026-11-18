package grupo1.socioclub.controller;

import grupo1.socioclub.api.dto.ApiError;
import grupo1.socioclub.service.ReglaNegocioException;
import grupo1.socioclub.service.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        ApiActividadController.class,
        ApiSocioController.class,
        ApiInscripcionController.class
})
public class ApiExceptionHandler {

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> reglaDeNegocio(ReglaNegocioException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> noEncontrado(RecursoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> solicitudInvalida(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException exception) {
        String mensaje = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("La solicitud contiene datos inválidos");
        return ResponseEntity.badRequest().body(new ApiError(mensaje));
    }
}
