
import view.LoginFrame;
import util.SingletonConnection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            util.SingletonConnection.getConnection();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            return;
        }


        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
//wwwwwwwwwwww