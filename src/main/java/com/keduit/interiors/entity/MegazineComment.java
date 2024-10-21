package com.keduit.interiors.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

public class MegazineComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentWriter;

    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "megazine_id")
    private Megazine megazine;

    private LocalDateTime commentCreatedTime;
}
