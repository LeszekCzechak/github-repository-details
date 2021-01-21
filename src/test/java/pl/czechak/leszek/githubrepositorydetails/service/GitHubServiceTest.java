package pl.czechak.leszek.githubrepositorydetails.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private WebClient webClient;

    public static MockWebServer mockWebServer;

    private ObjectMapper objectMapper;
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
    void setUpService() {
        String baseUrl = String.format("http://localhost:%s", //Nie wiem jak przypisać ten URL do gitHubService
                mockWebServer.getPort());
        objectMapper = new ObjectMapper();
        gitHubService = new GitHubService(webClient);
    }


    @Test
    void shouldReturnDetailsByOwnerAndRepositoryName() throws IOException {

        //given
        String repositoryName = "repoName";
        String owner = "username";
        String userToken = "";
        Details details = new Details(repositoryName,
                "descTest",
                "urlTest",
                16,
                LocalDateTime.MAX);

        Mono<Details> detailsMono = Mono.just(details);

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(details))
                .addHeader("Content-Type", "application/json"));

        //when
        Mono<Details> response = gitHubService.getDetails(owner, repositoryName);

        //then
        assertThat(response).isEqualTo(detailsMono);
    }
}
