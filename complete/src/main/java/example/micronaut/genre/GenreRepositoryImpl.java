package example.micronaut.genre;

import example.micronaut.domain.Genre;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Singleton
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public GenreRepositoryImpl(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> findById(Long id) {
        try {

            return Optional.of(entityManager
                    .createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class)
                    .setParameter("id", id)
                    .getSingleResult());

        } catch(NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
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

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return entityManager
                .createQuery("SELECT g FROM Genre g", Genre.class)
                .getResultList();
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
