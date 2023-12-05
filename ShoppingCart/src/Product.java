public class Product {
    private String name;
    private double price;
    private String description;
    private int quantity;
    private double cost;

    public Product(String name, double cost, double price, String description, int quantity) {
        this.name = name;
        this.cost = cost;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() { 
        return price; 
    }

    public void setPrice(double price) { 
        this.price = price; 
    }

    public int getQuantity() { 
        return quantity; 
    }

    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public Object[] toRow() {
        return new Object[]{name, price, quantity};
    }
}
