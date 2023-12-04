import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart {
    private Map<Product, Integer> products;
    private static final int MAX_QUANTITY = 10; // Maximum quantity per product

    public ShoppingCart() {
        products = new HashMap<>();
    }

    public void addProduct(Product product) {
        int currentQuantity = products.getOrDefault(product, 0);
        if (currentQuantity < MAX_QUANTITY) {
            products.put(product, currentQuantity + 1);
        }
    }

    public void removeProduct(Product product) {
        int currentQuantity = products.getOrDefault(product, 0);
        if (currentQuantity > 1) {
            products.put(product, currentQuantity - 1);
        } else {
            products.remove(product);
        }
    }

    public boolean isMaxQuantity(Product product) {
        return products.getOrDefault(product, 0) == MAX_QUANTITY;
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
