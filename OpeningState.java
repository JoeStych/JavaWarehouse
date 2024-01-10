import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpeningState extends State {
    private static Warehouse warehouse;

    public OpeningState(Context context, Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Opening State");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JLabel label = new JLabel("Opening State");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);

        JButton clientButton = new JButton("Client Login");
        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientID = JOptionPane.showInputDialog("Enter client ID:");
                context.setCurrentClientID(clientID);

                if (clientID == null){
                    JOptionPane.showMessageDialog(null, "Operation cancelled because you did not enter a client ID.");
                    return;
                }

                context.changeState(new ClientMenuState(context, clientID, warehouse));
                frame.dispose();
            }
        });
        panel.add(clientButton);

        JButton clerkButton = new JButton("Clerk Menu");
        clerkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.changeState(new ClerkMenuState(context, warehouse));
                frame.dispose();
            }
        });
        panel.add(clerkButton);

        JButton managerButton = new JButton("Manager Menu");
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.changeState(new ManagerMenuState(context, warehouse));
                frame.dispose();
            }
        });
        panel.add(managerButton);

        JButton saveAndExitButton = new JButton("Save and Exit");
        saveAndExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.save();
                System.exit(0);
            }
        });
        panel.add(saveAndExitButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void displayOptions() {
        // Not used in the GUI version
    }

    public State handleInput(String choice) {
        // Not used in the GUI version
        return null;
    }
}