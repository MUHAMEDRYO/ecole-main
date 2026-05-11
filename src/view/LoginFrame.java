package view;

import controller.AuthController;
import model.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("SE CONNECTER");

    // Menu Components
    private JMenu menuLang = new JMenu("FR 🌐");
    private JMenu menuHelp = new JMenu("Aide");
    private JMenuItem itemContact = new JMenuItem("Contactez l'administrateur");
    private JLabel lblStatus = new JLabel(" ● Online "); // Status indicator

    private JLabel lblUser = new JLabel("Email :");
    private JLabel lblPass = new JLabel("Mot de passe:");

    private AuthController authController;
    private Image backgroundImage;
    private String currentLang = "FR";

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

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(255, 255, 255, 200));

        itemContact.addActionListener(e -> showHelpDialog());
        menuHelp.add(itemContact);

        JMenuItem fr = new JMenuItem("Français");
        JMenuItem en = new JMenuItem("English");
        JMenuItem ar = new JMenuItem("العربية");
        fr.addActionListener(al -> updateLanguage("FR"));
        en.addActionListener(al -> updateLanguage("EN"));
        ar.addActionListener(al -> updateLanguage("AR"));
        menuLang.add(fr); menuLang.add(en); menuLang.add(ar);

        lblStatus.setForeground(new Color(0, 150, 0)); // Green for online
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuBar.add(menuHelp);
        menuBar.add(Box.createHorizontalGlue()); // Push language and status to right
        menuBar.add(lblStatus);
        menuBar.add(new JSeparator(SwingConstants.VERTICAL));
        menuBar.add(menuLang);
        setJMenuBar(menuBar);
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 80));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        setTitle("-Gestion d'ecole-");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        lblUser.setFont(labelFont);
        lblUser.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        add(lblUser, gbc);

        txtUsername.setFont(fieldFont);
        txtUsername.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = 2;
        add(txtUsername, gbc);

        lblPass.setFont(labelFont);
        lblPass.setForeground(Color.WHITE);
        gbc.gridy = 3;
        add(lblPass, gbc);

        txtPassword.setFont(fieldFont);
        txtPassword.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = 4;
        add(txtPassword, gbc);

        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(150, 40));

        gbc.gridy = 5;
        gbc.insets = new Insets(25, 10, 10, 10);
        add(btnLogin, gbc);
        this.getRootPane().setDefaultButton(btnLogin);

        btnLogin.addActionListener(e -> handleLogin());
    }

    private void updateLanguage(String lang) {
        this.currentLang = lang;
        switch (lang) {
            case "EN":
                lblUser.setText("Email:");
                lblPass.setText("Password:");
                btnLogin.setText("LOGIN");
                menuLang.setText("EN 🌐");
                menuHelp.setText("Help");
                itemContact.setText("Contact Administrator");
                setTitle("-School Management-");
                break;
            case "AR":
                lblUser.setText("البريد الإلكتروني :");
                lblPass.setText("كلمة المرور :");
                btnLogin.setText("تسجيل الدخول");
                menuLang.setText("AR 🌐");
                menuHelp.setText("مساعدة");
                itemContact.setText("الاتصال بالمسؤول");
                setTitle("-إدارة المدرسة-");
                break;
            default: // FR
                lblUser.setText("Email :");
                lblPass.setText("Mot de passe:");
                btnLogin.setText("SE CONNECTER");
                menuLang.setText("FR 🌐");
                menuHelp.setText("Aide");
                itemContact.setText("Contactez l'administrateur");
                setTitle("-Gestion d'ecole-");
                break;
        }
    }

    private void showHelpDialog() {
        String msg = currentLang.equals("AR") ? "للمساعدة، يرجى الاتصال بقسم تكنولوجيا المعلومات." :
                (currentLang.equals("EN") ? "For support, please contact the IT department." :
                 "Pour obtenir de l'aide, veuillez contacter le service informatique.");
        JOptionPane.showMessageDialog(this, msg, menuHelp.getText(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        Utilisateur loggedUser = authController.login(user, pass);

        if (loggedUser != null) {
            this.dispose();
            new MainDashboard(loggedUser, authController).setVisible(true);
        } else {
            String errorMsg = currentLang.equals("AR") ? "بيانات الاعتماد غير صحيحة" :
                    (currentLang.equals("EN") ? "Invalid credentials" : "Identifiants incorrects");
            String errorTitle = currentLang.equals("AR") ? "خطأ" :
                    (currentLang.equals("EN") ? "Error" : "Erreur");

            JOptionPane.showMessageDialog(this, errorMsg, errorTitle, JOptionPane.ERROR_MESSAGE);
        }
    }
}