package view;

import controller.MatiereController;
import model.Matiere;
import model.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class PersonnelView extends JPanel {
    private MatiereController matiereController = new MatiereController();
    private final Utilisateur currentPersonnel;

    private JLabel lblInfo, lblUser, lblRole;
    private JPanel infoPanel;

    public PersonnelView(Utilisateur personnel) {
        this.currentPersonnel = personnel;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblInfo, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Mes Informations"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        lblUser = new JLabel("Nom d'utilisateur: " + personnel.getUsername());
        infoPanel.add(lblUser, gbc);
        gbc.gridy++;
        lblRole = new JLabel("Rôle: " + personnel.getRole());
        infoPanel.add(lblRole, gbc);

        add(infoPanel, BorderLayout.CENTER);
        
        translateUI("FR");
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            lblInfo.setText("فضاء الموظفين - " + currentPersonnel.getUsername());
            infoPanel.setBorder(BorderFactory.createTitledBorder("معلوماتي"));
            lblUser.setText("اسم المستخدم: " + currentPersonnel.getUsername());
            lblRole.setText("الدور: " + currentPersonnel.getRole());
        } else if (lang.equals("EN")) {
            lblInfo.setText("Staff Space - " + currentPersonnel.getUsername());
            infoPanel.setBorder(BorderFactory.createTitledBorder("My Information"));
            lblUser.setText("Username: " + currentPersonnel.getUsername());
            lblRole.setText("Role: " + currentPersonnel.getRole());
        } else {
            lblInfo.setText("Espace Personnel - " + currentPersonnel.getUsername());
            infoPanel.setBorder(BorderFactory.createTitledBorder("Mes Informations"));
            lblUser.setText("Nom d'utilisateur: " + currentPersonnel.getUsername());
            lblRole.setText("Rôle: " + currentPersonnel.getRole());
        }
    }
}

