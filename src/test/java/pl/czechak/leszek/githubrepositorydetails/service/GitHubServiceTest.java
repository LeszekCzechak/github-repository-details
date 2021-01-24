package pl.czechak.leszek.githubrepositorydetails.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper;

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

        objectMapper = new ObjectMapper();
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
                LocalDateTime.now());

        Mono<Details> detailsMono = Mono.just(details);

        String body = objectMapper.writeValueAsString(details);

        MockResponse mockResponse = new MockResponse()
                .clearHeaders()
                .addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(body);

        mockWebServer.enqueue(mockResponse);

        //when
        Mono<Details> responseMono = gitHubService.getDetails(owner, repositoryName);

        //then
        Details response = responseMono.block();
        assertThat(responseMono).isEqualTo(detailsMono);
    }
}
