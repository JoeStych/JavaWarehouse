public abstract class State {
    protected Context context;

    public State(Context context) {
        this.context = context;
    }

    public abstract void displayOptions();

    public abstract State handleInput(String choice);
}