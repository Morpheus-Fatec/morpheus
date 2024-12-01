package fatec.morpheus.DTO;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedNewsResponse<T> {
    private List<T> news;
    private int totalPages;
    private long totalElements;
}
