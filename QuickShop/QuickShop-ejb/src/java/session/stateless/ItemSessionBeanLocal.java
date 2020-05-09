/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Item;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface ItemSessionBeanLocal {
    public List<Item> retrieveAllItemsBySupermarketId(Long supermarketId);
    public Long createNewItem(Item item, Long categoryId, Long supermarketId);
    
}
