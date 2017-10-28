package com.howtoprogram.springwebclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public class SpringWebClientApplicationTests {

  @Test
  public void retrieveTest() {

    WebClient client = WebClient.create("http://localhost:8080/v1");

    long id = 1;
    Mono<Book> response = client.get().uri("/books/{id}", id).accept(MediaType.APPLICATION_JSON)
        .retrieve().bodyToMono(Book.class);

    assertNotNull(response.block());

  }

  @Test
  public void exchangeTest() {

    WebClient client = WebClient.create("http://localhost:8080/v1");

    long id = 1;
    Mono<ClientResponse> response =
        client.get().uri("/books/{id}", id).accept(MediaType.APPLICATION_JSON).exchange();


    assertEquals(HttpStatus.OK, response.block().statusCode());


    Mono<ResponseEntity<Book>> resEntity = client.get().uri("/books/{id}", id)
        .accept(MediaType.APPLICATION_JSON).exchange().flatMap(res -> res.toEntity(Book.class));

    assertEquals(HttpStatus.OK, resEntity.block().getStatusCode());

  }

  @Test
  public void httpGetTest() {
    WebClient client = WebClient.create("http://localhost:8080/v1");

    Flux<Book> response = client.get().uri("/books").accept(MediaType.APPLICATION_JSON).retrieve()
        .bodyToFlux(Book.class);
    List<Book> books = response.collectList().block();
    assertTrue(books.size() > 0);

  }

  @Test
  public void httpGetSingleTest() {
    WebClient client = WebClient.create("http://localhost:8080/v1");
    long id = 1;
    Mono<Book> response = client.get().uri("/books/{id}", id).accept(MediaType.APPLICATION_JSON)
        .retrieve().bodyToMono(Book.class);
    Book book = response.block();
    assertEquals(id, book.getId().longValue());

  }

  @Test
  public void httpPostTest() {

    WebClient client = WebClient.create("http://localhost:8080/v1");
    Book spring5Book = new Book("Spring 5. 0 Microservices", "Rajesh R. V");

    Mono<Book> spring5Mono = Mono.just(spring5Book);

    Mono<Book> response = client.post().uri("/books").accept(MediaType.APPLICATION_JSON)
        .body(spring5Mono, Book.class).retrieve().bodyToMono(Book.class);

    Book createdBook = response.block();

    assertTrue(createdBook.getId() > 0);
    assertEquals(spring5Book.getName(), createdBook.getName());
    assertEquals(spring5Book.getAuthor(), createdBook.getAuthor());

  }

  @Test
  public void httpPutTest() {
    WebClient client = WebClient.create("http://localhost:8080/v1");
    Book proSpringBook = new Book("Pro Spring Boot", "");
    Mono<Book> proSpring5Mono = Mono.just(proSpringBook);
    // create the book
    Mono<Book> response = client.post().uri("/books").accept(MediaType.APPLICATION_JSON)
        .body(proSpring5Mono, Book.class).retrieve().bodyToMono(Book.class);

    proSpringBook = response.block();

    // update the author of the book

    proSpringBook.setAuthor("Gutierrez, Felipe");
    proSpring5Mono = Mono.just(proSpringBook);
    response =
        client.put().uri("/books/{id}", proSpringBook.getId()).accept(MediaType.APPLICATION_JSON)
            .body(proSpring5Mono, Book.class).retrieve().bodyToMono(Book.class);
    Book updatedBook = response.block();

    assertEquals(proSpringBook.getId(), updatedBook.getId());
    assertEquals(proSpringBook.getName(), updatedBook.getName());
    assertEquals(proSpringBook.getAuthor(), updatedBook.getAuthor());

  }

  @Test
  public void httpDeleteTest() {

    WebClient client = WebClient.create("http://localhost:8080/v1");
    Book mstSpringBook = new Book("Mastering Spring 5.0", "Ranga Rao Karanam");
    Mono<Book> mstSpringMono = Mono.just(mstSpringBook);
    // create the book
    Mono<Book> response = client.post().uri("/books").accept(MediaType.APPLICATION_JSON)
        .body(mstSpringMono, Book.class).retrieve().bodyToMono(Book.class);

    mstSpringBook = response.block();
    assertTrue(mstSpringBook.getId() > 0);

    // delete the book

    Mono<ClientResponse> clientRes = client.delete().uri("/books/{id}", mstSpringBook.getId())
        .accept(MediaType.APPLICATION_JSON).exchange();
    assertEquals(HttpStatus.NO_CONTENT, clientRes.block().statusCode());

  }

  @Test
  public void basicAuthTest() {
    WebClient client = WebClient.builder().filter(basicAuthentication("user", "passwd"))
        .baseUrl("http://httpbin.org").build();

    Mono<ClientResponse> response =
        client.get().uri("/basic-auth/user/passwd").accept(MediaType.APPLICATION_JSON).exchange();
    assertEquals(HttpStatus.OK, response.block().statusCode());

  }
}
