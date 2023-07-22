package com.tiny.url.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
public class Url {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "tiny_url")
    private String tinyUrl;

    @Column(name = "original_url")
    private String originalUrl;

    @GeneratedValue
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Timestamp created_at;

    @GeneratedValue
    @Column(name = "updated_at", columnDefinition = "timestamp")
    @UpdateTimestamp
    private Timestamp updated_at;

    public Url(String tinyUrl, String originalUrl) {
        this.tinyUrl = tinyUrl;
        this.originalUrl = originalUrl;
    }

    public Url(String id, String tinyUrl, String originalUrl, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.tinyUrl = tinyUrl;
        this.originalUrl = originalUrl;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Url() {

    }

    @Override
    public String toString() {
        return "Url{" +
                "id='" + id + '\'' +
                ", tinyUrl='" + tinyUrl + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
