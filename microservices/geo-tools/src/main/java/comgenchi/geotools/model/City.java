package comgenchi.geotools.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Entity
@Table(name="cities")
@Validated
public class City {
    
    @javax.persistence.Id
    @GeneratedValue
    protected int Id;

    @ManyToOne
    @JoinColumn(name="country_code")
    protected Country country;

    @Column(name="country_code", insertable=false, updatable=false)
    protected String country_code; 

    @Column(name="postal_code", unique=true, nullable=false)
    protected String postal_code;
    
    @Column(name="position", unique=true, nullable=false)
    @NotBlank(message="position can not be blank")
    protected String position;

    @Column(name="region", unique=true, nullable=false)
    @NotBlank(message="region can not be blank")
    protected String region;

    @Column(name="region_code", unique=true, nullable=false)
    @NotBlank(message="region_code can not be blank")
    protected String region_code;

    @Column(name="province", unique=true, nullable=false)
    @NotBlank(message="province can not be blank")
    protected String province;
    
    @Column(name="sigle_province", unique=true, nullable=false)
    @Pattern(regexp="^([A-Z]{2,3})|([/d]{0,5})|(^$)$", message="sigle province format error")
    protected String sigle_province;

    @Column(name="latitude", unique=true, nullable=false)
    @Range(min=-90, max=90, message="Range for latitude must be from -90 to 90")
    protected String latitude;

    @Column(name="longitude", unique=true, nullable=false)
    @Range(min=-180, max=180, message="Range for longitude must be from -90 to 90")
    protected String longitude;

}
