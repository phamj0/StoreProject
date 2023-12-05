import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SellerView extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel revenuePanel;
    private JPanel inventoryPanel;
    private JPanel addProductPanel;

    // Components for Revenue Panel
    private JLabel revenueLabel;
    private JLabel salesLabel;
    private JLabel profitLabel;

    // Components for Inventory Panel
    private JTable inventoryTable;
    private DefaultTableModel inventoryModel;
    private List<Product> products;

    // Components for Add Product Panel
    private JTextField productNameField;
    private JTextField productCostField;
    private JTextField productPriceField;
    private JTextField productQuantityField;
    private JTextField productDescriptionField;
    private JButton addProductButton;

    private JButton logoutButton;

    public SellerView() {
        setTitle("Seller Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        logoutButton = new JButton("Logout");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);

        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(SellerView.this,
                        "Are you sure you want to log out?", "Confirm Logout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    dispose(); // Close the SellerView
                    LoginView loginView = new LoginView();
                    LoginController loginController = new LoginController(loginView); 
                    loginView.setVisible(true);
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        // Revenue Panel
        revenuePanel = new JPanel();
        revenueLabel = new JLabel("Revenue: ");
        salesLabel = new JLabel("Sales: ");
        profitLabel = new JLabel("Profit: ");
        revenuePanel.add(revenueLabel);
        revenuePanel.add(salesLabel);
        revenuePanel.add(profitLabel);
        
        // Inventory Panel
        initializeProducts();
        inventoryPanel = new JPanel(new BorderLayout());
        inventoryModel = new DefaultTableModel(new Object[]{"Product Name", "Cost", "Price", "Quantity", "Increase", "Decrease"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4; // Only buttons are editable
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
        addProductPanel = new JPanel(new GridLayout(6, 2)); // Adjust grid layout to accommodate new field
        productNameField = new JTextField(10);
        productCostField = new JTextField(10);
        productPriceField = new JTextField(10);
        productQuantityField = new JTextField(10);
        productDescriptionField = new JTextField(10);
        // Add Product Panel
        addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = productNameField.getText();
                    double cost = Double.parseDouble(productCostField.getText());
                    double price = Double.parseDouble(productPriceField.getText());
                    String description = productDescriptionField.getText(); // Ensure this field is initialized
                    int quantity = Integer.parseInt(productQuantityField.getText());
                    if (quantity < 0) {
                        throw new IllegalArgumentException("Quantity cannot be negative.");
                    }
                    if (cost >= price) {
                        throw new IllegalArgumentException("Cost must be less than the price.");
                    }
                    Product newProduct = new Product(name, cost, price, description, quantity);

                    // Add product to local list and shared ProductData
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
        addProductPanel.add(new JLabel("Description:"));  // Add a label for description
        addProductPanel.add(productDescriptionField);     // Add the description field to the panel
        addProductPanel.add(addProductButton);

        // Adding tabs to TabbedPane
        tabbedPane.addTab("Revenue", revenuePanel);
        tabbedPane.addTab("Inventory", inventoryPanel);
        tabbedPane.addTab("Add Product", addProductPanel);

        add(tabbedPane);
        setLocationRelativeTo(null);
    }

    private void initializeProducts() {
        products = ProductData.getProducts();
    }

    private void updateInventoryTable() {
        inventoryModel.setRowCount(0); // Clear existing data
        for (Product product : products) {
            inventoryModel.addRow(new Object[]{
                product.getName(),
                product.getCost(),  // Add cost to the table
                product.getPrice(),
                product.getQuantity(),
                "Increase",
                "Decrease"
            });
        }
    }
    

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

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
