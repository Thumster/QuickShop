/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;


import entity.Item;
import entity.Supermarket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.CategorySessionBeanLocal;
import session.stateless.ItemSessionBeanLocal;
import session.stateless.SupermarketSessionBeanLocal;
import ws.datamodel.ErrorRsp;
import ws.datamodel.RetrieveAllSupermarketsRsp;
import ws.datamodel.RetrieveItemRsp;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("QuickShop")
public class QuickShopResource {

    @Context
    private UriInfo context;

    private final CategorySessionBeanLocal categorySessionBeanLocal;
    private final ItemSessionBeanLocal itemSessionBeanLocal;
    private final SupermarketSessionBeanLocal supermarketSessionBeanLocal;

    public QuickShopResource() {
        categorySessionBeanLocal = lookupCategorySessionBeanLocal();
        itemSessionBeanLocal = lookupItemSessionBeanLocal();
        supermarketSessionBeanLocal = lookupSupermarketSessionBeanLocal();
    }

    @Path("retrieveAllItemBySupermarket")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMenuItemBySupermarket(@QueryParam("supermarketId") String supermarketId) {
        try {

            List<Item> items = itemSessionBeanLocal.retrieveAllItemsBySupermarketId(Long.parseLong(supermarketId));
            items.forEach(x -> x.setSupermarket(null));
            items.forEach(x -> x.getCategory().setItems(null));
            return Response.status(Response.Status.OK).entity(new RetrieveItemRsp(items)).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("retrieveAllSupermarkets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSupermarkets() {
        try {

            List<Supermarket> supermarkets = supermarketSessionBeanLocal.retrieveAllSupermarkets();

            for (Supermarket s : supermarkets) {
                s.getItems().clear();
            }

            return Response.status(Response.Status.OK).entity(new RetrieveAllSupermarketsRsp(supermarkets)).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private CategorySessionBeanLocal lookupCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategorySessionBeanLocal) c.lookup("java:global/QuickShop/QuickShop-ejb/CategorySessionBean!session.stateless.CategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ItemSessionBeanLocal lookupItemSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ItemSessionBeanLocal) c.lookup("java:global/QuickShop/QuickShop-ejb/ItemSessionBean!session.stateless.ItemSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private SupermarketSessionBeanLocal lookupSupermarketSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SupermarketSessionBeanLocal) c.lookup("java:global/QuickShop/QuickShop-ejb/SupermarketSessionBean!session.stateless.SupermarketSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
