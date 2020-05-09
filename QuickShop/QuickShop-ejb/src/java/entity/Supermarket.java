/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User
 */
@Entity
public class Supermarket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supermarketId;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String supermarketName;
    
    @Column(nullable = false, length = 8000)
    @NotNull
    private String map;
    
    @Column(nullable = false)
    @NotNull
    private Integer dimensionX;
    
    @Column(nullable = false)
    @NotNull
    private Integer dimensionY;

    @OneToMany(mappedBy = "supermarket")
    private List<Item> items;

    public Supermarket() {
        items = new ArrayList<>();
    }

    public Supermarket(String supermarketName, String map, Integer dimensionX, Integer dimensionY) {
        this();
        this.supermarketName = supermarketName;
        this.map = map;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
    }
    
    
    public Long getSupermarketId() {
        return supermarketId;
    }

    public void setSupermarketId(Long supermarketId) {
        this.supermarketId = supermarketId;
    }

    public String getSupermarketName() {
        return supermarketName;
    }

    public void setSupermarketName(String supermarketName) {
        this.supermarketName = supermarketName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Integer getDimensionX() {
        return dimensionX;
    }

    public void setDimensionX(Integer dimensionX) {
        this.dimensionX = dimensionX;
    }

    public Integer getDimensionY() {
        return dimensionY;
    }

    public void setDimensionY(Integer dimensionY) {
        this.dimensionY = dimensionY;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supermarketId != null ? supermarketId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the supermarketId fields are not set
        if (!(object instanceof Supermarket)) {
            return false;
        }
        Supermarket other = (Supermarket) object;
        if ((this.supermarketId == null && other.supermarketId != null) || (this.supermarketId != null && !this.supermarketId.equals(other.supermarketId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Supermarket[ id=" + supermarketId + " ]";
    }

}
