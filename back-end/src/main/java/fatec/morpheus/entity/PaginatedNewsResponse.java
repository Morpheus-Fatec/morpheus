package fatec.morpheus.entity;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedNewsResponse {
    private List<NewsReponse> news;
    private int totalPages;
    private long totalElements;
}
