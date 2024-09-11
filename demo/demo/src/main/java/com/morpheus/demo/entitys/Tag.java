package com.morpheus.demo.entitys;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Long id;
    private String word;
    private List<Tag> synonyms;

}
