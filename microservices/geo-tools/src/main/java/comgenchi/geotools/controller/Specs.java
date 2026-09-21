package comgenchi.geotools.controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import comgenchi.geotools.model.SearchCriteria;

// return in all method un specification<Country>
public final class Specs<T> implements Specification<T> {

  private final SearchCriteria criteria;

  public Specs(SearchCriteria searchCriteria) {
    this.criteria = searchCriteria;
  }

  @Override
  @Nullable
  public Predicate toPredicate(
    Root<T> root,
    CriteriaQuery<?> query,
    CriteriaBuilder builder
  ) {
    
    Path<Object> key = null;
    //resolve join Join<Country, City> joinCountry
    if (criteria.getKey().contains(".")) {
      String[] arrayRoot = criteria.getKey().split("[.]");
      String tableJoin = arrayRoot[0];
      String pk = arrayRoot[1];
      key = root.join(tableJoin).get(pk);
    } else {
      key=root.get(criteria.getKey());
    }

    switch (criteria.getOperation()) {
      case ":":
        if (key.getJavaType() == String.class) {
          return builder.like(
            root.<String>get(criteria.getKey()),
            "%" + criteria.getValue() + "%"
          );
        } else {
          return builder.equal(key, criteria.getValue());
        }
      case "=":
        return builder.equal(key, criteria.getValue());
      case "in":
        return builder.in(key).value(criteria.getValue());
      default:
        return null;
    }
  }
}
