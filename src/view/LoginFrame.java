package view;

import controller.AuthController;
import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("Connexion");
    private AuthController authController;

    public LoginFrame() {
        this(new AuthController());
    }

    public LoginFrame(AuthController authController) {
        this.authController = authController;
        setTitle("Système de Gestion d'École - Connexion");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        add(new JLabel("Utilisateur:"), gbc);
        gbc.gridx = 1; add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1; add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        Utilisateur loggedUser = authController.login(user, pass);

        if (loggedUser != null) {
            this.dispose();
            new MainDashboard(loggedUser, authController).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
