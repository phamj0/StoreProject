import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * SellerView class that represents the seller dashboard in the application.
 */
public class SellerView extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel revenuePanel;
    private JPanel inventoryPanel;
    private JPanel addProductPanel;

    // Revenue Panel
    private JLabel totalRevenueLabel;
    private JLabel totalSalesLabel;
    private JLabel totalProfitLabel;

    // Inventory Panel
    private JTable inventoryTable;
    private DefaultTableModel inventoryModel;
    private List<Product> products;

    // Add Product Panel
    private JTextField productNameField;
    private JTextField productCostField;
    private JTextField productPriceField;
    private JTextField productQuantityField;
    private JTextField productDescriptionField;
    private JButton addProductButton;

    // Logout 
    private JButton logoutButton;

    /**
     * SellerView constructor to create a window with tabs for revenue, inventory, and adding products.
     */
    public SellerView() {
        setTitle("Seller Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logoutButton = new JButton("Logout");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(SellerView.this,
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

        add(topPanel, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        
        revenuePanel = new JPanel();
        revenuePanel.setLayout(new BoxLayout(revenuePanel, BoxLayout.Y_AXIS));

        JPanel revenueRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalRevenueLabel = new JLabel("Total Revenue: $" + ProductData.getTotalRevenue());
        revenueRow.add(totalRevenueLabel);
        revenuePanel.add(revenueRow);

        JPanel salesRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalSalesLabel = new JLabel("Total Sales: " + ProductData.getTotalSales());
        salesRow.add(totalSalesLabel);
        revenuePanel.add(salesRow);

        JPanel profitRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalProfitLabel = new JLabel("Total Profit: $" + ProductData.getTotalProfit());
        profitRow.add(totalProfitLabel);
        revenuePanel.add(profitRow);

        // Inventory Panel
        initializeProducts();
        inventoryPanel = new JPanel(new BorderLayout());
        inventoryModel = new DefaultTableModel(new Object[]{"Product Name", "Cost", "Price", "Quantity", 
                                                            "Increase", "Decrease"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4;
            }
        };
        inventoryTable = new JTable(inventoryModel);
        inventoryTable.getColumn("Increase").setCellRenderer(new ButtonRenderer());
        inventoryTable.getColumn("Decrease").setCellRenderer(new ButtonRenderer());
        inventoryTable.getColumn("Increase").setCellEditor(new ButtonEditor(new JCheckBox()));
        inventoryTable.getColumn("Decrease").setCellEditor(new ButtonEditor(new JCheckBox()));
        updateInventoryTable();
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        // Add Product Panel
        addProductPanel = new JPanel(new GridLayout(6, 2));
        productNameField = new JTextField(10);
        productCostField = new JTextField(10);
        productPriceField = new JTextField(10);
        productQuantityField = new JTextField(10);
        productDescriptionField = new JTextField(10);
        addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = productNameField.getText();
                    double cost = Double.parseDouble(productCostField.getText());
                    double price = Double.parseDouble(productPriceField.getText());
                    String description = productDescriptionField.getText();
                    int quantity = Integer.parseInt(productQuantityField.getText());
                    if (quantity < 0) {
                        throw new IllegalArgumentException("Quantity cannot be negative.");
                    }
                    if (cost >= price) {
                        throw new IllegalArgumentException("Cost must be less than the price.");
                    }
                    Product newProduct = new Product(name, cost, price, description, quantity);
                    products.add(newProduct);
                    ProductData.addProduct(newProduct);

                    updateInventoryTable();
                } catch ( IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(SellerView.this,
                            "Invalid input: " + ex.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addProductPanel.add(new JLabel("Product Name:"));
        addProductPanel.add(productNameField);
        addProductPanel.add(new JLabel("Cost:"));
        addProductPanel.add(productCostField);
        addProductPanel.add(new JLabel("Price:"));
        addProductPanel.add(productPriceField);
        addProductPanel.add(new JLabel("Quantity:"));
        addProductPanel.add(productQuantityField);
        addProductPanel.add(new JLabel("Description:"));
        addProductPanel.add(productDescriptionField);
        addProductPanel.add(addProductButton);

        tabbedPane.addTab("Revenue", revenuePanel);
        tabbedPane.addTab("Inventory", inventoryPanel);
        tabbedPane.addTab("Add Product", addProductPanel);

        add(tabbedPane);
        setLocationRelativeTo(null);
    }

    /**
     * Initializes the product list from the data source.
     * Singleton Pattern ensures that the product data is initialized only once.
     */
    private void initializeProducts() {
        products = ProductData.getProducts();
    }

    /**
     * Updates the inventory table with the current product data.
     * Observer pattern allows the inventory table to observe the changes in
     * the product data and updates itself accordingly
     */
    private void updateInventoryTable() {
        inventoryModel.setRowCount(0);
        for (Product product : products) {
            inventoryModel.addRow(new Object[]{
                product.getName(),
                product.getCost(),
                product.getPrice(),
                product.getQuantity(),
                "Increase",
                "Decrease"
            });
        }
    }

    /**
     * Refreshes the revenue data displayed on the revenue tab.
     * Observer pattern refreshes data when changes occur.
     */
    public void refreshRevenueData() {
        totalRevenueLabel.setText("Total Revenue: $" + ProductData.getTotalRevenue());
        totalSalesLabel.setText("Total Sales: " + ProductData.getTotalSales());
        totalProfitLabel.setText("Total Profit: $" + ProductData.getTotalProfit());
    }

    /**
     * Custom renderer that renders the buttons in a table cell.
     * Strategy pattern encapsulates the behavior of buttons, allowing 
     * it to be easily replaced or extended.
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * Custom editor that handles the button clicks in a table cell.
     * Command pattern because it encapsulates the logic and information associated with button clicks.
     */
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            button.setText((value == null) ? "" : value.toString());
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                Product product = products.get(inventoryTable.getSelectedRow());
                try {
                    if ("Increase".equals(button.getText())) {
                        product.setQuantity(product.getQuantity() + 1);
                    } else if ("Decrease".equals(button.getText())) {
                        if (product.getQuantity() <= 0) {
                            throw new IllegalArgumentException("Quantity cannot be decreased below 0.");
                        }
                        product.setQuantity(product.getQuantity() - 1);
                    }
                    updateInventoryTable();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(SellerView.this,
                            ex.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            isPushed = false;
            return button.getText();
        }
    }
}
