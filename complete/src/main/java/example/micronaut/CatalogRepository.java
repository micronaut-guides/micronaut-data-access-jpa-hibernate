package example.micronaut;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CatalogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CatalogRepository(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    Genre save(Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

    @Transactional
    Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Transactional(readOnly = true)
    List<Book> findAllBooksByGenreName(String genreName) {
        return entityManager
            .createQuery("SELECT b FROM Book b JOIN FETCH b.genre g WHERE g.name = :genreName", Book.class)
            .setParameter("genreName", genreName)
            .getResultList();
    }
}
