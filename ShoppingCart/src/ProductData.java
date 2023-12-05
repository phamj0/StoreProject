import java.util.ArrayList;
import java.util.List;

/**
 * Class that provides access to product data for the application.
 * Singleton pattern because there is a single instance of the product data for the entire application.
 */
public class ProductData {
    private static final List<Product> products = new ArrayList<>();
    static {
        products.add(new Product("Water Bottle", 5.00, 10.00, "This is a 16oz water bottle", 10));
        products.add(new Product("Teddy Bear", 10.00, 70.00, "This is a light brown Teddy Bear", 5));
        products.add(new Product("Sunglasses", 5.00, 20.00, "This is a simple pair of black sunglasses", 20));
        products.add(new Product("Running Shoes", 50.00, 100.00, "This is a pair of Running Shoes", 10));
        products.add(new Product("Monitor", 200.00, 1000.00, "This is a simple 27 inch monitor", 3));
    }

    /**
     * Add a new product to the product list.
     * Get a list of available products from the product list.
     * Update the quantity of a single product from the product list.
     * Products can't be null and the quantity must be positive to increase, and negative for decrease.
     */
    public static void addProduct(Product product) {
        products.add(product);
    }
    public static List<Product> getProducts() {
        return new ArrayList<>(products);
    }    
    public static void updateProductQuantity(Product product, int quantityChange) {
        for (Product p : products) {
            if (p.equals(product)) {
                int newQuantity = Math.max(p.getQuantity() + quantityChange, 0);
                p.setQuantity(newQuantity);
                break;
            }
        }
    }

    private static double totalRevenue = 0.0;
    private static int totalSales = 0;
    private static double totalProfit = 0.0;

    /**
     * A constructor that records a sale and updates the total revenue, total sales, and total profit.
     * @param product  is the product being sold. Can't be null.
     * @param quantity is the quantity sold. Needs to be greater than 0.
     */
    public static void recordSale(Product product, int quantity) {
        double revenueFromSale = product.getPrice() * quantity;
        double costOfSale = product.getCost() * quantity;
        totalRevenue += revenueFromSale;
        totalProfit += (revenueFromSale - costOfSale);
        totalSales += quantity;
    }
    public static double getTotalRevenue() {
        return totalRevenue;
    }
    public static int getTotalSales() {
        return totalSales;
    }
    public static double getTotalProfit() {
        return totalProfit;
    }
}
