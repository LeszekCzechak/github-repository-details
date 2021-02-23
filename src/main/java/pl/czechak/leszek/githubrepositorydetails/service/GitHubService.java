package pl.czechak.leszek.githubrepositorydetails.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GitHubService {

    @Value("${github.user.token}")
    private String userToken;

    private final WebClient githubWebClient;

    public Mono<Details> getDetails(String owner, String repositoryName) {

        return githubWebClient.get()
                .uri("/" + owner + "/" + repositoryName)
                .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .header(HttpHeaders.AUTHORIZATION, "token " + userToken)
                .retrieve()
                .bodyToMono(Details.class);
    }
}
