package com.shop.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 45, unique = true)
    private String name;

    @Column(nullable = false, length = 128, unique = false)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns =  @JoinColumn(name = "category_id")
    )
    private Set<Catogry> categories = new HashSet<>();

    public Brand() {
    }

    public Brand(String name) {
        this.name = name;
        this.logo = "brand-logo.png";
    }

    public int getId() {
        return id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Catogry> getCategories() {
        return categories;
    }

    public void setCategories(Set<Catogry> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", catogries=" + categories +
                '}';
    }

    public Brand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    public String getLogoPath(){
        if (this.id == 0 ) return "images/image-thumbnail.png";
        return "/brand-logos/" +this.id + "/" + this.logo;
    }
}
