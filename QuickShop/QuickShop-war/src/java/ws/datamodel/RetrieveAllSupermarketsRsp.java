package ws.datamodel;

import entity.Supermarket;
import java.util.List;

public class RetrieveAllSupermarketsRsp {

    List<Supermarket> supermarkets;

    public RetrieveAllSupermarketsRsp() {
    }

    public RetrieveAllSupermarketsRsp(List<Supermarket> supermarkets) {
        this.supermarkets = supermarkets;
    }

    public List<Supermarket> getSupermarkets() {
        return supermarkets;
    }

    public void setSupermarkets(List<Supermarket> supermarkets) {
        this.supermarkets = supermarkets;
    }

}
