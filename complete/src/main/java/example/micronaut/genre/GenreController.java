package example.micronaut.genre;

import java.util.List;
import java.util.Optional;

import example.micronaut.domain.Genre;
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

@Validated
@Controller("/genres")
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
    public Optional<Genre> show(Long id) {
        return genreRepository.findById(id);
    }

    @Put("/")
    public HttpResponse update(@Body @Valid GenreUpdateCommand command) {

        int numberOfEntitiesUpdated = genreRepository.update(command.getId(), command.getName());

        return HttpResponse.noContent().header(HttpHeaders.LOCATION, location(command.getId()));
    }

    @Get("/")
    public List<Genre> list() {
        return genreRepository.findAll();
    }

    @Post("/")
    public HttpResponse save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location(genre));
    }

    @Delete("/{id}")
    public HttpResponse delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    protected String location(Long id) {
        return "/genres/"+id;
    }
    protected String location(Genre genre) {
        return location(genre.getId());
    }
}
