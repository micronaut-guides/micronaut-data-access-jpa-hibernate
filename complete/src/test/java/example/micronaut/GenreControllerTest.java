package example.micronaut;

import static org.junit.Assert.assertEquals;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import example.micronaut.domain.Genre;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

public class GenreControllerTest {

    private static EmbeddedServer server; // <1>
    private static HttpClient client; // <2>

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext
                .build()
                .run(EmbeddedServer.class); // <1>
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL()); // <2>
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void supplyAnInvalidOrderTriggersValidationFailure() {
        thrown.expect(HttpClientResponseException.class);
        thrown.expect(hasProperty("response", hasProperty("status", is(HttpStatus.BAD_REQUEST))));
        client.toBlocking().exchange(HttpRequest.GET("/genres/list?order=foo"));
    }

    @Test
    public void testFindNonExistingGenreReturns404() {
        thrown.expect(HttpClientResponseException.class);
        thrown.expect(hasProperty("response", hasProperty("status", is(HttpStatus.NOT_FOUND))));
        HttpResponse response = client.toBlocking().exchange(HttpRequest.GET("/genres/99"));
    }

    @Test
    public void testGenreCrudOperations() {

        List<Long> genreIds = new ArrayList<>();

        HttpRequest request = HttpRequest.POST("/genres", new GenreSaveCommand("DevOps")); // <3>
        HttpResponse response = client.toBlocking().exchange(request);
        genreIds.add(entityId(response));

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/genres", new GenreSaveCommand("Microservices")); // <3>
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        Long id = entityId(response);
        genreIds.add(id);
        request = HttpRequest.GET("/genres/"+id);

        Genre genre = client.toBlocking().retrieve(request, Genre.class); // <4>

        assertEquals("Microservices", genre.getName());

        request = HttpRequest.PUT("/genres", new GenreUpdateCommand(id, "Micro-services"));
        response = client.toBlocking().exchange(request);  // <5>

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/genres/" + id);
        genre = client.toBlocking().retrieve(request, Genre.class);
        assertEquals("Micro-services", genre.getName());

        request = HttpRequest.GET("/genres/list");
        List<Genre> genres = client.toBlocking().retrieve(request, Argument.of(List.class, Genre.class));

        assertEquals(2, genres.size());

        request = HttpRequest.GET("/genres/list?max=1");
        genres = client.toBlocking().retrieve(request, Argument.of(List.class, Genre.class));

        assertEquals(1, genres.size());
        assertEquals("DevOps", genres.get(0).getName());

        request = HttpRequest.GET("/genres/list?max=1&order=desc&sort=name");
        genres = client.toBlocking().retrieve(request, Argument.of(List.class, Genre.class));

        assertEquals(1, genres.size());
        assertEquals("Micro-services", genres.get(0).getName());

        request = HttpRequest.GET("/genres/list?max=1&offset=10");
        genres = client.toBlocking().retrieve(request, Argument.of(List.class, Genre.class));

        assertEquals(0, genres.size());

        // cleanup:
        for (Long genreId : genreIds) {
            request = HttpRequest.DELETE("/genres/"+genreId);
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }
    }

    protected Long entityId(HttpResponse response) {
        String path = "/genres/";
        String value = response.header(HttpHeaders.LOCATION);
        if ( value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if ( index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }
        return null;
    }
}
