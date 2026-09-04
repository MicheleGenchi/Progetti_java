package comgenchi.geotools.repository;

import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import comgenchi.geotools.model.City;

//Remove @RepositoryRestResource below to disable auto REST api:
public interface CityRepo extends PagingAndSortingRepository<City, Id>, JpaSpecificationExecutor<City> {

}