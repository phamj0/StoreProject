/**
 * Product class for the application.
 */
public class Product {
    private String name;
    private double price;
    private String description;
    private int quantity;
    private double cost;

    /**
     * Product constructor that creates a new product with the specified attributes.
     * @param name        is the name of the product. Can't be null.
     * @param cost        is the cost of the product. Can't be negative.
     * @param price       is the selling price of the product. Can't be negative.
     * @param description is a description of the product. Can't be null
     * @param quantity    is the quantity of the product in stock. Can't be negative.
     */
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

    /**
     * Turns the product information into an array of objects for displaying purposes.
     * @return An array of objects that represent the products information.
     */
    public Object[] toRow() {
        return new Object[]{name, price, quantity};
    }
}
