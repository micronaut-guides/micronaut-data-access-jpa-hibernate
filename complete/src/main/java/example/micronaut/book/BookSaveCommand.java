package example.micronaut.book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class BookSaveCommand {

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @NotBlank
    String isbn;

    @NotNull
    Long genreId;

    public BookSaveCommand() {}

    public BookSaveCommand(String isbn, String name, Long genreId) {
        this.isbn = isbn;
        this.name = name;
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSaveCommand that = (BookSaveCommand) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(isbn, that.isbn) &&
                Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isbn, genreId);
    }
}
