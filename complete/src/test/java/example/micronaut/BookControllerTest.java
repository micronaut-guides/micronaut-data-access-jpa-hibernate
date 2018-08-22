package example.micronaut;

import example.micronaut.book.BookUpdateCommand;
import example.micronaut.domain.Book;
import example.micronaut.book.BookSaveCommand;
import example.micronaut.genre.GenreSaveCommand;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookControllerTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testBookCrudOperations() {
        List<Long> genreIds = new ArrayList<>();
        List<Long> bookIds = new ArrayList<>();

        HttpRequest request = HttpRequest.POST("/genres", new GenreSaveCommand("Microservices"));
        HttpResponse response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        genreIds.add(entityId(response, "/genres/"));

        request = HttpRequest.POST("/books", new BookSaveCommand("1491950358", "Microservices", genreIds.get(0)));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        bookIds.add(entityId(response, "/books/"));

        request = HttpRequest.POST("/books", new BookSaveCommand("1680502395", "Release It!", genreIds.get(0)));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        bookIds.add(entityId(response, "/books/"));

        request = HttpRequest.POST("/books", new BookSaveCommand("0321601912", "Continuous Delivery", genreIds.get(0)));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        bookIds.add(entityId(response, "/books/"));

        request = HttpRequest.POST("/genres", new GenreSaveCommand("DevOps"));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        genreIds.add(entityId(response, "/genres/"));

        request = HttpRequest.POST("/books", new BookSaveCommand("1942788002", "The DevOps Handbook", genreIds.get(1)));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        bookIds.add(entityId(response, "/books/"));

        request = HttpRequest.POST("/books", new BookSaveCommand("0988262592", "The Phoenix Project", genreIds.get(1)));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        Long id = entityId(response, "/books/");
        bookIds.add(id);

        request = HttpRequest.GET("/books");
        List books = client.toBlocking().retrieve(request, Argument.of(List.class, Book.class));

        assertEquals(5, books.size());

        request = HttpRequest.GET("/books/genres/"+genreIds.get(0));
        books = client.toBlocking().retrieve(request, Argument.of(List.class, Book.class));

        assertEquals(3, books.size());
        assertEquals(((Book)books.get(0)).getGenre().getName(), "Microservices");

        request = HttpRequest.GET("/books/genres/999");
        books = client.toBlocking().retrieve(request, Argument.of(List.class, Book.class));
        assertEquals(0, books.size());

        request = HttpRequest.GET("/books/"+id);
        Book book = client.toBlocking().retrieve(request, Book.class);

        assertEquals("The Phoenix Project",  book.getName());
        assertEquals("0988262592",  book.getIsbn());
        assertEquals("DevOps",  book.getGenre().getName());

        request = HttpRequest.PUT("/books/", new BookUpdateCommand(id,
                book.getIsbn(),
                "Phoenix Project: A Novel about It, Devops, and Helping Your Business Win",
                genreIds.get(0)));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/books/"+id);
        book = client.toBlocking().retrieve(request, Book.class);
        assertEquals("Phoenix Project: A Novel about It, Devops, and Helping Your Business Win",  book.getName());
        assertEquals("0988262592",  book.getIsbn());
        assertEquals("Microservices",  book.getGenre().getName());

        // cleanup:
        for ( Long bookId : bookIds) {
            request = HttpRequest.DELETE("/books/"+bookId);
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }

        for ( Long genreId : genreIds) {
            request = HttpRequest.DELETE("/genres/"+genreId);
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }
    }

    Long entityId(HttpResponse response, String pathToBeRemoved) {
        return Long.valueOf(response.header(HttpHeaders.LOCATION).replaceAll(pathToBeRemoved, ""));
    }
}
