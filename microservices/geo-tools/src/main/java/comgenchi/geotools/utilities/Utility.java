package comgenchi.geotools.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class Utility {

  /**
   * @param pageable
   * @param keys
   * @return sort
   * @throws IllegalArgumentException
   */
  public static List<Order> sorting(Map<String, String> myProperties) {
      List<Order> orders=new ArrayList<>();
      for (Entry<String, String> e:myProperties.entrySet()) {
        Direction direction=Direction.fromString(e.getValue());
        orders.add(new Order(direction, e.getKey()));
      }
      return orders;
   }
}
