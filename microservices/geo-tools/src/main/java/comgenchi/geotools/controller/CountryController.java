package comgenchi.geotools.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comgenchi.geotools.exception.RecordNotFoundException;
import comgenchi.geotools.model.DEFAULTVALUE;
import comgenchi.geotools.model.RequestObject;
import comgenchi.geotools.service.CountryService;
import comgenchi.geotools.utilities.Utility;

@RestController
@RequestMapping("country")
class CountryController {

  @Autowired
  CountryService service;

  /**
   * @param request
   * @param bindingResult
   * @return
   *         ResponseEntity<Map<String,Object>>
   * @throws MethodArgumentNotValidException
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws Exception
   */
  @GetMapping("/get")
  public ResponseEntity<Map<String, ?>> get (
    @Valid @RequestBody RequestObject request,
    final BindingResult bindingResult
  )
    throws IllegalArgumentException, RecordNotFoundException, MethodArgumentNotValidException, NoSuchMethodException, SecurityException, Exception {
    if (bindingResult.hasErrors()) {
      //Method currentMethod=this.getClass().getMethod("getAll",  RequestObject.class);
      //MethodParameter methodParameter=new MethodParameter(currentMethod, 0);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }

    if (request.getPage() == null) request.setPage(DEFAULTVALUE.PAGE.get());

    if (request.getSize() == null) request.setSize(DEFAULTVALUE.SIZE.get());

    Map<String, ?> countries = null;
    // DEFAULTVALUE.PAGE 1 and DEFAULTVALUE.SIZE=50
    Pageable pageable = PageRequest.of(
      request.getPage() - 1,
      request.getSize()
    );

    // Ordina more field
    if (request.getOrdina() != null) {
      final Sort sort = Sort.by(Utility.sorting(request.getOrdina()));
      pageable = PageRequest.of(
        request.getPage() - 1,
        request.getSize(),
        sort
      );
    }

    // filter
    countries = service.get(request, pageable);

    // service restituisce empty se la pagina richiesta è maggiore di quelle
    // presenti o se la richiesta non restituisce risultati
    if (countries.isEmpty()) {
      if (
        request.getPage().intValue() >
        ((Integer) countries.get("totalPages")).intValue()
      ) {
        throw new RecordNotFoundException(
          "The page " + request.getPage() + " is over the total page",
          request
        );
      }
      // la tabella interrogata non ha dati
      throw new RecordNotFoundException(
        "Invalid request, return no data",
        request
      );
    }

    ResponseEntity<Map<String, ?>> response = new ResponseEntity<>(
      countries,
      HttpStatus.OK
    );
    return response;
  }

  @GetMapping("/test")
  public String test() {
    return "test endpoint ok";
  }
}
