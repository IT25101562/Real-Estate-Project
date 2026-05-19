package com.luxeestates.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String property;
    private int rating;
    private String title;
    
    @Column(length = 2000)
    private String comment;
    
    @Column(name = "review_date")
    private LocalDate date;
    private String status; // Approved, Pending, Rejected

    // Default constructor
    public Review() {
        this.date = LocalDate.now();
        this.status = "Pending";
    }

    public Review(String name, String property, int rating, String title, String comment) {
        this.name = name;
        this.property = property;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.date = LocalDate.now();
        this.status = "Pending";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProperty() { return property; }
    public void setProperty(String property) { this.property = property; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
