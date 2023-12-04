import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart {
    private Map<Product, Integer> products;

    public ShoppingCart() {
        products = new HashMap<>();
    }

    public void addProduct(Product product) {
        products.put(product, products.getOrDefault(product, 0) + 1);
    }

    public Set<Map.Entry<Product, Integer>> getProducts() {
        return products.entrySet();
    }

    public double getTotal() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

}
