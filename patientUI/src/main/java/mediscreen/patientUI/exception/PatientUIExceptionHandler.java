package mediscreen.patientUI.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PatientUIExceptionHandler extends ResponseEntityExceptionHandler {


    private ModelAndView buildErrorPage(PatientUIError patientUIError) {
        String viewName = "error";
        Map<String, Object> model = new HashMap<>();
        model.put("status", patientUIError.getStatus());
        model.put("statusName", valueOf(patientUIError.getStatus().value()).name());
        model.put("message", patientUIError.getMessage());
        return new ModelAndView(viewName, model);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ModelAndView handleNotFoundObject(
            ObjectNotFoundException ex) {
        PatientUIError patientUIError = new PatientUIError(NOT_FOUND);
        patientUIError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(patientUIError);
    }
}
