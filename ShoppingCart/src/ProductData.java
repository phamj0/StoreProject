import java.util.ArrayList;
import java.util.List;

public class ProductData {
    private static final List<Product> products = new ArrayList<>();

    public static void addProduct(Product product) {
        products.add(product);
    }

    public static List<Product> getProducts() {
        return new ArrayList<>(products); // Return a copy to avoid modification
    }
}
