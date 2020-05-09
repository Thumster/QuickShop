/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bryan
 */
public class FilterItemsListReq {

    Long supermarketId;
    List<Item> items;

    public FilterItemsListReq() {
        items = new ArrayList<>();
    }

    public FilterItemsListReq(Long supermarketId, List<Item> items) {
        this();
        this.supermarketId = supermarketId;
        this.items = items;
    }

    public Long getSupermarketId() {
        return supermarketId;
    }

    public void setSupermarketId(Long supermarketId) {
        this.supermarketId = supermarketId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    
    
    
    
}
