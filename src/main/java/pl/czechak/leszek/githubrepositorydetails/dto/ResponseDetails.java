package pl.czechak.leszek.githubrepositorydetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDetails {
    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars;
    private LocalDateTime createdAt;

}
