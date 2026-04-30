package com.muv.muv.model;

import jakarta.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // hero, card
    private String title;
    private String content;
    private String imageUrl;
    private String bgColor;
    private String city;
    private Double flight;
    private Double hotel;
    private Double food;
    private Double transport;

    public Long getId() { return id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getBgColor() { return bgColor; }
    public void setBgColor(String bgColor) { this.bgColor = bgColor; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Double getFlight() { return flight; }
    public void setFlight(Double flight) { this.flight = flight; }

    public Double getHotel() { return hotel; }
    public void setHotel(Double hotel) { this.hotel = hotel; }

    public Double getFood() { return food; }
    public void setFood(Double food) { this.food = food; }

    public Double getTransport() { return transport; }
    public void setTransport(Double transport) { this.transport = transport; }
}