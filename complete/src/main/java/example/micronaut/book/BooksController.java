package example.micronaut.book;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import example.micronaut.genre.GenreRepository;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
@Controller("/books")
public class BooksController {

    protected final BooksRepository booksRepository;
    protected final GenreRepository genreRepository;

    public BooksController(BooksRepository booksRepository,
                           GenreRepository genreRepository) {
        this.booksRepository = booksRepository;
        this.genreRepository = genreRepository;
    }

    @Get("/genres/{id}")
    public List<Book> listByGenre(Long id) {
        return booksRepository.findAllBooksByGenre(id);
    }

    @Put("/")
    public HttpResponse update(@Body @Valid BookUpdateCommand command) {
        Book book = booksRepository.findById(command.getId());
        if (book == null) {
            return HttpResponse.badRequest();
        }
        Genre genre = genreRepository.findById(command.getId());
        if (genre == null) {
            return HttpResponse.badRequest();
        }
        book.setName(command.getName());
        book.setIsbn(command.getIsbn());
        book.setGenre(genre);
        booksRepository.save(book);

        return HttpResponse.noContent().header(HttpHeaders.LOCATION, location(book));
    }

    @Get("/")
    public List<Book> list() {
        return booksRepository.findAll();
    }

    @Get("/{id}")
    Book show(Long id) {
        return booksRepository.findById(id);
    }

    @Delete("/{id}")
    HttpResponse delete(Long id) {
        booksRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    @Post("/")
    HttpResponse save(@Body @Valid BookSaveCommand cmd) {
        Genre genre = genreRepository.findById(cmd.getGenreId());
        if (genre == null) {
            return HttpResponse.badRequest();
        }

        Book book = booksRepository.save(cmd.getIsbn(), cmd.getName(), genre);

        return HttpResponse.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location(book));
    }

    protected String location(Book book) {
        return "/books/"+book.getId();
    }
}
