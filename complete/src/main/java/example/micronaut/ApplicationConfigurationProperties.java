package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("application") // <1>
public class ApplicationConfigurationProperties implements ApplicationConfiguration {

    protected final Integer DEFAULT_MAX = 10;

    private Integer max = DEFAULT_MAX;

    @Override
    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        if(max != null) {
            this.max = max;
        }
    }
}
