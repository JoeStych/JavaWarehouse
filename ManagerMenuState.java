import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.awt.GridLayout;
import java.util.Objects;

public class ManagerMenuState extends State {
    private static Warehouse warehouse;
    private JFrame frame;

    public ManagerMenuState(Context context, Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        frame = new JFrame("Manager Menu State");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Manager Menu State");
        titleLabel.setBounds(150, 20, 200, 25);
        panel.add(titleLabel);

        JButton addProductButton = new JButton("Add a Product");
        addProductButton.setBounds(50, 80, 150, 25);
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        panel.add(addProductButton);

        JButton receiveShipmentButton = new JButton("Receive Shipment");
        receiveShipmentButton.setBounds(50, 120, 150, 25);
        receiveShipmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supplyProductsInWarehouse();
            }
        });
        panel.add(receiveShipmentButton);

        JButton becomeClerkButton = new JButton("Become Clerk");
        becomeClerkButton.setBounds(50, 160, 150, 25);
        becomeClerkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                becomeClerk();
            }
        });
        panel.add(becomeClerkButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(220, 180, 80, 25);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        panel.add(backButton);
    }

    private void addProduct() {
        JFrame frame = new JFrame("Add Product");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel();
        frame.add(panel);
        placeAddProductComponents(panel);
    
        frame.setVisible(true);
    }
    
    private void placeAddProductComponents(JPanel panel) {
        panel.setLayout(new GridLayout(4, 2));
    
        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);
    
        JLabel priceLabel = new JLabel("Product Price:");
        JTextField priceField = new JTextField();
        panel.add(priceLabel);
        panel.add(priceField);
    
        JLabel quantityLabel = new JLabel("Product Quantity:");
        JTextField quantityField = new JTextField();
        panel.add(quantityLabel);
        panel.add(quantityField);
    
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productName = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
    
                Product product = warehouse.addProduct(productName, price, quantity);
                if (product != null) {
                    JOptionPane.showMessageDialog(null, "Product added successfully:\n" + product);
                } else {
                    JOptionPane.showMessageDialog(null, "Product could not be added.");
                }
    
                // Close the add product window
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(addButton);
                topFrame.dispose();
            }
        });
        panel.add(addButton);
    }
    
    private void placeWaitlistComponents(JPanel panel) {
        panel.setLayout(new GridLayout(1, 1));
    
        JTextArea waitlistTextArea = new JTextArea();
        waitlistTextArea.setEditable(false);
    
        JScrollPane scrollPane = new JScrollPane(waitlistTextArea);
        panel.add(scrollPane);
    
        StringBuilder waitlistInfo = new StringBuilder("Waitlist:\n");
    
        Iterator<Product> allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            Waitlist waitlist = product.getWaitlist();
    
            if (!waitlist.getClients().isEmpty()) {
                waitlistInfo.append("Product ID: ").append(product.getProductId()).append("\n");
                waitlistInfo.append("Product Name: ").append(product.getProductName()).append("\n\n");
    
                waitlistInfo.append("Clients in Waitlist:\n");
    
                for (Client client : waitlist.getClients()) {
                    waitlistInfo.append("  Client ID: ").append(client.getClientId()).append("\n");
                    waitlistInfo.append("  Client Name: ").append(client.getClientName()).append("\n");
                    waitlistInfo.append("  Client Address: ").append(client.getAddress()).append("\n");
                    waitlistInfo.append("  Client Phone: ").append(client.getPhone()).append("\n");
                    waitlistInfo.append("  Product Quantity: ").append(waitlist.getClientQuantity(client)).append("\n\n");
                }
            }
        }
    
        waitlistTextArea.setText(waitlistInfo.toString());
    }

    private void supplyProductsInWarehouse() {
        JFrame frame = new JFrame("Supply Products");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel();
        frame.add(panel);
        placeSupplyComponents(panel);
    
        frame.setVisible(true);
    }
    
    private void placeSupplyComponents(JPanel panel) {
        panel.setLayout(new GridLayout(4, 1));
    
        JLabel titleLabel = new JLabel("Supply Products");
        panel.add(titleLabel);
    
        JComboBox<String> productComboBox = new JComboBox<>();
        JLabel productLabel = new JLabel("Select Product:");
        panel.add(productLabel);
        panel.add(productComboBox);
    
        JTextField quantityField = new JTextField();
        JLabel quantityLabel = new JLabel("Enter Quantity:");
        panel.add(quantityLabel);
        panel.add(quantityField);
    
        JButton supplyButton = new JButton("Supply");
        panel.add(supplyButton);
    
        // Populate the product combo box
        Iterator<Product> productIterator = warehouse.getProducts();
        while (productIterator.hasNext()) {
            Product product = productIterator.next();
            productComboBox.addItem(product.getProductId() + " | " + product.getProductName());
        }
    
        supplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProduct = Objects.requireNonNull(productComboBox.getSelectedItem()).toString();
                String[] parts = selectedProduct.split("\\|");
                String productId = parts[0].trim();
    
                int quantityToAdd;
                try {
                    quantityToAdd = Integer.parseInt(quantityField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a valid number.");
                    return;
                }
    
                Product selectedProductObj = warehouse.getProductById(productId);
                if (selectedProductObj != null) {
                    warehouse.supplyProducts(selectedProductObj, quantityToAdd);
                    JOptionPane.showMessageDialog(null, "Product supplied successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid product selection. Please try again.");
                }
            }
        });
    }

    private void becomeClerk() {
        // Transition to the ClerkMenuState
        context.changeState(new ClerkMenuState(context, warehouse));
        frame.dispose();
    }

    private void goBack() {
        context.changeState(new OpeningState(context, warehouse));
        frame.dispose();
    }

    //GUI doesn't use this
    public State handleInput(String choice) {
        return this;
    }
}