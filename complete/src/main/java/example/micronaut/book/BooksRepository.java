package example.micronaut.book;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BooksRepository {

    List<Book> findAllBooksByGenre(Long genreId);

    Book save(String isbn, String name, Genre genre);

    Optional<Book> findById(Long id);

    void deleteById(Long id);

    List<Book> findAll();

    int update(Long id, String isbn, String name, Genre genre);
}
