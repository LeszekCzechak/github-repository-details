package pl.czechak.leszek.githubrepositorydetails.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.czechak.leszek.githubrepositorydetails.model.Details;
import pl.czechak.leszek.githubrepositorydetails.service.GitHubService;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = GitHubController.class)
class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Test
    void shouldReturnDetailsByUsernameAndRepositoryName() throws Exception {

        //given
        String repoName = "repoName";
        String username = "username";
        String url = "/repositories/" + username + "/" + repoName;

        when(gitHubService.getDetails(username, repoName)).thenReturn(Mono.empty());

        //when//then

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<String> firstArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> secondArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(gitHubService).getDetails(firstArgumentCaptor.capture(), secondArgumentCaptor.capture());
        String firstArgumentCaptorValue = firstArgumentCaptor.getValue();
        String secondArgumentCaptorValue = secondArgumentCaptor.getValue();

        assertThat(firstArgumentCaptorValue).isEqualTo(username);
        assertThat(secondArgumentCaptorValue).isEqualTo(repoName);
        assertThat(secondArgumentCaptorValue).isNotEqualTo(username);


    }
}

