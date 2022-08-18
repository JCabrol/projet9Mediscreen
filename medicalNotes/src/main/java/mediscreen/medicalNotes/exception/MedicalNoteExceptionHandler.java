package mediscreen.medicalNotes.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MedicalNoteExceptionHandler extends ResponseEntityExceptionHandler {


    private ResponseEntity<Object> buildResponseEntity(MedicalNoteError medicalNoteError) {
        return new ResponseEntity<>(medicalNoteError.getMessage(), medicalNoteError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "The request is not correct : please verify the request's body.\n";
        log.error(errorMessage);
        return buildResponseEntity(new MedicalNoteError(BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundObject(
            ObjectNotFoundException ex) {
        MedicalNoteError medicalNoteError = new MedicalNoteError(NOT_FOUND);
        medicalNoteError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(medicalNoteError);
    }

}
