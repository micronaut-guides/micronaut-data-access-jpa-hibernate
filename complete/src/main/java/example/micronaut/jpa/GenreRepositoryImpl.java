package example.micronaut.jpa;

import example.micronaut.domain.Genre;
import example.micronaut.genre.GenreRepository;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public GenreRepositoryImpl(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Genre findById(Long id) {
        return entityManager
                .createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    @Transactional
    public Genre save(String name) {
        Genre genre = new Genre(name);
        return save(genre);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Genre genre = findById(id);
        entityManager.remove(genre);
    }

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return entityManager
                .createQuery("SELECT g FROM Genre g", Genre.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

}
