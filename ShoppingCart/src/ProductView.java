import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
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

        UIManager.put("ScrollBar.width", 15);
        UIManager.put("ScrollBar.thumb", new ColorUIResource(new Color(100, 149, 237))); // Thumb color
        UIManager.put("ScrollBar.thumbHighlight", new ColorUIResource(Color.LIGHT_GRAY)); // Thumb highlight color
        UIManager.put("ScrollBar.thumbDarkShadow", new ColorUIResource(Color.DARK_GRAY)); // Thumb shadow
        UIManager.put("ScrollBar.thumbLightShadow", new ColorUIResource(Color.GRAY)); // Thumb light shadow
        UIManager.put("ScrollBar.track", new ColorUIResource(new Color(240, 240, 240))); // Track color

        Timer refreshTimer = new Timer(5000, e -> refreshProducts());
        refreshTimer.start();

        shoppingCart = new ShoppingCart();
        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Spacing between products
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Modernized navigation bar
        JPanel navBar = new JPanel(new BorderLayout(10, 10));
        JLabel storeName = new JLabel("My Store", JLabel.CENTER);
        storeName.setFont(new Font("Arial", Font.BOLD, 18));
        navBar.add(storeName, BorderLayout.WEST);

        searchField = new JTextField();
        navBar.add(searchField, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartButton = new JButton(new ImageIcon("ShoppingCart/Images/SC.png"));
        styleButton(cartButton, new Color(100, 149, 237), Color.WHITE);
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShoppingCart();
            }
        });
        rightPanel.add(cartButton);

        logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(100, 149, 237)); // Styling the logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(ProductView.this,
                        "Are you sure you want to log out?", "Confirm Logout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
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

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border for a cleaner look
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);
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

    private void styleButton(JButton button, Color backgroundColor, Color textColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    private void displayProducts() {
        productPanel.removeAll();
        productPanel.setLayout(new GridLayout(0, 1, 10, 10)); // One item per row

        for (Product product : products) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout(10, 10));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

            JPanel detailsPanel = new JPanel(new GridLayout(2, 1));
            JLabel nameLabel = new JLabel(product.getName(), JLabel.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel priceLabel = new JLabel("$" + String.format("%.2f", product.getPrice()), JLabel.CENTER);
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            detailsPanel.add(nameLabel);
            detailsPanel.add(priceLabel);

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton detailsButton = new JButton("View Details");
            styleButton(detailsButton, new Color(70, 130, 180));
            detailsButton.addActionListener(e -> showProductDetails(product));

            JButton addToCartButton = new JButton("Add to Cart");
            styleButton(addToCartButton, new Color(60, 179, 113), Color.WHITE);
            styleButton(addToCartButton, new Color(60, 179, 113));
            addToCartButton.addActionListener(e -> {
                if (shoppingCart.addProduct(product)) {
                    JOptionPane.showMessageDialog(ProductView.this,
                            "Added to cart: " + product.getName());
                } else {
                    JOptionPane.showMessageDialog(ProductView.this,
                            "No more available", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                }
            });

            buttonsPanel.add(detailsButton);
            buttonsPanel.add(addToCartButton);

            itemPanel.add(detailsPanel, BorderLayout.CENTER);
            itemPanel.add(buttonsPanel, BorderLayout.SOUTH);

            productPanel.add(itemPanel);
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private void showProductDetails(Product product) {
        JDialog detailsDialog = new JDialog(this, "Product Details", true);
        detailsDialog.setLayout(new BorderLayout());
        detailsDialog.setSize(400, 300);
        detailsDialog.setLocationRelativeTo(this);

        // Panel for displaying product details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font detailsFont = new Font("Arial", Font.PLAIN, 16);

        JLabel nameLabel = new JLabel("Name: " + product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detailsPanel.add(nameLabel);

        JLabel priceLabel = new JLabel("Price: $" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(detailsFont);
        detailsPanel.add(priceLabel);

        JLabel descriptionLabel = new JLabel("Description: " + product.getDescription());
        descriptionLabel.setFont(detailsFont);
        detailsPanel.add(descriptionLabel);

        detailsDialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);

        // Add to Cart button with modern styling
        JButton addToCartButton = new JButton("Add to Cart");
        styleButton(addToCartButton, new Color(60, 179, 113), Color.WHITE);
        addToCartButton.addActionListener(e -> {
            if (shoppingCart.addProduct(product)) {
                JOptionPane.showMessageDialog(detailsDialog,
                        "Added to cart: " + product.getName());
            } else {
                JOptionPane.showMessageDialog(detailsDialog,
                        "No more available", "Stock Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.setVisible(true);
    }




    private void showShoppingCart() {
        JDialog cartDialog = new JDialog(this, "Shopping Cart", true);
        cartDialog.setLayout(new BorderLayout());

        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

        Font itemFont = new Font("Arial", Font.PLAIN, 16); // Modern font for cart items
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel itemLabel = new JLabel(product.getName() + " - $" + product.getPrice() + " x " + quantity);
            itemLabel.setFont(itemFont);
            itemPanel.add(itemLabel);

            JButton addButton = new JButton("+");
            styleButton(addButton, new Color(100, 149, 237), Color.WHITE);
            addButton.addActionListener(e -> {
                if (!shoppingCart.addProduct(product)) {
                    JOptionPane.showMessageDialog(cartDialog, "No more available", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                }
                cartDialog.dispose();
                showShoppingCart();
            });
            itemPanel.add(addButton);

            JButton subtractButton = new JButton("-");
            styleButton(subtractButton, new Color(255, 69, 0), Color.WHITE);
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
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Larger and bold font for total
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalLabel);

        JButton checkoutButton = new JButton("Checkout");
        styleButton(checkoutButton, new Color(60, 179, 113), Color.WHITE);
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
        checkoutDialog.setSize(400, 400);
        checkoutDialog.setLocationRelativeTo(this);

        // Panel for cart summary
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Cart Summary"));
        Font summaryFont = new Font("Arial", Font.PLAIN, 16);
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            JLabel productLabel = new JLabel(product.getName() + " - $" + product.getPrice() + " x " + quantity);
            productLabel.setFont(summaryFont);
            summaryPanel.add(productLabel);
        }
        checkoutDialog.add(new JScrollPane(summaryPanel), BorderLayout.NORTH);

        // Panel for payment information
        JPanel paymentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment Information"));
        paymentPanel.setOpaque(false);
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(labelFont);
        JTextField cardNumberField = new JTextField();
        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);

        JLabel expiryDateLabel = new JLabel("Expiry Date (MM/YY):");
        expiryDateLabel.setFont(labelFont);
        JTextField expiryDateField = new JTextField();
        paymentPanel.add(expiryDateLabel);
        paymentPanel.add(expiryDateField);

        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(labelFont);
        JTextField cvvField = new JTextField();
        paymentPanel.add(cvvLabel);
        paymentPanel.add(cvvField);

        checkoutDialog.add(paymentPanel, BorderLayout.CENTER);

        // Checkout button
        JButton confirmButton = new JButton("Confirm Purchase");
        styleButton(confirmButton, new Color(60, 179, 113), Color.WHITE);
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