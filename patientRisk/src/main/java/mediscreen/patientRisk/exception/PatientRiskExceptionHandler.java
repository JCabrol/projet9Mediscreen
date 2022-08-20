package mediscreen.patientRisk.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PatientRiskExceptionHandler extends ResponseEntityExceptionHandler {


    private ResponseEntity<Object> buildResponseEntity(PatientRiskError patientRiskError) {
        return new ResponseEntity<>(patientRiskError.getMessage(), patientRiskError.getStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @NonNull MissingServletRequestParameterException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        String errorMessage = "The request is not correct : a request parameter is missing or wrong.\n";
        log.error(errorMessage);
        return buildResponseEntity(new PatientRiskError(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundObject(
            ObjectNotFoundException ex) {
        PatientRiskError patientRiskError = new PatientRiskError(NOT_FOUND);
        patientRiskError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(patientRiskError);
    }

}
