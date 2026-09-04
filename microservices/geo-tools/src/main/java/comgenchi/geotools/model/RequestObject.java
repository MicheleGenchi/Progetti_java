package comgenchi.geotools.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
public class RequestObject {

    @Valid
    private List<@Pattern(regexp = "^[A-Za-z]{2}$", message = "country_code must be have 2 letters")
                @NotBlank(message = "country_code not blank") String> country_code = new ArrayList<>();

    @Valid
    private List<@NotBlank(message = "country_name not blank") String> country_name = new ArrayList<>();

    @Valid 
    private List<@NotBlank(message = "region not blank") String> region = new ArrayList<>();

    @Valid 
    private List<@NotBlank(message = "province not blank") String> province = new ArrayList<>();

    @Valid 
    private List<@NotBlank(message = "sigle province not blank") String> sigle_province = new ArrayList<>();
    
    @Range(min=1, message="range page must be greather than 1")
    @Valid
    private Integer page;
    
    @Range(min=1, max=50, message="range size must be from 1 to 50")
    @Valid
    private Integer size;
    
    @Nullable
    private Map<String, String> ordina;

    public Map<String, String> getOrdina() {
        return ordina;
    }
}
