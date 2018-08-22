package example.micronaut.book;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;

import java.util.List;

public interface BooksRepository {

    List<Book> findAllBooksByGenre(Long genreId);

    Book save(String isbn, String name, Genre genre);

    Book findById(Long id);

    void deleteById(Long id);

    List<Book> findAll();

    Book save(Book book);
}
