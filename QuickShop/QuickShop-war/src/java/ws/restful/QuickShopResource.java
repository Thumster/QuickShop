/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import entity.Item;
import entity.Supermarket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.CategorySessionBeanLocal;
import session.stateless.ItemSessionBeanLocal;
import session.stateless.SupermarketSessionBeanLocal;
import ws.datamodel.ErrorRsp;
import ws.datamodel.FilterItemsListReq;
import ws.datamodel.FilterItemsListRsp;
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

    @Path("retrieveSortedItems")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterItemsList(FilterItemsListReq filterItemsListReq) {

        if (filterItemsListReq != null) {

            try {
                Supermarket s = supermarketSessionBeanLocal.retrieveSupermarketById(filterItemsListReq.getSupermarketId());

                List<Item> result = generateShortestPath(mapStringToArray(s.getMap(),s.getDimensionX(),s.getDimensionY()), filterItemsListReq.getItems());

                return Response.status(Response.Status.OK).entity(new FilterItemsListRsp(result)).build();
            } catch (Exception ex) {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorRsp("Failed to filter shopping list.")).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorRsp("Supplied null value.")).build();
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

    private List<Item> generateShortestPath(int[][] map, List<Item> items) {
        //to be removed at rws
        List<Item> processedList = new ArrayList<>();

        addElements(map, items);
        printMap(map);

        Queue<String> queue = new LinkedList<>();

        outerloop:
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == 2) {
                    queue.add(x + "," + y);
                    break outerloop;
                }
            }
        }

        while (processedList.size() < items.size()) {

//            System.out.println("Added items: " + processedList.size());
//            processedList.forEach(x -> System.out.println(x));
//            printMap(map);
            String newStartNode = "";

            while (queue.isEmpty() == false) {
                //my curr location.
                String x = queue.remove();
                int row = Integer.parseInt(x.split(",")[0]);
                int col = Integer.parseInt(x.split(",")[1]);
                //look for next number that is above > 5;
                //check top boundary
                if (row - 1 >= 0) {

                    if (map[row - 1][col] > 5) { //found item

                        for (Item i : items) { //adding item from input list into output list.
                            if (i.getItemId() + 5 == map[row - 1][col]) {
                                processedList.add(i);
                                break;
                            }
                        }
                        map[row - 1][col] = 0; //mark path as no item.

                        newStartNode = (row - 1) + "," + col; //new start node.
                        queue = new LinkedList<>();
                        break;
                    } else if (map[row - 1][col] == 0) {
                        queue.add((row - 1) + "," + col);
                    }
                }

                //check buttom boundary
                if (row + 1 < map.length) {

                    if (map[row + 1][col] > 5) { //found item

                        for (Item i : items) { //adding item from input list into output list.
                            if (i.getItemId() + 5 == map[row + 1][col]) {
                                processedList.add(i);
                                break;
                            }
                        }
                        map[row + 1][col] = 0; //mark path as no item.

                        newStartNode = (row + 1) + "," + col; //new start node.
                        queue = new LinkedList<>();
                        break;
                    } else if (map[row + 1][col] == 0) {
                        queue.add((row + 1) + "," + col);
                    }

                }

                //check left boundary
                if (col - 1 >= 0) {

                    if (map[row][col - 1] > 5) { //found item

                        for (Item i : items) { //adding item from input list into output list.
                            if (i.getItemId() + 5 == map[row][col - 1]) {
                                processedList.add(i);
                                break;
                            }
                        }
                        map[row][col - 1] = 0; //mark path as no item.

                        newStartNode = (row) + "," + (col - 1); //new start node.
                        queue = new LinkedList<>();
                        break;
                    } else if (map[row][col - 1] == 0) {
                        queue.add((row) + "," + (col - 1));
                    }

                }

                //check right boundary
                if (col + 1 < map[0].length) {

                    if (map[row][col + 1] > 5) { //found item

                        for (Item i : items) { //adding item from input list into output list.
                            if (i.getItemId() + 5 == map[row][col + 1]) {
                                processedList.add(i);
                                break;
                            }
                        }
                        map[row][col + 1] = 0; //mark path as no item.

                        newStartNode = (row) + "," + (col + 1); //new start node.
                        queue = new LinkedList<>();
                        break;
                    } else if (map[row][col + 1] == 0) {
                        queue.add((row) + "," + (col + 1));
                    }
                }
            }

            if (newStartNode.isEmpty() || newStartNode.equals("")) {
                break;
            }
            System.out.println("New start node: " + newStartNode);
            queue.add(newStartNode);

        }

        return processedList;

    }

    private void addElements(int[][] map, List<Item> items) {
        for (Item i : items) {
            System.out.println("Item: " + i.getItemName());
            System.out.println("X: " + i.getPosX());
            System.out.println("Y: " + i.getPosY());
            if (map[i.getPosY()][i.getPosX()] == 1) {
                System.out.println("ERROR.");
            }
            map[i.getPosY()][i.getPosX()] = i.getItemId().intValue() + 5;

        }
    }

    private void printMap(int[][] map) {
        String mapString = "\n";
        for (int x = 0; x < map.length; x++) {
            String line = "";
            for (int y = 0; y < map[x].length; y++) {

                if (map[x][y] < 10) {
                    line += " " + map[x][y];
                } else {

                    line += map[x][y];
                }

            }
            mapString += line + "\n";
        }
        System.out.println(mapString);
    }

    private int[][] mapStringToArray(String map, int dimensionX, int dimensionY) {

        int[][] mapArr = new int[dimensionY][dimensionX];

        int x = 0;
        int y = 0;

        for (char ch : map.toCharArray()) {
            mapArr[y][x] = ch - 48;
            x++;
            if (x == dimensionX) {
                x = 0;
                y++;
            }
        }

        return mapArr;
    }
}
