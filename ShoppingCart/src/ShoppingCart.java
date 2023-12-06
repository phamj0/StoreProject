import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ShoppingCart class that represents a shopping cart that holds products and its quantities.
 */
public class ShoppingCart {
    private Map<Product, Integer> products;
    
    public ShoppingCart() {
        products = new HashMap<>();
    }

    /**
     * Adds a product to the shopping cart.
     * @param product  is the product to be added. Product can't be null
     * @return True    if the product was added successfully, false otherwise.
     * If the product is added successfully, the quantity in the cart is increased by 1.
     */
    public boolean addProduct(Product product) {
        int currentQuantity = products.getOrDefault(product, 0);
        if (currentQuantity < product.getQuantity()) {
            products.put(product, currentQuantity + 1);
            return true;
        }
        return false;
    }

    /**
     * Removes a product from the shopping cart.
     * @param product is the product to be removed. can't be null.
     */
    public void removeProduct(Product product) {
        int currentQuantity = products.getOrDefault(product, 0);
        if (currentQuantity > 1) {
            products.put(product, currentQuantity - 1);
        } else {
            products.remove(product);
        }
    }

    /**
     * Gets the set of products and quantities in the shopping cart.
     * @return the set of products and quantities.
     */
    public Set<Map.Entry<Product, Integer>> getProducts() {
        return products.entrySet();
    }

    /**
     * Calculation of the total cost of all the products in the shopping cart.
     * @return the total cost of all the products in the shopping cart.
     */
    public double getTotal() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    // Clears the shopping cart by removing all the products.
    public void clearCart() {
        products.clear();
    }
}
