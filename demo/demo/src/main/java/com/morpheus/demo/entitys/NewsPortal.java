package com.morpheus.demo.entitys;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsPortal {
    private Long id;
    private String name;
    private String link;
    private Date registrationDate;
    private List<Tag> tags;
}
