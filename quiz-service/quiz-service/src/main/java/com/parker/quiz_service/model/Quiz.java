package com.parker.quiz_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // this will make the id auto-generated.
    private Integer id;
    private String title;

    @ElementCollection
    private List<Integer> questionIds;

}
