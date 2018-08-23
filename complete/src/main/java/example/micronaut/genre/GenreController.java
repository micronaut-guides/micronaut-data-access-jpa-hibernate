package example.micronaut.genre;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import example.micronaut.domain.Genre;
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

@Validated // <1>
@Controller("/genres") // <2>
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <4>
    public Genre show(Long id) {
        return genreRepository
                .findById(id)
                .orElse(null);
    }

    @Put("/") // <5>
    public HttpResponse update(@Body @Valid GenreUpdateCommand command) { // <6>

        int numberOfEntitiesUpdated = genreRepository.update(command.getId(), command.getName());

        return HttpResponse.noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath()); // <7>
    }

    @Get("/") // <8>
    public List<Genre> list() {
        return genreRepository.findAll();
    }

    @Post("/") // <9>
    public HttpResponse save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse.created(location(genre));
    }

    @Delete("/{id}") // <10>
    public HttpResponse delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    protected URI location(Long id) {
        return URI.create("/genres/"+id);
    }
    protected URI location(Genre genre) {
        return location(genre.getId());
    }
}
