package comgenchi.geotools.exception;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionHandle extends ResponseEntityExceptionHandler {

 // RecordNotFoundException
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RecordNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request,
      HttpServletRequest httpRequest) {
    ErrorResponse errors = new ErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setError(ex.getMessage());
    errors.setObj(ex.getRequest());
    errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errors.setPath(httpRequest.getRequestURI());
    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleIllegalArgumentException (IllegalArgumentException ex, WebRequest request,
  HttpServletRequest httpRequest) {
    ErrorResponse errors = new ErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setError(ex.getMessage());
    errors.setObj(null);
    errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errors.setPath(httpRequest.getRequestURI());
    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("status", status.value());
    body.put("target", ex.getTarget());
    body.put("ObjectName", ex.getObjectName());
    List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
        .collect(Collectors.toList());
    body.put("errors", errors);
    return new ResponseEntity<>(body, headers, status);
  }
}
