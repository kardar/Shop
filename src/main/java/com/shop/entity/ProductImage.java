package com.shop.entity;

import javax.persistence.*;

@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public int getId() {
        return id;
    }

    public ProductImage(int id, String name, Product product) {
        this.id = id;
        this.name = name;
        this.product = product;
    }

    public ProductImage() {
    }

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Transient
    public String getImagePath(){
        return "/product-images/"+ product.getId() + "/extras/"+ this.name;
    }
}
