import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart {
    private Map<Product, Integer> products;

    public ShoppingCart() {
        products = new HashMap<>();
    }

    public boolean addProduct(Product product) {
        int currentQuantity = products.getOrDefault(product, 0);
        if (currentQuantity < product.getQuantity()) {
            products.put(product, currentQuantity + 1);
            return true;
        }
        return false;
    }

    public void removeProduct(Product product) {
        int currentQuantity = products.getOrDefault(product, 0);
        if (currentQuantity > 1) {
            products.put(product, currentQuantity - 1);
        } else {
            products.remove(product);
        }
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
