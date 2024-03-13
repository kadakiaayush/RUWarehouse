package warehouse;


public class Warehouse {
    private Sector[] sectors;

    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }

   
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

   
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        int index = id%10;
        Product p = new Product(id, name, stock, day, demand);
        sectors[index].add(p);
    }

    
    private void fixHeap(int id) {
        int index = id%10;
        if (sectors[index].getSize()!=1)
            sectors[index].swim(sectors[index].getSize());
    }

   
    private void evictIfNeeded(int id) {
        int index = id%10;
        if (sectors[index].getSize()==5){
            sectors[index].swap(1, 5);
            sectors[index].deleteLast();
            sectors[index].sink(1);
        }
    }

 
    public void restockProduct(int id, int amount) {
        int index = id%10;
        int size = sectors[index].getSize();
        for (int i = size; i>0; i--) {
            if (sectors[index].get(i).getId()==id){
                sectors[index].get(i).setStock(sectors[index].get(i).getStock()+amount);
                break;
            }
        }
    }

  
    public void deleteProduct(int id) {
        int index = id%10;
        int size = sectors[index].getSize();
        for (int i = size; i>0; i--){
            if (sectors[index].get(i).getId()==id){
                sectors[index].swap(i, size);
                sectors[index].deleteLast();
                sectors[index].sink(i);
            }
        }
    }

   
    public void purchaseProduct(int id, int day, int amount) {
        for (int i = sectors[id%10].getSize(); i>0; i--){
            if (sectors[id%10].get(i).getId()==id){
                if (sectors[id % 10].get(i).getStock() >= amount) {
                    sectors[id % 10].get(i).setStock(sectors[id % 10].get(i).getStock() - amount);
                    sectors[id % 10].get(i).setDemand(sectors[id % 10].get(i).getDemand() + amount);
                    sectors[id % 10].get(i).setLastPurchaseDay(day);
                    sectors[id % 10].sink(i);
                }
                break;
            }
        }
    }

   
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        int index=id%10;
        int size = sectors[index%10].getSize();
        do{
            int i = index%10;
            if (sectors[index%10].getSize()<5){
                sectors[i].add(new Product(id, name, stock, day, demand));
                sectors[i].swim(sectors[index%10].getSize());
                return;
            }
            index++;
        }while(index%10!=id%10);
        int ID = id;
        String NAME = name;
        int STOCK = stock;
        int DAY = day;
        int DEMAND = demand;
        addProduct(ID, NAME, STOCK, DAY, DEMAND);
    }

    
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }

        return warehouseString + "]";
    }

    public Sector[] getSectors () {
        return sectors;
    }
}