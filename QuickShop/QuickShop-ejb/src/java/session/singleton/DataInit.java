/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.singleton;

import entity.Category;
import entity.Item;
import entity.Supermarket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import session.stateless.CategorySessionBeanLocal;
import session.stateless.ItemSessionBeanLocal;
import session.stateless.SupermarketSessionBeanLocal;

/**
 *
 * @author User
 */
@Singleton
@LocalBean
@Startup

public class DataInit {

    @PersistenceContext(unitName = "QuickShop-ejbPU")
    private EntityManager em;

    @EJB
    ItemSessionBeanLocal itemSessionBeanLocal;
    @EJB
    SupermarketSessionBeanLocal supermarketSessionBeanLocal;
    @EJB
    CategorySessionBeanLocal categorySessionbeanLocal;

    @PostConstruct
    public void postConstruct() {

        Supermarket s = supermarketSessionBeanLocal.retrieveSupermarketById(1L);
        if (s == null) {
            initializeData();
        } else {

            System.out.println("Supermarket Mapping 1:");
            int[][] map = mapStringToArray(s.getMap(), s.getDimensionX(), s.getDimensionY());
            
            Supermarket s2 = supermarketSessionBeanLocal.retrieveSupermarketById(2L);
            System.out.println("Supermarket Mapping 2:");
            int[][] map2 = mapStringToArray(s2.getMap(), s2.getDimensionX(), s2.getDimensionY());
            

            Supermarket s3 = supermarketSessionBeanLocal.retrieveSupermarketById(3L);
            System.out.println("Supermarket Mapping 3:");
            int[][] map3 = mapStringToArray(s3.getMap(), s3.getDimensionX(), s3.getDimensionY());
            

            Supermarket s4 = supermarketSessionBeanLocal.retrieveSupermarketById(4L);
            System.out.println("Supermarket Mapping 4:");
            int[][] map4 = mapStringToArray(s4.getMap(), s4.getDimensionX(), s4.getDimensionY());
          
            List<Item> processed = generateShortestPath(map, itemSessionBeanLocal.retrieveAllItemsBySupermarketId(s.getSupermarketId()));
            processed.forEach(x -> System.out.println(x.getItemName()));

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

            System.out.println("Added items: " + processedList.size());
            processedList.forEach(x -> System.out.println(x));
            printMap(map);

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

    private void initializeData() {
        //20x11
        String map1 = "1111111111111111111110000000000000000001101111111110101111011000000000001000000110111111111010111101100000000000100000011011111111001111100110000000010000000001101111111111111100011000000001000000000113111111111111111121";
        //30x14
        String map2 = "111111111111111111111111111111100000000000000000000000000001101111110111111111011111111101101000010000000000000000000001101010010111111111011111111101100010010000000000000000000001110010010111111111011111111101110010010000000000000000000001110010010111111111011111111101110010010000000000000000000001100010010111111111011111111101101110000000000000000000000001100000010000000000000000000001111121113111111111111111111111";
        //30x21
        String map3 = "111111111111111111111111111111110000000000000000000000000000111000000000000000000000000000011101111111111111111000000000001110000000000000000000000000000021000000000000000000000000000011101111111111111111000000000001110000000000000000000000000000111000000000000000000000000000011101111111111111111000000000001110000000000000000000000000000111000000000000000000000000000011101111111111111111000000000000310000000000000000000000000000111000000000000000000000000000011101111111111111111000000000001110000000000000000000000000000111000000000000000000000000000011101111111111111111000000000001110000000000000000000000000000111111111111111111111111111111111";
        //30x15
        String map4 = "111111111111111111111111111111100000000000000000000000000001101010101010101010101010111111101010101010101010101010111111101010101010101010101010111111101010101010101010101010000002101000101010101010101010000011101000000000000000000000000011101000101010101010101010000011101000101010101010101010000003101010101010101010101010111111101010101010101010101010111111101010101010101010101010111111100000000000000000000000000001111111111111111111111111111111";

        Long sid1 = supermarketSessionBeanLocal.createSupermarket(new Supermarket("NTUC(Jurong)", map1, 20, 11));
        Long sid2 = supermarketSessionBeanLocal.createSupermarket(new Supermarket("NTUC(Pasir Ris)", map2, 30, 14));
        Long sid3 = supermarketSessionBeanLocal.createSupermarket(new Supermarket("ColdStorage(Serangoon-NEX)", map3, 31, 21));
        Long sid4 = supermarketSessionBeanLocal.createSupermarket(new Supermarket("ShengSiong(Clementi)", map4, 30, 15));

        Long id1 = categorySessionbeanLocal.createCategory(new Category("Frozen"));
        Long id2 = categorySessionbeanLocal.createCategory(new Category("Drinks"));
        Long id3 = categorySessionbeanLocal.createCategory(new Category("Dairy & Eggs"));
        Long id4 = categorySessionbeanLocal.createCategory(new Category("Fruits & Vegetables"));
        Long id5 = categorySessionbeanLocal.createCategory(new Category("Meat & Seafood"));
        Long id6 = categorySessionbeanLocal.createCategory(new Category("Rice & Noodles"));
        Long id7 = categorySessionbeanLocal.createCategory(new Category("Baking & Cooking Ingredients"));
        Long id8 = categorySessionbeanLocal.createCategory(new Category("Baby, Child & Toys"));
        Long id9 = categorySessionbeanLocal.createCategory(new Category("Food Cupboard"));
        Long id10 = categorySessionbeanLocal.createCategory(new Category("Health & Beauty"));

        Item i1 = new Item("Magnum Mini Ice Cream - Classic, Almond & White", "6 x 60ml", 10.90, "Magnum.jpg", 4, 1); //Frozen - id1
        Item i2 = new Item("Haagen-Dazs Ice Cream - Salted Caramel", "473ml", 14.50, "Haagen.jpg", 3, 1); //Frozen - id1
        Item i3 = new Item("Ben & Jerry's Ice Cream - Chocolate Fudge Brownie", "458ml", 13.90, "BenAndJerry.jpg", 2, 1); //Frozen - id1   

        Item i4 = new Item("Pokka Packet Drink - Jasmine Green Tea", "24 x 250ml", 10.20, "Pokke.jpg", 2, 5); //Drinks - id2
        Item i5 = new Item("100 Plus Isotonic Bottle Drink - Original", "1.5L", 1.90, "100Plus.jpg", 3, 5); //Drinks - id2
        Item i6 = new Item("Coca-Cola Can Drink - Classic", "30 x 320ml", 17.60, "Cola.jpg", 4, 5); //Drinks - id2 

        Item i7 = new Item("SCS Pure Creamery Butter Block - Salted", "250g", 4.95, "Butter.jpg", 16, 7); //Dairy Egg - id3
        Item i8 = new Item("Pasar Fresh Eggs", "30 eggs", 4.90, "Eggs.jpg", 17, 7); //Dairy Egg - id3
        Item i9 = new Item("Pasar Fresh Eggs", "10 eggs", 2.35, "Eggs2.jpg", 18, 7); //Dairy Egg - id3

        Item i10 = new Item("South Africa Candy Apple", "500g", 1.65, "Apple.jpg", 14, 5); //Fruits & Veg - id3
        Item i11 = new Item("Zespri New Zealand Kiwifruit - SunGold (Organic)", "600g", 6.95, "Kiwi.jpg", 15, 5); //Fruits & Veg - id3
        Item i12 = new Item("Holland Potato (China)", "1kg", 1.85, "Potato.jpg", 16, 5); //Fruits & Veg - id3

        Item i13 = new Item("CP Tsukune Chicken Meatball with Teriyaki Sauce", "400g", 5.90, "Meatball.jpg", 17, 3); //Meat - id4
        Item i14 = new Item("Kee Song Fresh Chicken - Boneless Breast", "300g", 3.25, "Chicken.jpg", 16, 3); //Meat - id4
        Item i15 = new Item("Kee Song Fresh Chicken - Whole", "1.4kg", 7.45, "Chicken2.jpg", 15, 3); //Meat - id4

        Item i16 = new Item("Royal Umbrella Thai Hom Mali Rice", "5kg", 16.20, "RoyalUmbrella.jpg", 11, 4); //Rice & Noodles - id6
        Item i17 = new Item("SongHe AAA Thai Hom Mali Rice", "5kg", 16.90, "SongHe.jpg", 11, 5); //Rice & Noodles - id6
        Item i18 = new Item("Suma Organic Red Quinoa", "500g", 12.90, "SumaOrganic.jpg", 11, 6); //Rice & Noodles - id6

        Item i19 = new Item("Erawan Elephant Rice Flour", "600g", 1.60, "ErawanElephant.jpg", 11, 3); //Baking & Cooking Ingredients - id7
        Item i20 = new Item("Hollyfarms Chocolate Mix", "100g", 5.35, "ChocolateMix.jpg", 11, 2); //Baking & Cooking Ingredients - id7
        Item i21 = new Item("Ayam Brand Coconut Milk - Organic Light", "400ml", 3.30, "CoconutMilk.jpg", 12, 1); //Baking & Cooking Ingredients - id7

        Item i22 = new Item("Pigeon Baby Wet Wipes", "6 x 80 per pack", 17.50, "BabyWipes.jpg", 2, 7); //Baby, Child & Toys - id8
        Item i23 = new Item("Aptamil Growing Up Milk Formula", "900g", 34.50, "milk.jpg", 3, 7); //Baby, Child & Toys - id8
        Item i24 = new Item("Johnson's Active Kids Shampoo", "800ml", 12.00, "shampoo.jpg", 4, 7); //Baby, Child & Toys - id8

        Item i25 = new Item("Pringles Potato Crisps - Sour Cream & Onion", "147g", 2.70, "chips.jpg", 7, 5); //Food Cupboard - id9
        Item i26 = new Item("Oreo Cookie Sandwich Biscuit - Original", "9 x 29.4g", 1.95, "oreo.jpg", 8, 5); //Food Cupboard - id9
        Item i27 = new Item("Kellogg's Cereal", "500g", 3.75, "cereal.jpg", 9, 5); //Food Cupboard - id9

        Item i28 = new Item("Gillette Skinguard Razor", "1 per pack", 13.90, "gillette.jpg", 7, 3); //Health & Beauty - id10
        Item i29 = new Item("Colgate Total Toothpaste", "4 x 150g", 14.95, "colgate.jpg", 8, 3); //Health & Beauty - id10
        Item i30 = new Item("Pantene Pro-V Shampoo", "750ml", 9.95, "patene.jpg", 9, 3); //Health & Beauty - id10

        itemSessionBeanLocal.createNewItem(i1, id1, sid1);
        itemSessionBeanLocal.createNewItem(i2, id1, sid1);
        itemSessionBeanLocal.createNewItem(i3, id1, sid1);
        itemSessionBeanLocal.createNewItem(i4, id2, sid1);
        itemSessionBeanLocal.createNewItem(i5, id2, sid1);
        itemSessionBeanLocal.createNewItem(i6, id2, sid1);
        itemSessionBeanLocal.createNewItem(i7, id3, sid1);
        itemSessionBeanLocal.createNewItem(i8, id3, sid1);
        itemSessionBeanLocal.createNewItem(i9, id3, sid1);
        itemSessionBeanLocal.createNewItem(i10, id4, sid1);
        itemSessionBeanLocal.createNewItem(i11, id4, sid1);
        itemSessionBeanLocal.createNewItem(i12, id4, sid1);
        itemSessionBeanLocal.createNewItem(i13, id5, sid1);
        itemSessionBeanLocal.createNewItem(i14, id5, sid1);
        itemSessionBeanLocal.createNewItem(i15, id5, sid1);
        itemSessionBeanLocal.createNewItem(i16, id6, sid1);
        itemSessionBeanLocal.createNewItem(i17, id6, sid1);
        itemSessionBeanLocal.createNewItem(i18, id6, sid1);
        itemSessionBeanLocal.createNewItem(i19, id7, sid1);
        itemSessionBeanLocal.createNewItem(i20, id7, sid1);
        itemSessionBeanLocal.createNewItem(i21, id7, sid1);
        itemSessionBeanLocal.createNewItem(i22, id8, sid1);
        itemSessionBeanLocal.createNewItem(i23, id8, sid1);
        itemSessionBeanLocal.createNewItem(i24, id8, sid1);
        itemSessionBeanLocal.createNewItem(i25, id9, sid1);
        itemSessionBeanLocal.createNewItem(i26, id9, sid1);
        itemSessionBeanLocal.createNewItem(i27, id9, sid1);
        itemSessionBeanLocal.createNewItem(i28, id10, sid1);
        itemSessionBeanLocal.createNewItem(i29, id10, sid1);
        itemSessionBeanLocal.createNewItem(i30, id10, sid1);
//============================================================================/

        i1 = new Item("Magnum Mini Ice Cream - Classic, Almond & White", "6 x 60ml", 10.90, "Magnum.jpg", 3, 1); //Frozen - id1
        i2 = new Item("Haagen-Dazs Ice Cream - Salted Caramel", "473ml", 14.50, "Haagen.jpg", 4, 1); //Frozen - id1
        i3 = new Item("Ben & Jerry's Ice Cream - Chocolate Fudge Brownie", "458ml", 13.90, "BenAndJerry.jpg", 5, 1); //Frozen - id1

        i4 = new Item("Pokka Packet Drink - Jasmine Green Tea", "24 x 250ml", 10.20, "Pokke.jpg", 12, 11); //Drinks - id2
        i5 = new Item("100 Plus Isotonic Bottle Drink - Original", "1.5L", 1.90, "100Plus.jpg", 13, 11); //Drinks - id2
        i6 = new Item("Coca-Cola Can Drink - Classic", "30 x 320ml", 17.60, "Cola.jpg", 14, 11); //Drinks - id2 

        i7 = new Item("SCS Pure Creamery Butter Block - Salted", "250g", 4.95, "Butter.jpg", 22, 1); //Dairy Egg - id3
        i8 = new Item("Pasar Fresh Eggs", "30 eggs", 4.90, "Eggs.jpg", 23, 1); //Dairy Egg - id3
        i9 = new Item("Pasar Fresh Eggs", "10 eggs", 2.35, "Eggs2.jpg", 24, 1); //Dairy Egg - id3

        i10 = new Item("South Africa Candy Apple", "500g", 1.65, "Apple.jpg", 12, 3); //Fruits & Veg - id3
        i11 = new Item("Zespri New Zealand Kiwifruit - SunGold (Organic)", "600g", 6.95, "Kiwi.jpg", 13, 3); //Fruits & Veg - id3
        i12 = new Item("Holland Potato (China)", "1kg", 1.85, "Potato.jpg", 14, 3); //Fruits & Veg - id3

        i13 = new Item("CP Tsukune Chicken Meatball with Teriyaki Sauce", "400g", 5.90, "Meatball.jpg", 22, 3); //Meat - id4
        i14 = new Item("Kee Song Fresh Chicken - Boneless Breast", "300g", 3.25, "Chicken.jpg", 23, 3); //Meat - id4
        i15 = new Item("Kee Song Fresh Chicken - Whole", "1.4kg", 7.45, "Chicken2.jpg", 24, 3); //Meat - id4

        i16 = new Item("Royal Umbrella Thai Hom Mali Rice", "5kg", 16.20, "RoyalUmbrella.jpg", 12, 5); //Rice & Noodles - id6
        i17 = new Item("SongHe AAA Thai Hom Mali Rice", "5kg", 16.90, "SongHe.jpg", 13, 5); //Rice & Noodles - id6
        i18 = new Item("Suma Organic Red Quinoa", "500g", 12.90, "SumaOrganic.jpg", 14, 5); //Rice & Noodles - id6

        i19 = new Item("Erawan Elephant Rice Flour", "600g", 1.60, "ErawanElephant.jpg", 22, 5); //Baking & Cooking Ingredients - id7
        i20 = new Item("Hollyfarms Chocolate Mix", "100g", 5.35, "ChocolateMix.jpg", 23, 5); //Baking & Cooking Ingredients - id7
        i21 = new Item("Ayam Brand Coconut Milk - Organic Light", "400ml", 3.30, "CoconutMilk.jpg", 24, 5); //Baking & Cooking Ingredients - id7

        i22 = new Item("Pigeon Baby Wet Wipes", "6 x 80 per pack", 17.50, "BabyWipes.jpg", 12, 7); //Baby, Child & Toys - id8
        i23 = new Item("Aptamil Growing Up Milk Formula", "900g", 34.50, "milk.jpg", 13, 7); //Baby, Child & Toys - id8
        i24 = new Item("Johnson's Active Kids Shampoo", "800ml", 12.00, "shampoo.jpg", 14, 7); //Baby, Child & Toys - id8

        i25 = new Item("Pringles Potato Crisps - Sour Cream & Onion", "147g", 2.70, "chips.jpg", 22, 7); //Food Cupboard - id9
        i26 = new Item("Oreo Cookie Sandwich Biscuit - Original", "9 x 29.4g", 1.95, "oreo.jpg", 23, 7); //Food Cupboard - id9
        i27 = new Item("Kellogg's Cereal", "500g", 3.75, "cereal.jpg", 24, 7); //Food Cupboard - id9

        i28 = new Item("Gillette Skinguard Razor", "1 per pack", 13.90, "gillette.jpg", 5, 8); //Health & Beauty - id10
        i29 = new Item("Colgate Total Toothpaste", "4 x 150g", 14.95, "colgate.jpg", 5, 7); //Health & Beauty - id10
        i30 = new Item("Pantene Pro-V Shampoo", "750ml", 9.95, "patene.jpg", 5, 6); //Health & Beauty - id10

        itemSessionBeanLocal.createNewItem(i1, id1, sid2);
        itemSessionBeanLocal.createNewItem(i2, id1, sid2);
        itemSessionBeanLocal.createNewItem(i3, id1, sid2);
        itemSessionBeanLocal.createNewItem(i4, id2, sid2);
        itemSessionBeanLocal.createNewItem(i5, id2, sid2);
        itemSessionBeanLocal.createNewItem(i6, id2, sid2);
        itemSessionBeanLocal.createNewItem(i7, id3, sid2);
        itemSessionBeanLocal.createNewItem(i8, id3, sid2);
        itemSessionBeanLocal.createNewItem(i9, id3, sid2);
        itemSessionBeanLocal.createNewItem(i10, id4, sid2);
        itemSessionBeanLocal.createNewItem(i11, id4, sid2);
        itemSessionBeanLocal.createNewItem(i12, id4, sid2);
        itemSessionBeanLocal.createNewItem(i13, id5, sid2);
        itemSessionBeanLocal.createNewItem(i14, id5, sid2);
        itemSessionBeanLocal.createNewItem(i15, id5, sid2);
        itemSessionBeanLocal.createNewItem(i16, id6, sid2);
        itemSessionBeanLocal.createNewItem(i17, id6, sid2);
        itemSessionBeanLocal.createNewItem(i18, id6, sid2);
        itemSessionBeanLocal.createNewItem(i19, id7, sid2);
        itemSessionBeanLocal.createNewItem(i20, id7, sid2);
        itemSessionBeanLocal.createNewItem(i21, id7, sid2);
        itemSessionBeanLocal.createNewItem(i22, id8, sid2);
        itemSessionBeanLocal.createNewItem(i23, id8, sid2);
        itemSessionBeanLocal.createNewItem(i24, id8, sid2);
        itemSessionBeanLocal.createNewItem(i25, id9, sid2);
        itemSessionBeanLocal.createNewItem(i26, id9, sid2);
        itemSessionBeanLocal.createNewItem(i27, id9, sid2);
        itemSessionBeanLocal.createNewItem(i28, id10, sid2);
        itemSessionBeanLocal.createNewItem(i29, id10, sid2);
        itemSessionBeanLocal.createNewItem(i30, id10, sid2);
        //============================================================================/

        i1 = new Item("Magnum Mini Ice Cream - Classic, Almond & White", "6 x 60ml", 10.90, "Magnum.jpg", 1, 1); //Frozen - id1
        i2 = new Item("Haagen-Dazs Ice Cream - Salted Caramel", "473ml", 14.50, "Haagen.jpg", 2, 1); //Frozen - id1
        i3 = new Item("Ben & Jerry's Ice Cream - Chocolate Fudge Brownie", "458ml", 13.90, "BenAndJerry.jpg", 3, 1); //Frozen - id1   
        i4 = new Item("Pokka Packet Drink - Jasmine Green Tea", "24 x 250ml", 10.20, "Pokke.jpg", 7, 1); //Drinks - id2
        i5 = new Item("100 Plus Isotonic Bottle Drink - Original", "1.5L", 1.90, "100Plus.jpg", 8, 1); //Drinks - id2
        i6 = new Item("Coca-Cola Can Drink - Classic", "30 x 320ml", 17.60, "Cola.jpg", 9, 1); //Drinks - id2 
        i7 = new Item("SCS Pure Creamery Butter Block - Salted", "250g", 4.95, "Butter.jpg", 14, 1); //Dairy Egg - id3
        i8 = new Item("Pasar Fresh Eggs", "30 eggs", 4.90, "Eggs.jpg", 15, 1); //Dairy Egg - id3
        i9 = new Item("Pasar Fresh Eggs", "10 eggs", 2.35, "Eggs2.jpg", 16, 1); //Dairy Egg - id3
        i10 = new Item("South Africa Candy Apple", "500g", 1.65, "Apple.jpg", 21, 3); //Fruits & Veg - id3
        i11 = new Item("Zespri New Zealand Kiwifruit - SunGold (Organic)", "600g", 6.95, "Kiwi.jpg", 22, 3); //Fruits & Veg - id3
        i12 = new Item("Holland Potato (China)", "1kg", 1.85, "Potato.jpg", 23, 3); //Fruits & Veg - id3
        i13 = new Item("CP Tsukune Chicken Meatball with Teriyaki Sauce", "400g", 5.90, "Meatball.jpg", 2, 4); //Meat - id4
        i14 = new Item("Kee Song Fresh Chicken - Boneless Breast", "300g", 3.25, "Chicken.jpg", 3, 4); //Meat - id4
        i15 = new Item("Kee Song Fresh Chicken - Whole", "1.4kg", 7.45, "Chicken2.jpg", 4, 4); //Meat - id4
        i16 = new Item("Royal Umbrella Thai Hom Mali Rice", "5kg", 16.20, "RoyalUmbrella.jpg", 15, 4); //Rice & Noodles - id6
        i17 = new Item("SongHe AAA Thai Hom Mali Rice", "5kg", 16.90, "SongHe.jpg", 16, 4); //Rice & Noodles - id6
        i18 = new Item("Suma Organic Red Quinoa", "500g", 12.90, "SumaOrganic.jpg", 17, 4); //Rice & Noodles - id6
        i19 = new Item("Erawan Elephant Rice Flour", "600g", 1.60, "ErawanElephant.jpg", 2, 11); //Baking & Cooking Ingredients - id7
        i20 = new Item("Hollyfarms Chocolate Mix", "100g", 5.35, "ChocolateMix.jpg", 3, 11); //Baking & Cooking Ingredients - id7
        i21 = new Item("Ayam Brand Coconut Milk - Organic Light", "400ml", 3.30, "CoconutMilk.jpg", 4, 11); //Baking & Cooking Ingredients - id7
        i22 = new Item("Pigeon Baby Wet Wipes", "6 x 80 per pack", 17.50, "BabyWipes.jpg", 10, 13); //Baby, Child & Toys - id8
        i23 = new Item("Aptamil Growing Up Milk Formula", "900g", 34.50, "milk.jpg", 11, 13); //Baby, Child & Toys - id8
        i24 = new Item("Johnson's Active Kids Shampoo", "800ml", 12.00, "shampoo.jpg", 12, 13); //Baby, Child & Toys - id8
        i25 = new Item("Pringles Potato Crisps - Sour Cream & Onion", "147g", 2.70, "chips.jpg", 5, 16); //Food Cupboard - id9
        i26 = new Item("Oreo Cookie Sandwich Biscuit - Original", "9 x 29.4g", 1.95, "oreo.jpg", 6, 16); //Food Cupboard - id9
        i27 = new Item("Kellogg's Cereal", "500g", 3.75, "cereal.jpg", 7, 16); //Food Cupboard - id9
        i28 = new Item("Gillette Skinguard Razor", "1 per pack", 13.90, "gillette.jpg", 6, 19); //Health & Beauty - id10
        i29 = new Item("Colgate Total Toothpaste", "4 x 150g", 14.95, "colgate.jpg", 7, 19); //Health & Beauty - id10
        i30 = new Item("Pantene Pro-V Shampoo", "750ml", 9.95, "patene.jpg", 8, 19); //Health & Beauty - id10

        itemSessionBeanLocal.createNewItem(i1, id1, sid3);
        itemSessionBeanLocal.createNewItem(i2, id1, sid3);
        itemSessionBeanLocal.createNewItem(i3, id1, sid3);
        itemSessionBeanLocal.createNewItem(i4, id2, sid3);
        itemSessionBeanLocal.createNewItem(i5, id2, sid3);
        itemSessionBeanLocal.createNewItem(i6, id2, sid3);
        itemSessionBeanLocal.createNewItem(i7, id3, sid3);
        itemSessionBeanLocal.createNewItem(i8, id3, sid3);
        itemSessionBeanLocal.createNewItem(i9, id3, sid3);
        itemSessionBeanLocal.createNewItem(i10, id4, sid3);
        itemSessionBeanLocal.createNewItem(i11, id4, sid3);
        itemSessionBeanLocal.createNewItem(i12, id4, sid3);
        itemSessionBeanLocal.createNewItem(i13, id5, sid3);
        itemSessionBeanLocal.createNewItem(i14, id5, sid3);
        itemSessionBeanLocal.createNewItem(i15, id5, sid3);
        itemSessionBeanLocal.createNewItem(i16, id6, sid3);
        itemSessionBeanLocal.createNewItem(i17, id6, sid3);
        itemSessionBeanLocal.createNewItem(i18, id6, sid3);
        itemSessionBeanLocal.createNewItem(i19, id7, sid3);
        itemSessionBeanLocal.createNewItem(i20, id7, sid3);
        itemSessionBeanLocal.createNewItem(i21, id7, sid3);
        itemSessionBeanLocal.createNewItem(i22, id8, sid3);
        itemSessionBeanLocal.createNewItem(i23, id8, sid3);
        itemSessionBeanLocal.createNewItem(i24, id8, sid3);
        itemSessionBeanLocal.createNewItem(i25, id9, sid3);
        itemSessionBeanLocal.createNewItem(i26, id9, sid3);
        itemSessionBeanLocal.createNewItem(i27, id9, sid3);
        itemSessionBeanLocal.createNewItem(i28, id10, sid3);
        itemSessionBeanLocal.createNewItem(i29, id10, sid3);
        itemSessionBeanLocal.createNewItem(i30, id10, sid3);
        //============================================================================/

        i1 = new Item("Magnum Mini Ice Cream - Classic, Almond & White", "6 x 60ml", 10.90, "Magnum.jpg", 1, 3); //Frozen - id1
        i2 = new Item("Haagen-Dazs Ice Cream - Salted Caramel", "473ml", 14.50, "Haagen.jpg", 1, 4); //Frozen - id1
        i3 = new Item("Ben & Jerry's Ice Cream - Chocolate Fudge Brownie", "458ml", 13.90, "BenAndJerry.jpg", 1, 5); //Frozen - id1   
        i4 = new Item("Pokka Packet Drink - Jasmine Green Tea", "24 x 250ml", 10.20, "Pokke.jpg", 1, 9); //Drinks - id2
        i5 = new Item("100 Plus Isotonic Bottle Drink - Original", "1.5L", 1.90, "100Plus.jpg", 1, 10); //Drinks - id2
        i6 = new Item("Coca-Cola Can Drink - Classic", "30 x 320ml", 17.60, "Cola.jpg", 1, 11); //Drinks - id2 
        i7 = new Item("SCS Pure Creamery Butter Block - Salted", "250g", 4.95, "Butter.jpg", 5, 3); //Dairy Egg - id3
        i8 = new Item("Pasar Fresh Eggs", "30 eggs", 4.90, "Eggs.jpg", 5, 4); //Dairy Egg - id3
        i9 = new Item("Pasar Fresh Eggs", "10 eggs", 2.35, "Eggs2.jpg", 5, 5); //Dairy Egg - id3
        i10 = new Item("South Africa Candy Apple", "500g", 1.65, "Apple.jpg", 23, 3); //Fruits & Veg - id3
        i11 = new Item("Zespri New Zealand Kiwifruit - SunGold (Organic)", "600g", 6.95, "Kiwi.jpg", 23, 4); //Fruits & Veg - id3
        i12 = new Item("Holland Potato (China)", "1kg", 1.85, "Potato.jpg", 23, 5); //Fruits & Veg - id3
        i13 = new Item("CP Tsukune Chicken Meatball with Teriyaki Sauce", "400g", 5.90, "Meatball.jpg", 7, 9); //Meat - id4
        i14 = new Item("Kee Song Fresh Chicken - Boneless Breast", "300g", 3.25, "Chicken.jpg", 7, 10); //Meat - id4
        i15 = new Item("Kee Song Fresh Chicken - Whole", "1.4kg", 7.45, "Chicken2.jpg", 7, 11); //Meat - id4
        i16 = new Item("Royal Umbrella Thai Hom Mali Rice", "5kg", 16.20, "RoyalUmbrella.jpg", 9, 9); //Rice & Noodles - id6
        i17 = new Item("SongHe AAA Thai Hom Mali Rice", "5kg", 16.90, "SongHe.jpg", 9, 10); //Rice & Noodles - id6
        i18 = new Item("Suma Organic Red Quinoa", "500g", 12.90, "SumaOrganic.jpg", 9, 11); //Rice & Noodles - id6
        i19 = new Item("Erawan Elephant Rice Flour", "600g", 1.60, "ErawanElephant.jpg", 11, 9); //Baking & Cooking Ingredients - id7
        i20 = new Item("Hollyfarms Chocolate Mix", "100g", 5.35, "ChocolateMix.jpg", 11, 10); //Baking & Cooking Ingredients - id7
        i21 = new Item("Ayam Brand Coconut Milk - Organic Light", "400ml", 3.30, "CoconutMilk.jpg", 11, 11); //Baking & Cooking Ingredients - id7
        i22 = new Item("Pigeon Baby Wet Wipes", "6 x 80 per pack", 17.50, "BabyWipes.jpg", 13, 3); //Baby, Child & Toys - id8
        i23 = new Item("Aptamil Growing Up Milk Formula", "900g", 34.50, "milk.jpg", 13, 4); //Baby, Child & Toys - id8
        i24 = new Item("Johnson's Active Kids Shampoo", "800ml", 12.00, "shampoo.jpg", 13, 5); //Baby, Child & Toys - id8
        i25 = new Item("Pringles Potato Crisps - Sour Cream & Onion", "147g", 2.70, "chips.jpg", 11, 3); //Food Cupboard - id9
        i26 = new Item("Oreo Cookie Sandwich Biscuit - Original", "9 x 29.4g", 1.95, "oreo.jpg", 11, 4); //Food Cupboard - id9
        i27 = new Item("Kellogg's Cereal", "500g", 3.75, "cereal.jpg", 11, 5); //Food Cupboard - id9
        i28 = new Item("Gillette Skinguard Razor", "1 per pack", 13.90, "gillette.jpg", 17, 3); //Health & Beauty - id10
        i29 = new Item("Colgate Total Toothpaste", "4 x 150g", 14.95, "colgate.jpg", 17, 4); //Health & Beauty - id10
        i30 = new Item("Pantene Pro-V Shampoo", "750ml", 9.95, "patene.jpg", 17, 5); //Health & Beauty - id10

        itemSessionBeanLocal.createNewItem(i1, id1, sid4);
        itemSessionBeanLocal.createNewItem(i2, id1, sid4);
        itemSessionBeanLocal.createNewItem(i3, id1, sid4);
        itemSessionBeanLocal.createNewItem(i4, id2, sid4);
        itemSessionBeanLocal.createNewItem(i5, id2, sid4);
        itemSessionBeanLocal.createNewItem(i6, id2, sid4);
        itemSessionBeanLocal.createNewItem(i7, id3, sid4);
        itemSessionBeanLocal.createNewItem(i8, id3, sid4);
        itemSessionBeanLocal.createNewItem(i9, id3, sid4);
        itemSessionBeanLocal.createNewItem(i10, id4, sid4);
        itemSessionBeanLocal.createNewItem(i11, id4, sid4);
        itemSessionBeanLocal.createNewItem(i12, id4, sid4);
        itemSessionBeanLocal.createNewItem(i13, id5, sid4);
        itemSessionBeanLocal.createNewItem(i14, id5, sid4);
        itemSessionBeanLocal.createNewItem(i15, id5, sid4);
        itemSessionBeanLocal.createNewItem(i16, id6, sid4);
        itemSessionBeanLocal.createNewItem(i17, id6, sid4);
        itemSessionBeanLocal.createNewItem(i18, id6, sid4);
        itemSessionBeanLocal.createNewItem(i19, id7, sid4);
        itemSessionBeanLocal.createNewItem(i20, id7, sid4);
        itemSessionBeanLocal.createNewItem(i21, id7, sid4);
        itemSessionBeanLocal.createNewItem(i22, id8, sid4);
        itemSessionBeanLocal.createNewItem(i23, id8, sid4);
        itemSessionBeanLocal.createNewItem(i24, id8, sid4);
        itemSessionBeanLocal.createNewItem(i25, id9, sid4);
        itemSessionBeanLocal.createNewItem(i26, id9, sid4);
        itemSessionBeanLocal.createNewItem(i27, id9, sid4);
        itemSessionBeanLocal.createNewItem(i28, id10, sid4);
        itemSessionBeanLocal.createNewItem(i29, id10, sid4);
        itemSessionBeanLocal.createNewItem(i30, id10, sid4);

    }

}
