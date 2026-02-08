package com.SneakySolo.RepairDock.domain.request;

import com.SneakySolo.RepairDock.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "requests")
public class Request {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(nullable = false, length = 100)
    private String Title;

    @Column(nullable = false, length = 1000)
    private String Description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.requestStatus = RequestStatus.OPEN;
    }
}
