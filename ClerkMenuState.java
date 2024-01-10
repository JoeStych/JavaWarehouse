import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class ClerkMenuState extends State {
    private static Warehouse warehouse;
    private JFrame frame;

    public ClerkMenuState(Context context, Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        frame = new JFrame("Clerk Menu State");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Clerk Menu State");
        titleLabel.setBounds(150, 20, 200, 25);
        panel.add(titleLabel);

        JButton addClientButton = new JButton("Add a Client");
        addClientButton.setBounds(50, 80, 150, 25);
        addClientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addClient();
            }
        });
        panel.add(addClientButton);

        JButton showProductsButton = new JButton("Show Products");
        showProductsButton.setBounds(50, 120, 150, 25);
        showProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showProducts();
            }
        });
        panel.add(showProductsButton);

        JButton showClientsButton = new JButton("Show Clients");
        showClientsButton.setBounds(50, 160, 150, 25);
        showClientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showClients();
            }
        });
        panel.add(showClientsButton);

        JButton viewWaitlistButton = new JButton("View Waitlist");
        viewWaitlistButton.setBounds(50, 200, 150, 25);
        viewWaitlistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewWaitlist();
            }
        });
        panel.add(viewWaitlistButton);

        JButton acceptPaymentButton = new JButton("Accept Payment");
        acceptPaymentButton.setBounds(50, 240, 150, 25);
        acceptPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                acceptPayment();
            }
        });
        panel.add(acceptPaymentButton);

        JButton becomeClientButton = new JButton("Become a Client");
        becomeClientButton.setBounds(50, 280, 150, 25);
        becomeClientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                becomeClient();
            }
        });
        panel.add(becomeClientButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(220, 300, 80, 25);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        panel.add(backButton);
    }

    private void addClient() {
        String clientName = JOptionPane.showInputDialog("Enter client name:");
        String address = JOptionPane.showInputDialog("Enter address:");
        String phone = JOptionPane.showInputDialog("Enter phone:");

        if (clientName == null || address == null || phone == null){
            JOptionPane.showMessageDialog(null, "Client was not added because a field was left empty.");
            return;
        }

        Client client = warehouse.addClient(clientName, address, phone);
        if (client != null) {
            JOptionPane.showMessageDialog(null, "Client added successfully:\n" + client);
        } else {
            JOptionPane.showMessageDialog(null, "Client could not be added.");
        }
    }

    private void showProducts() {
        StringBuilder productsInfo = new StringBuilder("Products:\n");
        Iterator<Product> allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            productsInfo.append(product.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, productsInfo.toString());
    }

    private void showClients() {
        // Create an array of options for the menu
        String[] options = {"All Clients", "Clients with Outstanding Balance", "Clients with No Transactions in Last Six Months"};
    
        // Display a menu and get the user's choice
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select an option:",
                "View Clients",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
    
        // Process the user's choice
        switch (choice) {
            case 0:
                displayAllClients();
                break;
            case 1:
                displayClientsWithOutstandingBalance();
                break;
            case 2:
                displayClientsWithNoTransactionsInLastSixMonths();
                break;
        }
    }
    
    // Method to display all clients
    private void displayAllClients() {
        StringBuilder clientsInfo = new StringBuilder("All Clients:\n");
        Iterator<Client> allClients = warehouse.getClients();
        while (allClients.hasNext()) {
            Client client = allClients.next();
            clientsInfo.append(client.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, clientsInfo.toString());
    }
    
    // Method to display clients with outstanding balance
    private void displayClientsWithOutstandingBalance() {
        StringBuilder clientsInfo = new StringBuilder("Clients with Outstanding Balance:\n");
        Iterator<Client> clientsWithBalance = warehouse.getClientsWithOutstandingBalance();
        while (clientsWithBalance.hasNext()) {
            Client client = clientsWithBalance.next();
            clientsInfo.append(client.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, clientsInfo.toString());
    }
    
    // Method to display clients with no transactions in the last six months
    private void displayClientsWithNoTransactionsInLastSixMonths() {
        StringBuilder clientsInfo = new StringBuilder("Clients with No Transactions in Last Six Months:\n");
        Iterator<Client> inactiveClients = warehouse.getClientsWithNoTransactionsInLastSixMonths();
        while (inactiveClients.hasNext()) {
            Client client = inactiveClients.next();
            clientsInfo.append(client.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, clientsInfo.toString());
    }

    private void acceptPayment() {
        String clientIDInput = JOptionPane.showInputDialog("Enter client ID:");
        if (clientIDInput == null) {
            // User canceled the input
            return;
        }

        String amountInput = JOptionPane.showInputDialog("Enter amount of payment:");
        if (amountInput == null) {
            // User canceled the input
            return;
        }

        // Validate that the amount is a valid double
        double amount;
        try {
            amount = Double.parseDouble(amountInput);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Operation canceled due to invalid input.");
            return;
        }

        warehouse.acceptClientPayment(warehouse.getClientById(clientIDInput), amount);
        JOptionPane.showMessageDialog(null, "Payment accepted from client " + clientIDInput + " in the amount of $" + amount);
    }

    private void viewWaitlist() {
        // Retrieve the waitlist from the warehouse and build the waitlist string
        boolean success = false;
        String productID = JOptionPane.showInputDialog("Enter product ID:");
        StringBuilder waitlistInfo = new StringBuilder("Waitlist:\n");
        Iterator<Product> productlist = warehouse.getProducts();
        while (productlist.hasNext()) {
            Product product = productlist.next();
            if (product.getProductId().equals(productID)){
                waitlistInfo.append(product.getWaitlist().toString());
                success = true;
            }
        }
        // Display the waitlist in a dialog box
        if (success){
            JOptionPane.showMessageDialog(null, waitlistInfo.toString(), "Waitlist", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No clients were found on this product's waitlist.");
        }
        
    }

    private void becomeClient() {
        String clientIDInput = JOptionPane.showInputDialog("Enter Client ID to become a client:");
        if (clientIDInput == null) {
            // User canceled the input
            return;
        }
    
        // Validate that the client ID is valid
        if (warehouse.getClientById(clientIDInput) != null) {
            // Store the client ID in the context
            context.setCurrentClientID(clientIDInput);
            
            // Transition to ClientMenuState
            context.changeState(new ClientMenuState(context, clientIDInput, warehouse));
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Operation canceled due to invalid Client ID.");
        }
    }

    //return to opening state
    private void goBack() {
        context.changeState(new OpeningState(context, warehouse));
        frame.dispose();
    }

    //this function is unused in GUI version
    public State handleInput(String choice){
        return this;
    }
}