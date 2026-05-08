package view;

import controller.AuthController;
import model.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class LoginFrame extends JFrame {
    // 1. Raddina el fields "Medium" (20 blasset 30)
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("SE CONNECTER");
    private AuthController authController;
    private Image backgroundImage;

    public LoginFrame() {
        this(new AuthController());
    }

    public LoginFrame(AuthController authController) {
        this.authController = authController;

        try {
            backgroundImage = ImageIO.read(new File("src/img/back1.jpeg"));
        } catch (IOException e) {
            System.out.println("Erreur: Malqitech el taswira fi src/img/");
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Narssmou el background
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                    // 2. Overlay transparent (mouch abyadh) bech el background yabqa dhahir
                    g.setColor(new Color(0, 0, 0, 80));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        setTitle("-Gestion d'ecole-");
        setSize(900, 600); // 7ajm medium lel fenetre
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // --- Email Section ---
        JLabel lblUser = new JLabel("Email :");
        lblUser.setFont(labelFont);
        lblUser.setForeground(Color.WHITE); // Ktiba bidha bech tban fil background
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblUser, gbc);

        txtUsername.setFont(fieldFont);
        txtUsername.setPreferredSize(new Dimension(300, 35)); // 3. Taille Medium
        gbc.gridy = 1;
        add(txtUsername, gbc);

        // --- Password Section ---
        JLabel lblPass = new JLabel("Mot de passe:");
        lblPass.setFont(labelFont);
        lblPass.setForeground(Color.WHITE);
        gbc.gridy = 2;
        add(lblPass, gbc);

        txtPassword.setFont(fieldFont);
        txtPassword.setPreferredSize(new Dimension(300, 35)); // 3. Taille Medium
        gbc.gridy = 3;
        add(txtPassword, gbc);

        // --- Button Section ---
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        // 4. Connecter maktouba bel akhel w bouton design
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(150, 40));

        gbc.gridy = 4;
        gbc.insets = new Insets(25, 10, 10, 10);
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