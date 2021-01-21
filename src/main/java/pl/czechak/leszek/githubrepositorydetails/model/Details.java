package pl.czechak.leszek.githubrepositorydetails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Details {

    @JsonProperty("full_name")
    private String fullName;
    private String description;
    @JsonProperty("clone_url")
    private String cloneUrl;
    private Integer stars;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
