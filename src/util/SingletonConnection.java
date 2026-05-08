package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SingletonConnection {
    private static Connection connection = null;

    static {
        try (InputStream input = SingletonConnection.class.getClassLoader().getResourceAsStream("configuration.properties")) {
            Properties props = new Properties();

            if (input == null) {
                System.out.println("Erreur: Impossible de trouver configuration.properties");
            } else {
                props.load(input);

                String url = props.getProperty("jdbc.url");
                String user = props.getProperty("jdbc.user");
                String pass = props.getProperty("jdbc.password");

                if (!url.startsWith("jdbc:")) {
                    url = "jdbc:" + url;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Connexion Singleton réussie !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}