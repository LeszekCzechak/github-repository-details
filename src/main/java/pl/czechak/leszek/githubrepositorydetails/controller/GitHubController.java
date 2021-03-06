package pl.czechak.leszek.githubrepositorydetails.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import pl.czechak.leszek.githubrepositorydetails.service.GitHubService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/repositories")
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/{owner}/{repository-name}")
    public ResponseEntity<Mono<Details>> getDetails(@PathVariable(name = "owner") String owner,
                                                    @PathVariable(name = "repository-name") String repositoryName) {
        return ResponseEntity.ok(gitHubService.getDetails(owner, repositoryName));
    }
}
