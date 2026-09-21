package comgenchi.geotools.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="countries")
@Validated
public class Country {

    @Id
    @Column(name="country_code", unique=true, nullable=false)
    @Pattern(regexp="^[A-Za-z]{2}$", message="country_code must be have 2 letters")
    @NotBlank(message = "country code not blank")
    protected String country_code;

    @Column(name="country", unique=true, nullable=false)
    @NotBlank(message = "country name not blank")
    protected String country;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @JsonIgnore
    List<City> cities=null;
}