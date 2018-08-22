package example.micronaut;

import static org.junit.Assert.assertEquals;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class CatalogControllerTest {

    private static EmbeddedServer server;
    private static io.micronaut.http.client.HttpClient client;

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
    public void testRetrieveBooksByGenre() {
        HttpRequest request = HttpRequest.GET("/book/by-genre/Microservices");
        List books = client.toBlocking().retrieve(request, Argument.of(List.class, Book.class));

        assertEquals(3, books.size());
        assertEquals(((Book)books.get(0)).getGenre().getName(), "Microservices");
    }

    @Test
    public void testRetrieveBooksWithNonExistingGenre() {
        HttpRequest request = HttpRequest.GET("/book/by-genre/XXXXXXX");
        List books = client.toBlocking().retrieve(request, Argument.of(List.class, Book.class));

        assertEquals(0, books.size());
    }
}
