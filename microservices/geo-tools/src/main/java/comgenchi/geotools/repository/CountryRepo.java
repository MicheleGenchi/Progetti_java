package comgenchi.geotools.repository;

import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import comgenchi.geotools.model.Country;

//Remove @RepositoryRestResource below to disable auto REST api:
public interface CountryRepo extends PagingAndSortingRepository<Country, Id>, JpaSpecificationExecutor<Country> {

}