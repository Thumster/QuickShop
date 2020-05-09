/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Category;
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
public class CategorySessionBean implements CategorySessionBeanLocal {

    @PersistenceContext(unitName = "QuickShop-ejbPU")
    private EntityManager em;

    public CategorySessionBean() {
    }

    @Override
    public Long createCategory(Category c) {
        em.persist(c);
        em.flush();
        return c.getCategoryId();
    }
    
    @Override
    public List<Category> retrieveAllCategories() {
        Query query = em.createQuery("SELECT c FROM Category c ORDER BY c.categoryName ASC");
        List<Category> c = query.getResultList();
        return c;
    }
    
    public void persist(Object object) {
        em.persist(object);
    }

    
}
