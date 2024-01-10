public class ExitState extends State {
    public ExitState(Context context) {
        super(context);
    }

    public void displayOptions() {
        System.out.println(
                "Exiting the application. Do you want to save data before closing? (Y|y)[es] to save, anything else to exit without saving.");
    }

    public State handleInput(String choice) {
        choice = choice.trim();
        if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            context.save();
        }
        // Exiting the application
        System.out.println("Exiting the application.");
        return this;
    }
}