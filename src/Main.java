
import view.LoginFrame;
import util.SingletonConnection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for the School Management System.
 * Coordinates the launch of the MVC View components.
 */
public class Main {
    public static void main(String[] args) {
        // Set System Look and Feel for a cleaner UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize the database connection (Singleton) [cite: 4]
        try {
            util.SingletonConnection.getConnection();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            return; // Stop execution if DB is not reachable
        }

        // Launch the application GUI on the Event Dispatch Thread [cite: 3]
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}