package com.SneakySolo.RepairDock.domain.rating;

import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (optional = false)
    @JoinColumn (name = "shop_id", nullable = false)
    private RepairShop repairShop;

    @ManyToOne (optional = false)
    @JoinColumn (name = "customer_id", nullable = false)
    private User customer;

    @OneToOne(optional = false)
    @JoinColumn(name = "request_id", unique = true, nullable = false)
    private Request request;

    @Column (nullable = false)
    private Integer star;

    @Column (nullable = true, length = 200)
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Rating() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RepairShop getRepairShop() {
        return repairShop;
    }

    public void setRepairShop(RepairShop repairShop) {
        this.repairShop = repairShop;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}