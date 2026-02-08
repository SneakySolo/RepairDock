package com.SneakySolo.RepairDock.domain.request;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "request_media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "request_id", nullable = false)
    private Request request;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column (nullable = false)
    private String filePath;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
