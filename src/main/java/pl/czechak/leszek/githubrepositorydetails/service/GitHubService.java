package pl.czechak.leszek.githubrepositorydetails.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import reactor.core.publisher.Mono;

@Service
public class GitHubService {

//    @Value("${github.user.token}")
//    wartość wpisana  ręcznie na czas testów
    private String userToken = "965f496c7ac6bbf37c67f4e9d0f3f5194528d837";

    private final WebClient webClient;

    public GitHubService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Details> getDetails(String owner, String repositoryName) {

        return webClient.get()
                .uri("/" + owner + "/" + repositoryName)
                .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .header(HttpHeaders.AUTHORIZATION, "token " + userToken)
                .retrieve()
                .bodyToMono(Details.class);
    }
}
