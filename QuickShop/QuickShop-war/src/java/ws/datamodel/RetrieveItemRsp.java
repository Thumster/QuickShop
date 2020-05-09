/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Item;
import java.util.List;

/**
 *
 * @author User
 */
public class RetrieveItemRsp {
    
    List<Item> items;

    public RetrieveItemRsp() {
    }

    public RetrieveItemRsp(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    
    
}
