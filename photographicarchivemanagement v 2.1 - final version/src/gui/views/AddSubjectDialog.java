package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gui.Theme;
import progettoarchivio.CatalogoSoggetti;
import progettoarchivio.Personaggio;
import progettoarchivio.Luogo;
import progettoarchivio.Oggetto;
import progettoarchivio.Soggetto;

public class AddSubjectDialog extends JDialog {

    private JTextField keyField;
    private JComboBox<String> typeCombo;
    private JTextField nameOrDescField;
    private JPanel dynamicPanel;

    // Personaggio fields
    private JComboBox<String> sessoCombo;
    private JTextField nascitaField;
    private JCheckBox morteCheck;

    private boolean saved = false;

    public AddSubjectDialog(Window owner) {
        super(owner, "Add New Subject", ModalityType.APPLICATION_MODAL);
        initUI();
    }

    private void initUI() {
        setSize(500, 550);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Theme.CARD_BG);

        // Header
        JPanel headerBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(59, 130, 246)); // blue accent
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        headerBar.setLayout(new BoxLayout(headerBar, BoxLayout.Y_AXIS));
        headerBar.setBorder(new EmptyBorder(20, 28, 18, 28));
        headerBar.setOpaque(false);

        JLabel headerTitle = new JLabel("Create Subject");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerTitle.setForeground(Color.WHITE);
        headerBar.add(headerTitle);
        add(headerBar, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARD_BG);
        form.setBorder(new EmptyBorder(22, 28, 8, 28));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 10, 0);
        gc.weightx = 1.0;
        gc.gridx = 0;

        keyField = addRow(form, gc, 0, "Unique Key (ID)", "e.g. MUSEUM_01, PERSON_ALEX");

        gc.gridy = 2;
        JLabel typeLbl = new JLabel("Subject Type");
        typeLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLbl.setForeground(Theme.TEXT_PRIMARY);
        form.add(typeLbl, gc);

        gc.gridy = 3;
        typeCombo = new JComboBox<>(new String[] { "Personaggio", "Luogo", "Oggetto" });
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeCombo.setPreferredSize(new Dimension(0, 38));
        typeCombo.addActionListener(e -> updateDynamicFields());
        form.add(typeCombo, gc);

        nameOrDescField = addRow(form, gc, 2, "Name / Description", "Main identifier or description");

        // Dynamic Panel for type-specific fields
        dynamicPanel = new JPanel(new GridBagLayout());
        dynamicPanel.setBackground(Theme.CARD_BG);
        gc.gridy = 6;
        gc.weighty = 1.0;
        form.add(dynamicPanel, gc);

        add(form, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Theme.CARD_BG);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_COLOR),
                new EmptyBorder(14, 24, 14, 24)));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("Save Subject");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBackground(new Color(59, 130, 246));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveAction());

        JPanel btnRow = new JPanel();
        btnRow.setLayout(new BoxLayout(btnRow, BoxLayout.X_AXIS));
        btnRow.setBackground(Theme.CARD_BG);
        btnRow.add(Box.createHorizontalGlue());
        btnRow.add(cancelBtn);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(saveBtn);
        footer.add(btnRow);
        add(footer, BorderLayout.SOUTH);

        updateDynamicFields();
    }

    private void updateDynamicFields() {
        dynamicPanel.removeAll();
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 8, 0);

        String type = (String) typeCombo.getSelectedItem();
        if ("Personaggio".equals(type)) {
            gc.gridy = 0;
            dynamicPanel.add(new JLabel("Gender"), gc);
            gc.gridy = 1;
            sessoCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
            dynamicPanel.add(sessoCombo, gc);

            gc.gridy = 2;
            dynamicPanel.add(new JLabel("Birth Year"), gc);
            gc.gridy = 3;
            nascitaField = new JTextField();
            nascitaField.setPreferredSize(new Dimension(0, 38));
            dynamicPanel.add(nascitaField, gc);

            gc.gridy = 4;
            morteCheck = new JCheckBox("Deceased");
            morteCheck.setBackground(Theme.CARD_BG);
            dynamicPanel.add(morteCheck, gc);
        }

        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }

    private JTextField addRow(JPanel form, GridBagConstraints gc, int row, String label, String tooltip) {
        gc.gridy = row * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        form.add(lbl, gc);
        gc.gridy = row * 2 + 1;
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 38));
        field.setToolTipText(tooltip);
        form.add(field, gc);
        return field;
    }

    private void saveAction() {
        String key = keyField.getText().trim();
        String name = nameOrDescField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (key.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Key and Name/Description are required.");
            return;
        }

        try {
            Soggetto s;
            if ("Personaggio".equals(type)) {
                char genderChar = sessoCombo.getSelectedItem().toString().charAt(0);
                int year = Integer.parseInt(nascitaField.getText().trim());
                s = new Personaggio(key, name, genderChar, morteCheck.isSelected(), year);
            } else if ("Luogo".equals(type)) {
                s = new Luogo(key, name, name); // key, name, description
            } else {
                s = new Oggetto(key, name, name);
            }

            CatalogoSoggetti.getInstance().aggiungiSoggetto(s);
            saved = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
