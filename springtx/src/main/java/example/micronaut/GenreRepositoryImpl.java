package example.micronaut;

import example.micronaut.domain.Genre;
//tag::import[]
import io.micronaut.spring.tx.annotation.Transactional;
//end::import[]
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//tag::clazz[]
@Singleton
public class GenreRepositoryImpl implements GenreRepository {
//end::clazz[]
    private final EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public GenreRepositoryImpl(EntityManager entityManager,
                               ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    //tag::findById[]
    @Transactional(readOnly = true)
    public Optional<Genre> findById(@NotNull Long id) {
    //end::findById[]
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    @Transactional
    //tag::save[]
    public Genre save(@NotBlank String name) {
    //end::save[]
        Genre genre = new Genre(name);
        entityManager.persist(genre);
        return genre;
    }

    @Override
    //tag::deleteById[]
    @Transactional
    public void deleteById(@NotNull Long id) {
    //end::deleteById[]
        findById(id).ifPresent(entityManager::remove);
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

    //tag::findAll[]
    @Transactional(readOnly = true)
    public List<Genre> findAll(@NotNull SortingAndOrderArguments args) {
    //end::findAll[]
        String qlString = "SELECT g FROM Genre as g";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
                qlString += " ORDER BY g." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Genre> query = entityManager.createQuery(qlString, Genre.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);

        return query.getResultList();
    }

    @Override
    //tag::update[]
    @Transactional
    public int update(@NotNull Long id, @NotBlank String name) {
    //end::update[]

        return entityManager.createQuery("UPDATE Genre g SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    //tag::saveWithException[]
    @Transactional
    public Genre saveWithException(@NotBlank String name) {
    //end::saveWithException[]
        save(name);
        throw new PersistenceException();
    }
}
