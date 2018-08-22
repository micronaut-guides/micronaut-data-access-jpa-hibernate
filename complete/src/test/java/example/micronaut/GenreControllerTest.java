package example.micronaut;

import example.micronaut.book.BookUpdateCommand;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import example.micronaut.genre.GenreSaveCommand;
import example.micronaut.genre.GenreUpdateCommand;
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

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GenreControllerTest {

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
    public void testGenreCrudOperations() {
        HttpRequest request = HttpRequest.POST("/genres", new GenreSaveCommand("Microservices"));
        HttpResponse response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        Long id = entityId(response);
        request = HttpRequest.GET("/genres/"+id);
        Genre genre = client.toBlocking().retrieve(request, Genre.class);

        assertEquals("Microservices",  genre.getName());

        request = HttpRequest.PUT("/genres/", new GenreUpdateCommand(id, "Micro-services"));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        id = entityId(response);
        request = HttpRequest.GET("/genres/"+id);
        genre = client.toBlocking().retrieve(request, Genre.class);
        assertEquals("Micro-services",  genre.getName());

        request = HttpRequest.GET("/genres");
        List<Genre> genres = client.toBlocking().retrieve(request, Argument.of(List.class, Genre.class));

        assertEquals(1, genres.size());

        // cleanup:
        request = HttpRequest.DELETE("/genres/"+id);
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    Long entityId(HttpResponse response) {
        return Long.valueOf(response.header(HttpHeaders.LOCATION).replaceAll("/genres/", ""));
    }
}
