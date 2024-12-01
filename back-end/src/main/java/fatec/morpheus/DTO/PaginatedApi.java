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
public class PaginatedApi<T> {
    private List<T> api;
    private int totalPages;
    private long totalElements;
}
