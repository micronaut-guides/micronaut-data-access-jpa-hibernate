package example.micronaut.jpa;

import example.micronaut.book.BooksRepository;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
public class BooksRepositoryImpl implements BooksRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public BooksRepositoryImpl(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Book save(String isbn, String name, Genre genre) {
        Book book = new Book(isbn, name, genre);
        return save(book);
    }

    @Override
    @Transactional
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return entityManager
                .createQuery("SELECT b FROM Book b WHERE b.id = :id", Book.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllBooksByGenre(Long genreId) {
        return entityManager
                .createQuery("SELECT b FROM Book b JOIN FETCH b.genre g WHERE g.id = :genreid", Book.class)
                .setParameter("genreid", genreId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return entityManager
                .createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Book book = findById(id);
        entityManager.remove(book);
    }
}
