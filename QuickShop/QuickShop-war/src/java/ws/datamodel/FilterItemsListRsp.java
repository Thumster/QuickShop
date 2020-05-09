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
public class FilterItemsListRsp {

    List<Item> items;

    public FilterItemsListRsp() {
        items = new ArrayList<>();
    }

    public FilterItemsListRsp(List<Item> items) {
        this();
        this.items = items;
    }
    
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
