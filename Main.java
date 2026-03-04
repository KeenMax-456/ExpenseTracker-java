import javax.swing.SwingUtilities;

// --- 4. MAIN CLASS ---
public class Main {
    public static void main(String[] args) {
        // Run the application on the Event Dispatch Thread for Swing thread safety
        SwingUtilities.invokeLater(() -> new ExpenseTrackerUI());
    }
}