package com.shop.entity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Catogry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 54, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @Transient
    private boolean hasChildren;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Catogry parent;

    public Catogry(){

    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public static Catogry copyIdAndName(Catogry catogry) {
       Catogry copyCat = new Catogry();
        copyCat.setName(catogry.getName());
        copyCat.setId(catogry.getId());

       return copyCat;

    }

    public static Catogry copyFull(Catogry catogry){
        Catogry copyCat = new Catogry();
        copyCat.setName(catogry.getName());
        copyCat.setId(catogry.getId());
        copyCat.setImage(catogry.getImage());
        copyCat.setAlias(catogry.getAlias());
        copyCat.setEnabled(catogry.isEnabled());
        copyCat.setHasChildren(catogry.getChildren().size() > 0);
        return copyCat;
    }

    public static Catogry copyFull(Catogry catogry, String name){
        Catogry copyCategory = Catogry.copyFull(catogry);
        copyCategory.setName(name);

        return copyCategory;
    }

    public static Catogry copyIdAndName(Integer id, String name) {
        Catogry copyCat = new Catogry();
        copyCat.setName(name);
        copyCat.setId(id);

        return copyCat;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Catogry getParent() {
        return parent;
    }

    public void setParent(Catogry parent) {
        this.parent = parent;
    }

    public Catogry(Integer id) {
        this.id = id;
    }

    public Catogry(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default";
    }
    public Catogry(String name, Catogry parent) {
        this(name);
       this.parent = parent;
    }

    public Set<Catogry> getChildren() {
        return children;
    }

    public String getCategoryImagePath(){
       if (this.id == null) return "/images/image-thumbnail.png";
        return "/category-images/"+this.id +"/"+ this.image;
    }

    public Catogry(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    @Transient
    public void setChildren(Set<Catogry> children) {
        this.children = children;
    }

    @OneToMany(mappedBy = "parent")
    private Set<Catogry> children = new HashSet<>();

    @Override
    public String toString() {
        return "Catogry{" +
                "name='" + name + '\'' +
                '}';
    }
}
