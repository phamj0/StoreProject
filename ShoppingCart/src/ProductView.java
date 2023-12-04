import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        cartButton = new JButton(new ImageIcon("Images/SC.png"));
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
    }

    private void initializeProducts() {
        products = new ArrayList<>(); // Load products (hardcoded or from a database)
        products.add(new Product("Product 1", 19.99, "Description", 10));
        products.add(new Product("Product 2", 29.99, "Description",  5));
        products.add(new Product("Product 3", 19.99, "Description", 10));
        products.add(new Product("Product 4", 29.99, "Description", 5));
        products.add(new Product("Product 5", 19.99, "Description", 10));
        products.add(new Product("Product 6", 29.99, "Description", 5));
        products.add(new Product("Product 7", 19.99, "Description", 10));
        products.add(new Product("Product 8", 29.99, "Description", 5));

    }

    private void displayProducts() {
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
                if (product.getQuantity() > 0) {
                    shoppingCart.addProduct(product);
                    JOptionPane.showMessageDialog(ProductView.this,
                            "Added to cart: " + product.getName());
                } else {
                    JOptionPane.showMessageDialog(ProductView.this,
                            "Product is out of stock", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                }
            });
            panel.add(addToCartButton);

            productPanel.add(panel);
        }
    }

    private void showProductDetails(Product product) {
        JDialog detailsDialog = new JDialog(this, "Product Details", true);
        detailsDialog.setLayout(new GridLayout(0, 1));
        detailsDialog.add(new JLabel("Name: " + product.getName()));
        detailsDialog.add(new JLabel("Price: $" + product.getPrice()));
        detailsDialog.add(new JLabel("Description: " + product.getDescription()));
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            if (product.getQuantity() > 0) {
                shoppingCart.addProduct(product);
                JOptionPane.showMessageDialog(detailsDialog,
                        "Added to cart: " + product.getName());
            } else {
                JOptionPane.showMessageDialog(detailsDialog,
                        "Product is out of stock", "Stock Alert", JOptionPane.WARNING_MESSAGE);
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

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            listModel.addElement(product.getName() + " - $" + product.getPrice() + " x " + quantity);
        }

        JList<String> list = new JList<>(listModel);
        cartDialog.add(new JScrollPane(list), BorderLayout.CENTER);

        JLabel totalLabel = new JLabel("Total: $" + String.format("%.2f", shoppingCart.getTotal()));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cartDialog.add(totalLabel, BorderLayout.SOUTH);

        cartDialog.setSize(300, 300);
        cartDialog.setLocationRelativeTo(this);
        cartDialog.setVisible(true);
    }
}
