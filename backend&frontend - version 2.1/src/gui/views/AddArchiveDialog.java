package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gestione.Archivio;
import gestione.GestoreArchivi;
import gestione.Responsabile;
import gui.Theme;

public class AddArchiveDialog extends JDialog {

    private JTextField nomeArchivioField;
    private JTextField respNomeField;
    private JTextField respIndirizzoField;
    private JTextField respTelefonoField;
    private JTextField respOrarioField;
    private boolean saved = false;

    public AddArchiveDialog(Window owner) {
        super(owner, "Add New Archive", ModalityType.APPLICATION_MODAL);
        initUI();
    }

    private void initUI() {
        setSize(450, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.CARD_BG);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBackground(Theme.CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        nomeArchivioField = createField(formPanel, "Archive Name:");
        respNomeField = createField(formPanel, "Manager Name:");
        respIndirizzoField = createField(formPanel, "Address:");
        respTelefonoField = createField(formPanel, "Phone:");
        respOrarioField = createField(formPanel, "Opening Hours (09:00 - 18:00):");

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Theme.CONTENT_BG);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_COLOR));

        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(Theme.FONT_BOLD);
        cancelBtn.setBackground(new Color(220, 53, 69));       
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(Theme.FONT_BOLD);
        saveBtn.setBackground(Theme.ACCENT_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> saveArchive());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField createField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.FONT_BOLD);
        label.setForeground(Theme.TEXT_PRIMARY);

        JTextField field = new JTextField();
        field.setFont(Theme.FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        panel.add(label);
        panel.add(field);
        return field;
    }

    private void saveArchive() {
        String nomeArchivio = nomeArchivioField.getText().trim();
        String respNome = respNomeField.getText().trim();
        String respIndirizzo = respIndirizzoField.getText().trim();
        String respTel = respTelefonoField.getText().trim();
        String respOrari = respOrarioField.getText().trim();

        if (nomeArchivio.isEmpty() || respNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Archive Name and Manager Name are required.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (GestoreArchivi.getInstance().getArchivio(nomeArchivio) != null) {
            JOptionPane.showMessageDialog(this, "An archive with this name already exists.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            Responsabile resp = new Responsabile();

            resp.setNome(respNome);
            resp.setIndirizzo(respIndirizzo);
            resp.setTelefono(respTel);
            resp.setOrarioApertura(respOrari);

            Archivio archivio = new Archivio(nomeArchivio, resp);
            GestoreArchivi.getInstance().aggiungiArchivio(archivio);

            saved = true;
            dispose();

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
