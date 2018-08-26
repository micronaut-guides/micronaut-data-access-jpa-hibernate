package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("application")
public class ApplicationConfigurationProperties implements ApplicationConfiguration {

    protected final Integer DEFAULT_MAX = 10;

    private Integer max = DEFAULT_MAX;

    @Override
    public Integer getMax() {
        return max;
    }
}
