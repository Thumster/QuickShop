/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

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
public class SupermarketSessionBean implements SupermarketSessionBeanLocal {

    @PersistenceContext(unitName = "QuickShop-ejbPU")
    private EntityManager em;

    
    
    public SupermarketSessionBean(){
        
    }
    
    @Override
    public Supermarket retrieveSupermarketById(Long supermarketId){
        Supermarket s = em.find(Supermarket.class, supermarketId);
        return s;
    }
    
    @Override
    public List<Supermarket> retrieveAllSupermarkets() {
        Query query = em.createQuery("SELECT s FROM Supermarket s ORDER BY s.supermarketName ASC");
        
        List<Supermarket> supermarkets = query.getResultList();
        
        for (Supermarket s : supermarkets) {
            s.getItems();
        }
        
        return supermarkets;
    }
    
    @Override
    public Long createSupermarket(Supermarket supermarket){
        em.persist(supermarket);
        em.flush();
        return supermarket.getSupermarketId();
    }

    public void persist(Object object) {
        em.persist(object);
    }
   
}
