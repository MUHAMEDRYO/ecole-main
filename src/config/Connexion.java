package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Connexion {
    private static Connection connection = null;

    static {
        // Ista3mel getClassLoader().getResourceAsStream bech yal9ah fil "resources" wala "src"
        try (InputStream is = Connexion.class.getClassLoader().getResourceAsStream("configuration.properties")) {
            if (is == null) {
                System.err.println("Erreur: Le fichier configuration.properties est introuvable !");
            } else {
                Properties props = new Properties();
                props.load(is);

                // Thabbet elli el keys (jdbc.url, etc.) kif melli fil file bedhabt
                String driver = props.getProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
                String url = props.getProperty("jdbc.url");
                String user = props.getProperty("jdbc.user");
                String pass = props.getProperty("jdbc.password");


                if (url != null && !url.startsWith("jdbc:")) {
                    url = "jdbc:" + url;
                }

                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Connexion réussie (Classe Connexion) !");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
