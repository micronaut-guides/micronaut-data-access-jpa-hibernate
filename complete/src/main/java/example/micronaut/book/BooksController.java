package example.micronaut.book;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import example.micronaut.genre.GenreRepository;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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
        Optional<Genre> genreOptional = genreRepository.findById(command.getGenreId());
        return genreOptional.map(genre -> {
            booksRepository.update(command.getId(), command.getIsbn(), command.getName(), genre);
            return HttpResponse.noContent().header(HttpHeaders.LOCATION, location(command.getId()).getPath());
        }).orElse(HttpResponse.badRequest());
    }

    @Get("/")
    public List<Book> list() {
        return booksRepository.findAll();
    }

    @Get("/{id}")
    Book show(Long id) {
        return booksRepository
                .findById(id)
                .orElse(null);
    }

    @Delete("/{id}")
    HttpResponse delete(Long id) {
        booksRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    @Post("/")
    HttpResponse save(@Body @Valid BookSaveCommand cmd) {
        Optional<Genre> genreOptional = genreRepository.findById(cmd.getGenreId());
        return genreOptional.map(genre -> {
            Book book = booksRepository.save(cmd.getIsbn(), cmd.getName(), genre);
            return HttpResponse
                    .created(book)
                    .headers(headers -> headers.location(location(book)));
        }).orElse(HttpResponse.badRequest());
    }

    protected URI location(Book book) {
        return location(book.getId());
    }

    protected URI location(Long id) {
        return URI.create("/books/" + id);
    }

}
