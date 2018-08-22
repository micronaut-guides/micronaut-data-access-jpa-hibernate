package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Controller("/")
public class CatalogController {

    private final CatalogRepository catalogRepository;

    public CatalogController(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Get("/book/by-genre/{genreName}")
    List<Book> booksByGenre(String genreName) {
       return catalogRepository.findAllBooksByGenreName(genreName);
    }
}
