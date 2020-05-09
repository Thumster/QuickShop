/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Category;
import entity.Item;
import entity.Supermarket;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class ItemSessionBean implements ItemSessionBeanLocal {

    @PersistenceContext(unitName = "QuickShop-ejbPU")
    private EntityManager em;

    public ItemSessionBean() {
    }

    @Override
    public Long createNewItem(Item item, Long categoryId, Long supermarketId) {
        Category c = em.find(Category.class, categoryId);
        Supermarket s = em.find(Supermarket.class, supermarketId);
        item.setCategory(c);
        item.setSupermarket(s);
        em.persist(item);
        em.flush();
        return item.getItemId();
    }
    
    @Override
    public List<Item> retrieveAllItemsBySupermarketId(Long supermarketId) {
        Query query = em.createQuery("SELECT m FROM Item m WHERE m.supermarket.supermarketId = :inSupermarketId ORDER BY m.category.categoryName ASC");
        query.setParameter("inSupermarketId", supermarketId);
        List<Item> i = query.getResultList();
        
        return i;
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
