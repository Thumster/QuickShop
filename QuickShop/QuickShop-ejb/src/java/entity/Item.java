/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String itemName;

    @Column(nullable = true, length = 128)
    @Size(max = 128)
    private String itemDescription;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private Double itemPrice;

    @Column
    private Integer itemQuantity;

    @Column
    private Integer posX;

    @Column
    private Integer posY;

    @XmlTransient
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Supermarket supermarket;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String imagePath = "item-default.png";

    public Item() {
        itemQuantity = getRand(50, 100);
    }

    
    public Item(String itemName, String itemDescription, Double itemPrice) {
        this();
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
    }

    public Item(String itemName, String itemDescription, Double itemPrice, String imagePath, Integer posX, Integer posY) {
        this();
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.posX = posX;
        this.posY = posY;
        this.imagePath = imagePath;
    }

    public final int getRand(int start, int end) {
        return (int) (Math.random() * (end - start)) + start;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (this.category != null) {
            if (this.category.getItems().contains(this)) {
                this.category.getItems().remove(this);
            }
        }

        this.category = category;

        if (this.category != null) {
            if (!this.category.getItems().contains(this)) {
                this.category.getItems().add(this);
            }
        }
    }

    public Supermarket getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(Supermarket supermarket) {
        if (this.supermarket != null) {
            if (this.supermarket.getItems().contains(this)) {
                this.supermarket.getItems().remove(this);
            }
        }

        this.supermarket = supermarket;

        if (this.supermarket != null) {
            if (!this.supermarket.getItems().contains(this)) {
                this.supermarket.getItems().add(this);
            }
        }
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the itemId fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Item[ id=" + itemId + " ]";
    }

}
