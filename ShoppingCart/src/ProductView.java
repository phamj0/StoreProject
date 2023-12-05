import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ProductView extends JFrame {
    private List<Product> products;
    private JPanel productPanel;
    private ShoppingCart shoppingCart; 
    private JTextField searchField;
    private JButton cartButton;
    private JButton logoutButton;

    public ProductView() {
        setTitle("Product Catalog");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Timer refreshTimer = new Timer(5000, e -> refreshProducts());
        refreshTimer.start();

        shoppingCart = new ShoppingCart();
        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2));

        // Navigation bar
        JPanel navBar = new JPanel(new BorderLayout());
        JLabel storeName = new JLabel("My Store");
        storeName.setFont(new Font("Arial", Font.BOLD, 14));
        navBar.add(storeName, BorderLayout.WEST);

        searchField = new JTextField();
        navBar.add(searchField, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartButton = new JButton(new ImageIcon("ShoppingCart/Images/SC.png"));
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShoppingCart();
            }
        });
        rightPanel.add(cartButton);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(ProductView.this,
                        "Are you sure you want to log out?", "Confirm Logout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    dispose(); // Close the ProductView
                    LoginView loginView = new LoginView();
                    LoginController loginController = new LoginController(loginView); 
                    loginView.setVisible(true);
                }
            }
        });
        rightPanel.add(logoutButton);
        navBar.add(rightPanel, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        initializeProducts(); // Load products
        displayProducts(); // Display products

        add(new JScrollPane(productPanel), BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void refreshProducts() {
        List<Product> updatedProducts = ProductData.getProducts();
        // Create a map to efficiently check for existing products
        Map<String, Product> productMap = new HashMap<>();
        for (Product p : products) {
            productMap.put(p.getName(), p);
        }
        // Merge updated products, avoiding duplicates
        for (Product p : updatedProducts) {
            if (!productMap.containsKey(p.getName())) {
                products.add(p);
            }
        }
        displayProducts();
        revalidate();
        repaint();
    }


    private void initializeProducts() {
        products = ProductData.getProducts();
    }

    private void displayProducts() {
        productPanel.removeAll();
        for (Product product : products) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel(product.getName()));
            panel.add(new JLabel("$" + product.getPrice()));

            JButton detailsButton = new JButton("View Details");
            detailsButton.addActionListener(e -> showProductDetails(product));
            panel.add(detailsButton);

            JButton addToCartButton = new JButton("Add to Cart");
            addToCartButton.addActionListener(e -> {
                if (shoppingCart.addProduct(product)) {
                    JOptionPane.showMessageDialog(ProductView.this,
                            "Added to cart: " + product.getName());
                } else {
                    JOptionPane.showMessageDialog(ProductView.this,
                            "No more available", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                }
            });
            panel.add(addToCartButton);

            productPanel.add(panel);
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private void showProductDetails(Product product) {
        JDialog detailsDialog = new JDialog(this, "Product Details", true);
        detailsDialog.setLayout(new GridLayout(0, 1));
        detailsDialog.add(new JLabel("Name: " + product.getName()));
        detailsDialog.add(new JLabel("Price: $" + product.getPrice()));
        detailsDialog.add(new JLabel("Description: " + product.getDescription()));
        
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            if (shoppingCart.addProduct(product)) {
                JOptionPane.showMessageDialog(detailsDialog,
                        "Added to cart: " + product.getName());
            } else {
                JOptionPane.showMessageDialog(detailsDialog,
                        "No more available", "Stock Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        detailsDialog.add(addToCartButton);
        detailsDialog.setSize(300, 200);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }
    

    private void showShoppingCart() {
        JDialog cartDialog = new JDialog(this, "Shopping Cart", true);
        cartDialog.setLayout(new BorderLayout());

        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.add(new JLabel(product.getName() + " - $" + product.getPrice() + " x " + quantity));

            JButton addButton = new JButton("+");
            addButton.addActionListener(e -> {
                if (!shoppingCart.addProduct(product)) {
                    JOptionPane.showMessageDialog(cartDialog, "No more available", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                }
                cartDialog.dispose();
                showShoppingCart();
            });
            itemPanel.add(addButton);

            JButton subtractButton = new JButton("-");
            subtractButton.addActionListener(e -> {
                shoppingCart.removeProduct(product);
                cartDialog.dispose();
                showShoppingCart();
            });
            itemPanel.add(subtractButton);

            cartPanel.add(itemPanel);
        }

        cartDialog.add(new JScrollPane(cartPanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Total: $" + String.format("%.2f", shoppingCart.getTotal()));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalLabel);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            cartDialog.dispose();
            showCheckoutWindow();
        });
        bottomPanel.add(checkoutButton);

        cartDialog.add(new JScrollPane(cartPanel), BorderLayout.CENTER);
        cartDialog.add(bottomPanel, BorderLayout.SOUTH);

        cartDialog.setSize(400, 300);
        cartDialog.setLocationRelativeTo(this);
        cartDialog.setVisible(true);
    }


    private void showCheckoutWindow() {
        JDialog checkoutDialog = new JDialog(this, "Checkout", true);
        checkoutDialog.setLayout(new BorderLayout());

        // Panel for cart summary
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Cart Summary"));
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            summaryPanel.add(new JLabel(product.getName() + " - $" + product.getPrice() + " x " + quantity));
        }
        checkoutDialog.add(new JScrollPane(summaryPanel), BorderLayout.NORTH);

        // Panel for payment information
        JPanel paymentPanel = new JPanel(new GridLayout(0, 2));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment Information"));
        JTextField cardNumberField = new JTextField();
        JTextField expiryDateField = new JTextField();
        JTextField cvvField = new JTextField();
        paymentPanel.add(new JLabel("Card Number:"));
        paymentPanel.add(cardNumberField);
        paymentPanel.add(new JLabel("Expiry Date (MM/YY):"));
        paymentPanel.add(expiryDateField);
        paymentPanel.add(new JLabel("CVV:"));
        paymentPanel.add(cvvField);
        checkoutDialog.add(paymentPanel, BorderLayout.CENTER);

        // Checkout button
        JButton confirmButton = new JButton("Confirm Purchase");
        confirmButton.addActionListener(e -> {
            if (validatePaymentDetails(cardNumberField.getText(), expiryDateField.getText(), cvvField.getText())) {
                // Process payment and record sales
                for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    ProductData.recordSale(product, quantity);
                    // Update the product quantity in the global product data
                    ProductData.updateProductQuantity(product, -quantity);
                }

                JOptionPane.showMessageDialog(checkoutDialog, "Purchase Successful!");
                shoppingCart.clearCart();
                // Call to refresh revenue data in SellerView
                // Note: This requires access to an instance of SellerView. Adjust as per your application design.
                // sellerView.refreshRevenueData();

                checkoutDialog.dispose();
            }
        });
        checkoutDialog.add(confirmButton, BorderLayout.SOUTH);

        checkoutDialog.setSize(400, 400);
        checkoutDialog.setLocationRelativeTo(this);
        checkoutDialog.setVisible(true);
    }

    private boolean validatePaymentDetails(String cardNumber, String expiryDate, String cvv) {
        // Validate Card Number (16 digits)
        if (!cardNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Invalid Card Number. It must be 16 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate Expiry Date (MM/YY format)
        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid Expiry Date. Format must be MM/YY.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate CVV (3 digits)
        if (!cvv.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Invalid CVV. It must be 3 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void updateProductQuantities() {
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
            Product product = entry.getKey();
            int quantityPurchased = entry.getValue();
            ProductData.updateProductQuantity(product, -quantityPurchased); 
        }
    }
}