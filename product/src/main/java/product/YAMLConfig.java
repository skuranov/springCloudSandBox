package product;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {
    private String pathToSourceCSV;
    private String nameOfSourceCSV;

    public String getPathToSourceCSV() {
        return pathToSourceCSV;
    }

    public void setPathToSourceCSV(String pathToSourceCSV) {
        this.pathToSourceCSV = pathToSourceCSV;
    }

    public String getNameOfSourceCSV() {
        return nameOfSourceCSV;
    }

    public void setNameOfSourceCSV(String nameOfSourceCSV) {
        this.nameOfSourceCSV = nameOfSourceCSV;
    }
}
