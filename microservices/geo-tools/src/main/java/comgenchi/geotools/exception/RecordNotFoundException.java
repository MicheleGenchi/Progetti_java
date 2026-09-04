package comgenchi.geotools.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RecordNotFoundException extends RuntimeException 
{
    private Object request;

  public RecordNotFoundException(String exception, Object request) {
    super(exception);
    this.request=request;
  }

  public RecordNotFoundException(String exception, Throwable cause,  Object request) {
    super(exception, cause);
    this.request=request;
  }

}