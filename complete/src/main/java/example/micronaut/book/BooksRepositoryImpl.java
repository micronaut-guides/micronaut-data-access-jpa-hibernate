package example.micronaut.book;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
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
    public int update(Long id, String isbn, String name, Genre genre) {
        return entityManager.createQuery("UPDATE Book b SET isbn = :isbn, name = :name, genre = :genre where id = :id")
                .setParameter("name", name)
                .setParameter("isbn", isbn)
                .setParameter("genre", genre)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(book -> entityManager.remove(book));
    }
}
