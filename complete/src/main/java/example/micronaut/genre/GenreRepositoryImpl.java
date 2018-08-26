package example.micronaut.genre;

import example.micronaut.PaginationArguments;
import example.micronaut.SortingArguments;
import example.micronaut.domain.Genre;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton // <1>
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;  // <2>

    public GenreRepositoryImpl(@CurrentSession EntityManager entityManager) { // <2>
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true) // <3>
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    @Transactional // <4>
    public Genre save(String name) {
        Genre genre = new Genre(name);
        entityManager.persist(genre);
        return genre;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(genre -> entityManager.remove(genre));
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

    @Transactional(readOnly = true)
    public List<Genre> findAll(PaginationArguments paginationArgs, SortingArguments sortingArgs) {
        String qlString = "SELECT g FROM Genre as g";
        if (sortingArgs != null) {
            if (sortingArgs.getOrder() != null && VALID_PROPERTY_NAMES.contains(sortingArgs.getSort())) {
                qlString += " ORDER BY g." + sortingArgs.getSort() + " " + sortingArgs.getOrder().toString().toLowerCase();
            }
        }
        TypedQuery query = entityManager.createQuery(qlString, Genre.class);
        if (paginationArgs != null) {
            if (paginationArgs.getMax() != null) {
                query.setMaxResults(paginationArgs.getMax());
            }
            if (paginationArgs.getOffset() != null) {
                query.setFirstResult(paginationArgs.getOffset());
            }
        }
        return query.getResultList();
    }

    @Override
    @Transactional
    public int update(Long id, String name) {
        return entityManager.createQuery("UPDATE Genre g SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate();
    }
}
