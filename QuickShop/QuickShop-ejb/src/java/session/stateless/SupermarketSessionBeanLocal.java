/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Supermarket;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface SupermarketSessionBeanLocal {

    public Supermarket retrieveSupermarketById(Long supermarketId);

    public Long createSupermarket(Supermarket supermarket);

    public List<Supermarket> retrieveAllSupermarkets();

}
