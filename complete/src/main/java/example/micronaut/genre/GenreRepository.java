package example.micronaut.genre;

import example.micronaut.domain.Genre;

import java.util.List;

public interface GenreRepository {

    Genre findById(Long id);

    Genre save(String name);

    void deleteById(Long id);

    List<Genre> findAll();

    Genre save(Genre genre);
}
