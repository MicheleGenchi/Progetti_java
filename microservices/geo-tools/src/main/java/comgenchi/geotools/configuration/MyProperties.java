package comgenchi.geotools.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class MyProperties {
    @Autowired
    private Environment env;

    // spring.datasource.url=jdbc:driver://url:port/database
    public Environment getEnv() {
        return env;
    }

    /**
     * @param key
     * @return object application.property value for config key
     * @exception throws NumberFormatException for port value
     */
    public Object get(String key, String value) throws NumberFormatException {
        // spring.datasource.url=spring.datasource.url=jdbc:driver://url:port/database
        @SuppressWarnings("null")
        String[] prop=env.getProperty(key).split("/");
        switch (value) {
            case "url": 
                //url:port
                String url=prop[2].split(":")[0];
                return url!=null?url:"";
            case "port":
                //url:port
                String port=prop[2].split(":")[1];
                Integer intport=Integer.parseInt(port);
                return intport;
            case "driver":
                String driver=prop[0].split(":")[1];
                return driver;
            default : return "";
        }
    }

    public Object get(String key) {
        return env.getProperty(key);
    }
}
