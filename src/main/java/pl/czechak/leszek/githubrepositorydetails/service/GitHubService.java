package pl.czechak.leszek.githubrepositorydetails.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.czechak.leszek.githubrepositorydetails.dto.ResponseDetails;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import reactor.core.publisher.Mono;

@Service
public class GitHubService {

    @Value("${github.user.token}")
    private String userToken;

    private final WebClient webClient;

    public GitHubService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseDetails> getDetails(String owner, String repositoryName) {

        return webClient.get()
                .uri("/" + owner + "/" + repositoryName)
                .header(HttpHeaders.ACCEPT,"application/vnd.github.v3+json")
                .header(HttpHeaders.AUTHORIZATION, "token "+userToken)
                .retrieve()
                .bodyToMono(Details.class)
                .map(details -> {
                    ResponseDetails responseDetails = new ResponseDetails(
                            details.getFull_name(),
                            details.getDescription(),
                            details.getClone_url(),
                            details.getStars(),
                            details.getCreated_at()
                    );
                    return responseDetails;
                });
    }
}
