package example.micronaut;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class Bootstrap {

    private final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private final CatalogRepository catalogRepository;

    public Bootstrap(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @EventListener
    void onStartup(StartupEvent event) {
        Genre microservices = catalogRepository.save(new Genre("Microservices"));
        Genre devops = catalogRepository.save(new Genre("DevOps"));

        catalogRepository.save(new Book("Building Microservices", "1491950358", microservices));
        catalogRepository.save(new Book("Release It!", "1680502395", microservices));
        catalogRepository.save(new Book("Continuous Delivery", "0321601912", microservices));
        catalogRepository.save(new Book("The DevOps Handbook", "1942788002", devops));
        catalogRepository.save(new Book("The Phoenix Project", "0988262592", devops));
    }
}
