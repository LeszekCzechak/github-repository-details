package pl.czechak.leszek.githubrepositorydetails.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    public static MockWebServer mockWebServer;
    private GitHubService gitHubService;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();

        gitHubService = new GitHubService(webClient);

    }

    @Test
    void shouldReturnDetailsByOwnerAndRepositoryName() throws IOException {

        //given
        String repositoryName = "repoName";
        String owner = "username";
        Details details = new Details(repositoryName,
                "descTest",
                "urlTest",
                16,
                LocalDateTime.parse("2020-09-24T10:35:17"));

        String body = "{\"description\":\"descTest\",\n" +
                "\"stars\":16,\n" +
                "\"full_name\":\"repoName\",\n" +
                "\"clone_url\":\"urlTest\",\n" +
                "\"created_at\":\"2020-09-24T10:35:17\"" +
                "}";

        MockResponse mockResponse = new MockResponse()
                .clearHeaders()
                .addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(body);

        mockWebServer.enqueue(mockResponse);

        //when
        Mono<Details> responseMono = gitHubService.getDetails(owner, repositoryName);

        //then
        Details response = responseMono.block();
        assertThat(response.getCloneUrl()).isEqualTo(details.getCloneUrl());
        assertThat(response.getDescription()).isEqualTo(details.getDescription());
        assertThat(response.getStars()).isEqualTo(details.getStars());
    }
}
