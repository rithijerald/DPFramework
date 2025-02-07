package com.server.dp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated DB ID
    private Long csvId; // ID from CSV file
    private Double salePrice;
    private Double noisyPrice;

    // Default constructor (Required for JPA)
    public DataRecord() {}

    // Constructor without `id` (Used when saving new records)
    public DataRecord(Long csvId, Double salePrice, Double noisyPrice) {
        this.csvId = csvId;
        this.salePrice = salePrice;
        this.noisyPrice = noisyPrice;
    }

    // Constructor with all fields (Including database ID)
    public DataRecord(Long id, Long csvId, Double salePrice, Double noisyPrice) {
        this.id = id;
        this.csvId = csvId;
        this.salePrice = salePrice;
        this.noisyPrice = noisyPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCsvId() { return csvId; }
    public void setCsvId(Long csvId) { this.csvId = csvId; }

    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }

    public Double getNoisyPrice() { return noisyPrice; }
    public void setNoisyPrice(Double noisyPrice) { this.noisyPrice = noisyPrice; }
}
