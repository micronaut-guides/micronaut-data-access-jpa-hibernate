package example.micronaut.genre;

import example.micronaut.ApplicationConfiguration;
import example.micronaut.PaginationArguments;
import example.micronaut.SortingArguments;
import example.micronaut.SortingOrder;
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

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Validated // <1>
@Controller("/genres") // <2>
public class GenreController {

    protected final GenreRepository genreRepository;

    protected final ApplicationConfiguration applicationConfiguration;

    public GenreController(GenreRepository genreRepository,
                           ApplicationConfiguration applicationConfiguration) { // <3>
        this.genreRepository = genreRepository;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Get("/{id}") // <4>
    public Genre show(Long id) {
        return genreRepository
                .findById(id)
                .orElse(null); // <5>
    }

    @Put("/") // <6>
    public HttpResponse update(@Body @Valid GenreUpdateCommand command) { // <7>
        int numberOfEntitiesUpdated = genreRepository.update(command.getId(), command.getName());

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath()); // <8>
    }

    @Get(value = "/") // <9>
    public List<Genre> list(@Nullable Integer offset,
                            @Nullable Integer max,
                            @Nullable String sort,
                            @Nullable String order) {
        PaginationArguments paginationArguments = new PaginationArguments(offset != null ? offset : 0,
                max != null ? max : applicationConfiguration.getMax());
        SortingArguments sortingArguments = sort != null ? new SortingArguments(sort, SortingOrder.of(order)) : null;

        return genreRepository.findAll(paginationArguments, sortingArguments);
    }

    @Post("/") // <10>
    public HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId())));
    }

    @Delete("/{id}") // <11>
    public HttpResponse delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    protected URI location(Long id) {
        return URI.create("/genres/" + id);
    }

    protected URI location(Genre genre) {
        return location(genre.getId());
    }
}
