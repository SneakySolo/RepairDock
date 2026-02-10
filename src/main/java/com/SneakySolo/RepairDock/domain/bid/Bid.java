package com.SneakySolo.RepairDock.domain.bid;

import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.request.Request;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int estimatedHours;

    @Column(length = 1000)
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status;

    @ManyToOne (optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne (optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private RepairShop repairShop;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = BidStatus.PENDING;
    }

    public Bid() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RepairShop getRepairShop() {
        return repairShop;
    }

    public void setRepairShop(RepairShop repairShop) {
        this.repairShop = repairShop;
    }
}
