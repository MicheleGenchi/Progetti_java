package comgenchi.geotools.service;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import comgenchi.geotools.controller.Specs;
import comgenchi.geotools.model.Country;
import comgenchi.geotools.model.RequestObject;
import comgenchi.geotools.model.SearchCriteria;
import comgenchi.geotools.repository.CountryRepo;

@Service
public class CountryService {

  @Autowired
  CountryRepo repository;


  // public Map<String, Object> getAll(Pageable pageable) {
  //   return getAll(null, pageable);
  // }

  /**
   * @param list
   * @param pageable
   * @return
   */
  public Map<String, Object> get(RequestObject request, Pageable pageable) {
    /* paginazione filtrata */
    Specs<Country> specs = null;
    if (request != null) {
      for (Field field : request.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        if (field.getName() != "page" & field.getName() != "size" & field.getName()!="ordina") {
          try {
            if (!((List<Country>) field.get(request)).isEmpty()) {
              specs =
                new Specs<>(
                  new SearchCriteria(field.getName(), "in", field.get(request))
                );
            }
          } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
    }
    //Page<Country> pages = null;
    Map<String, Object> service = buildService(specs, pageable);
    return service;
  }

  private Map<String, Object> buildService(
    Specs<Country> specs,
    Pageable pageable
  ) {
    Map<String, Object> service = new LinkedHashMap<>();
    Page<Country> pages = null;
    do {
      Map<String, Object> servicePage = new LinkedHashMap<>();
      pages = repository.findAll(specs, pageable);
      // aggiunta delle informazioni sulla paginazione
      servicePage.put("countries", pages.getContent());
      servicePage.put("currentPage", pages.getNumber() + 1);
      service.put(String.valueOf(pages.getNumber() + 1), servicePage);
      pageable = pages.nextPageable();
    } while (pages.hasNext());
    service.put("totalItems", pages.getTotalElements());
    service.put("totalForPage", pages.getSize());
    service.put("totalPages", pages.getTotalPages());
    return service;
  }
}
