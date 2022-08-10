package mediscreen.patientInformation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PatientInformationExceptionHandler extends ResponseEntityExceptionHandler {


    private ResponseEntity<Object> buildResponseEntity(PatientInformationError patientInformationError) {
        return new ResponseEntity<>(patientInformationError.getMessage(), patientInformationError.getStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @NonNull MissingServletRequestParameterException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        String errorMessage = "The request is not correct : a request parameter is missing or wrong.\n";
        log.error(errorMessage);
        return buildResponseEntity(new PatientInformationError(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "The request is not correct : please verify the request's url.\n";
        log.error(errorMessage);
        return buildResponseEntity(new PatientInformationError(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "The request is not correct : please verify the request's url.\n";
        log.error(errorMessage);
        return buildResponseEntity(new PatientInformationError(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "The researched page was not found.\n";
        log.error(errorMessage);
        return buildResponseEntity(new PatientInformationError(NOT_FOUND, errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "The request is not correct : please verify the request's body.\n";
        log.error(errorMessage);
        return buildResponseEntity(new PatientInformationError(BAD_REQUEST, errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        PatientInformationError patientInformationError = new PatientInformationError();
        patientInformationError.setStatus(BAD_REQUEST);
        List<String> allMessageError = new ArrayList<>();
        for(FieldError fieldError:ex.getBindingResult().getFieldErrors()){
           String errorMessage = fieldError.getDefaultMessage();
           allMessageError.add(errorMessage);
        }
        StringBuilder message = new StringBuilder("There is a problem with the following given data : ");
        for (String defaultMassage :allMessageError
             ) {
            message.append("\n- ").append(defaultMassage);
        }
        patientInformationError.setMessage(message.toString());
        log.error(message.toString());
        return buildResponseEntity(patientInformationError);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundObject(
            ObjectNotFoundException ex) {
        PatientInformationError patientInformationError = new PatientInformationError(NOT_FOUND);
        patientInformationError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(patientInformationError);
    }

}
