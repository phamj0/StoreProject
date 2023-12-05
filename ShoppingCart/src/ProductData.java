import java.util.ArrayList;
import java.util.List;

public class ProductData {
    private static final List<Product> products = new ArrayList<>();

    static {
        // Initialize hardcoded products
        products.add(new Product("Water Bottle", 10.00, "This is a 16oz water bottle", 10));
        products.add(new Product("Teddy Bear", 70.00, "This is a light brown Teddy Bear", 5));
        products.add(new Product("Sunglasses", 20.00, "This is a simple pair of black sunglasses", 20));
        products.add(new Product("Running Shoes", 100.00, "This is a pair of Running Shoes", 10));
        products.add(new Product("Monitor", 1000.00, "This is a simple 27 inch monitor", 3));
    }

    public static void addProduct(Product product) {
        products.add(product);
    }

    public static List<Product> getProducts() {
        return new ArrayList<>(products); // Return a copy to avoid modification
    }
}
