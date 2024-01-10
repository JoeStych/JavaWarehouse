import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class ClientMenuState extends State {
    private static Warehouse warehouse;
    private JFrame frame;

    public ClientMenuState(Context context, String clientID, Warehouse warehouse) {
        super(context);
        context.setCurrentClientID(clientID);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        frame = new JFrame("Client Menu State");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Client Menu State");
        titleLabel.setBounds(150, 20, 200, 25);
        panel.add(titleLabel);

        JButton showDetailsButton = new JButton("Show Client Details");
        showDetailsButton.setBounds(50, 80, 200, 25);
        showDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showClientDetails();
            }
        });
        panel.add(showDetailsButton);

        JButton showProductsButton = new JButton("Show Products");
        showProductsButton.setBounds(50, 120, 200, 25);
        showProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showProducts();
            }
        });
        panel.add(showProductsButton);

        JButton showInvoiceButton = new JButton("Show Client Invoice");
        showInvoiceButton.setBounds(50, 160, 200, 25);
        showInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showClientInvoice();
            }
        });
        panel.add(showInvoiceButton);

        JButton addWishlistButton = new JButton("Add Product to Wishlist");
        addWishlistButton.setBounds(50, 200, 200, 25);
        addWishlistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProductToWishlist();
            }
        });
        panel.add(addWishlistButton);

        JButton removeWishlistButton = new JButton("Remove Product from Wishlist");
        removeWishlistButton.setBounds(50, 240, 200, 25);
        removeWishlistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeProductFromWishlist();
            }
        });
        panel.add(removeWishlistButton);

        JButton showWishlistButton = new JButton("Show Client Wishlist");
        showWishlistButton.setBounds(50, 280, 200, 25);
        showWishlistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWishlist();
            }
        });
        panel.add(showWishlistButton);

        JButton showWaitlistButton = new JButton("Show Client Waitlist");
        showWaitlistButton.setBounds(50, 320, 200, 25);
        showWaitlistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWaitlist();
            }
        });
        panel.add(showWaitlistButton);

        JButton processOrderButton = new JButton("Process an Order");
        processOrderButton.setBounds(50, 360, 200, 25);
        processOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processOrder();
            }
        });
        panel.add(processOrderButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(220, 400, 80, 25);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        panel.add(backButton);
    }

    private void showClientDetails() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);
    
        if (client != null) {
            StringBuilder details = new StringBuilder("Client Details:\n");
            details.append("Client ID: ").append(client.getClientId()).append("\n");
            details.append("Client Name: ").append(client.getClientName()).append("\n");
            details.append("Address: ").append(client.getAddress()).append("\n");
            details.append("Phone: ").append(client.getPhone()).append("\n");
            details.append("Balance: $").append(client.getBalance()).append("\n");
    
            JOptionPane.showMessageDialog(null, details.toString(), "Client Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showProducts() {
        StringBuilder productsInfo = new StringBuilder("Products:\n");
        Iterator<Product> allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            productsInfo.append(product.toString()).append("\n");
        }
    
        JTextArea textArea = new JTextArea(productsInfo.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
    
        JOptionPane.showMessageDialog(null, scrollPane, "Product List", JOptionPane.PLAIN_MESSAGE);
    }

    private void showClientInvoice() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);
    
        if (client != null) {
            List<Invoice> clientInvoices = warehouse.getInvoicesForClient(client.getClientId());
            StringBuilder invoicesInfo = new StringBuilder();
    
            if (!clientInvoices.isEmpty()) {
                invoicesInfo.append("Invoices for ").append(client.getClientName()).append(":\n");
                for (Invoice invoice : clientInvoices) {
                    invoicesInfo.append(invoice).append("\n");
                }
            } else {
                invoicesInfo.append("No invoices found for ").append(client.getClientName());
            }
    
            JTextArea textArea = new JTextArea(invoicesInfo.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setEditable(false);
    
            JOptionPane.showMessageDialog(null, scrollPane, "Client Invoices", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProductToWishlist() {
        String clientID = context.getCurrentClientID();
        String productId = JOptionPane.showInputDialog("Enter product ID:");
        int quantity;
    
        try {
            quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        Client client = warehouse.getClientById(clientID);
        Product product = warehouse.getProductById(productId);
    
        if (client == null || product == null) {
            JOptionPane.showMessageDialog(null, "Client or product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int result = warehouse.addProductToWishlist(client, product, quantity);
        switch (result) {
            case Warehouse.ADD_PRODUCT_TO_WISHLIST_SUCCESS:
                JOptionPane.showMessageDialog(null, "Product added to the wishlist successfully.");
                break;
            case Warehouse.WISHLIST_PRODUCT_ALREADY_EXISTS:
                JOptionPane.showMessageDialog(null, "Product is already in the wishlist.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case Warehouse.CLIENT_ALREADY_IN_WAITLIST:
                JOptionPane.showMessageDialog(null, "Client is already in the waitlist for this product.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Failed to add the product to the wishlist.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void removeProductFromWishlist() {
        String clientID = context.getCurrentClientID();
        String productId = JOptionPane.showInputDialog("Enter product ID:");
    
        Client client = warehouse.getClientById(clientID);
        Product product = warehouse.getProductById(productId);
    
        if (client == null || product == null) {
            JOptionPane.showMessageDialog(null, "Client or product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int result = warehouse.removeProductFromWishlist(client, product);
        switch (result) {
            case Warehouse.REMOVE_PRODUCT_FROM_WISHLIST_SUCCESS:
                JOptionPane.showMessageDialog(null, "Product removed from the wishlist successfully.");
                break;
            case Warehouse.WISHLIST_PRODUCT_NOT_FOUND:
                JOptionPane.showMessageDialog(null, "Product not found in the wishlist.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Failed to remove the product from the wishlist.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void showWishlist() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);
    
        if (client != null) {
            StringBuilder wishlistInfo = new StringBuilder("Wishlist for Client: " + client.getClientName() + "\n");
    
            Wishlist wishlist = client.getWishlist();
            if (!wishlist.getProducts().isEmpty()) {
                wishlistInfo.append("Products in Wishlist:\n");
    
                for (Product product : wishlist.getProducts()) {
                    double totalPrice = wishlist.getProductQuantity(product.getProductId()) * product.getPrice();
    
                    wishlistInfo.append("  Product ID: ").append(product.getProductId()).append("\n");
                    wishlistInfo.append("  Product Name: ").append(product.getProductName()).append("\n");
                    wishlistInfo.append("  Quantity: ").append(wishlist.getProductQuantity(product.getProductId())).append("\n");
                    wishlistInfo.append("  Product Price: ").append(product.getPrice()).append("\n");
                    wishlistInfo.append("  Total Amount: ").append(totalPrice).append("\n\n");
                }
            } else {
                wishlistInfo.append("The wishlist is empty.\n");
            }
    
            JOptionPane.showMessageDialog(null, wishlistInfo.toString(), "Wishlist Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showWaitlist() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);
    
        if (client != null) {
            StringBuilder waitlistInfo = new StringBuilder("Waitlist for Client: " + client.getClientName() + "\n");
    
            Iterator<Product> allProducts = warehouse.getProducts();
            while (allProducts.hasNext()) {
                Product product = allProducts.next();
                Waitlist waitlist = product.getWaitlist();
                int clientQuantity = waitlist.getClientQuantity(client);
    
                if (clientQuantity > 0) {
                    waitlistInfo.append("Product ID: ").append(product.getProductId()).append("\n");
                    waitlistInfo.append("Product Name: ").append(product.getProductName()).append("\n");
                    waitlistInfo.append("Client Quantity: ").append(clientQuantity).append("\n\n");
                }
            }
    
            JOptionPane.showMessageDialog(null, waitlistInfo.toString(), "Waitlist Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void processOrder() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);


        if (client != null) {
            Wishlist wishlist = client.getWishlist();
            if (wishlist.getProducts().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must place items into your wishlist before you can process an order.");
                return;
            }
            warehouse.processOrder(client);
        } else {
            JOptionPane.showMessageDialog(null, "Client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        context.changeState(new OpeningState(context, warehouse));
        frame.dispose(); // Close the current window
    }

    //GUI doesnt use this
    public State handleInput(String choice) {
        return this;
    }
}