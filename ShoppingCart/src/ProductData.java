import java.util.ArrayList;
import java.util.List;

public class ProductData {
    private static final List<Product> products = new ArrayList<>();

    static {
        // Initialize hardcoded products
        products.add(new Product("Water Bottle", 5.00, 10.00, "This is a 16oz water bottle", 10));
        products.add(new Product("Teddy Bear", 10.00, 70.00, "This is a light brown Teddy Bear", 5));
        products.add(new Product("Sunglasses", 5.00, 20.00, "This is a simple pair of black sunglasses", 20));
        products.add(new Product("Running Shoes", 50.00, 100.00, "This is a pair of Running Shoes", 10));
        products.add(new Product("Monitor", 200.00, 1000.00, "This is a simple 27 inch monitor", 3));
    }

    public static void addProduct(Product product) {
        products.add(product);
    }

    public static List<Product> getProducts() {
        return new ArrayList<>(products); // Return a copy to avoid modification
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
}
