package view;

import controller.AuthController;
import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

// Hethi el fenétre el principal mta3 el login
public class LoginFrame extends JFrame {
    // Naasna3 el blayes win nektbou el username wel password
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    // Bouton el connexion
    private JButton btnLogin = new JButton("Connexion");
    // Na3rfou el controller elli bech ythabbet fel base de données
    private AuthController authController;

    // Constructeur par défaut
    public LoginFrame() {
        this(new AuthController());
    }

    // Constructeur elli yebni el interface
    public LoginFrame(AuthController authController) {
        this.authController = authController;

        // Settings mta3 el fenétre (Title, Size, Position fil west)
        setTitle("Système de Gestion d'École - Connexion");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Nesta3mlou GridBagLayout bech n-nadhmou el les composants s7i7
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaces bin el les cases

        // Zid el kelma "Utilisateur" wel champ mte3ha
        add(new JLabel("Utilisateur:"), gbc);
        gbc.gridx = 1; add(txtUsername, gbc);

        // Zid el kelma "Mot de passe" wel champ mte3ha (gridy = 1 ya3ni el star elli ba3do)
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1; add(txtPassword, gbc);

        // Zid el bouton mta3 el login f-e5er star
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        // Ki tenzel 3la el bouton, nadelna el fonction "handleLogin"
        btnLogin.addActionListener(e -> handleLogin());
    }

    // El fonction elli tthabbet f-el utilisateur
    private void handleLogin() {
        // Ne5dou el maktoub f-el interface
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        // Nab3thouhom lel controller bech yel9ahom fel database
        Utilisateur loggedUser = authController.login(user, pass);

        // Itha l9ah (ma3naha mouch null)
        if (loggedUser != null) {
            this.dispose(); // Sakker el fenétre mta3 el login
            // 7el el Dashboard el kbir w 3tih el utilisateur elli d5al
            new MainDashboard(loggedUser, authController).setVisible(true);
        } else {
            // Itha l-id wala el pass ghalta, talle3 message d'erreur
            JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}