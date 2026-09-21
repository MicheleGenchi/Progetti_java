package comgenchi.geotools.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import comgenchi.geotools.configuration.MyProperties;

@RestController
public class ConnectionController {

  @Autowired
  MyProperties env;
  
  /**
   * @param address
   * @param port
   * @return connection true or connection false
   */
  private static boolean hostAvailabilityCheck(String address, int port) {
    try (Socket s = new Socket(address, port)) {
            return true; //possible to connect it
        } catch (IOException ex) {
          return false; //not is possible to connect it
        }
    }
  
  /**
   * @return ResponseEntity<Map<String, Object>> value in ConfigProp env
   */
  @GetMapping("/connection")
  public ResponseEntity<Map<String, Object>> connect() {
    
    Map<String, Object> map = new LinkedHashMap<>(); //LinkedHashMap mantiene l'ordine di inserimento
      map.put("driver", env.get("spring.datasource.url", "driver"));
      map.put("url", env.get("spring.datasource.url","url"));
      map.put("port", env.get("spring.datasource.url", "port"));
      map.put("username", env.get("spring.datasource.username"));
      map.put("password", env.get("spring.datasource.password"));

      boolean connectionOK=hostAvailabilityCheck((String) map.get("url"), (Integer) map.get("port"));
      map.put("connectionOK", connectionOK);
      
      ResponseEntity<Map<String, Object>> response=new ResponseEntity<>(map, HttpStatus.OK);
    return response;
  }
}
