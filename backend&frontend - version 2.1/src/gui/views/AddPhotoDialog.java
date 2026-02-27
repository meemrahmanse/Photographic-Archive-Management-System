package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gestione.Archivio;
import gestione.FotoAColore;
import gestione.Fotografia;
import gestione.GestoreArchivi;
import gui.Theme;
import progettoarchivio.CatalogoSoggetti;
import progettoarchivio.Soggetto;

public class AddPhotoDialog extends JDialog {

    private JTextField idField;
    private JTextField dimField;
    private JTextField statoField;
    private JComboBox<String> soggettoCombo;
    private JCheckBox isColorCheck;
    private JTextField printTypeField;
    private boolean saved = false;

    private final String selectedArchive;

    public AddPhotoDialog(Window owner, String selectedArchive) {
        super(owner, "Add Photo to " + selectedArchive, ModalityType.APPLICATION_MODAL);
        this.selectedArchive = selectedArchive;
        initUI();
    }

    private void initUI() {
        setSize(450, 450);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.CARD_BG);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBackground(Theme.CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        idField = createField(formPanel, "Photo ID:");
        dimField = createField(formPanel, "Size (e.g. 10x15):");
        statoField = createField(formPanel, "Condition:");

        // Subject selector
        formPanel.add(new JLabel("Subject Key:"));
        soggettoCombo = new JComboBox<>();
        soggettoCombo.addItem("Select Subject...");
        for (Soggetto s : CatalogoSoggetti.getInstance().tuttiSoggetti()) {
            soggettoCombo.addItem(s.getKey());
        }
        formPanel.add(soggettoCombo);

        // Color or B/W
        isColorCheck = new JCheckBox("Is Color Photo?");
        isColorCheck.setBackground(Theme.CARD_BG);
        isColorCheck.addActionListener(e -> printTypeField.setEnabled(isColorCheck.isSelected()));

        formPanel.add(isColorCheck);
        formPanel.add(new JLabel("")); // Spacer

        printTypeField = createField(formPanel, "Print Type (if color):");
        printTypeField.setEnabled(false);

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
        saveBtn.addActionListener(e -> savePhoto());

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

    private void savePhoto() {
        String id = idField.getText().trim();
        if (id.isEmpty() || soggettoCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "ID and Subject are required.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Archivio archivio = GestoreArchivi.getInstance().getArchivio(selectedArchive);
        if (archivio.cercaFoto(id) != null) {
            JOptionPane.showMessageDialog(this, "A photo with this ID already exists in this archive.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String subjKey = (String) soggettoCombo.getSelectedItem();
        Soggetto soggetto;
        try {
            soggetto = CatalogoSoggetti.getInstance().trovaPerChiave(subjKey);
        } catch (NoSuchElementException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Subject not found: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Fotografia foto;
        if (isColorCheck.isSelected()) {
            foto = new FotoAColore(
                    id,
                    dimField.getText().trim(),
                    statoField.getText().trim(),
                    soggetto,
                    printTypeField.getText().trim());
        } else {
            foto = new Fotografia(
                    id,
                    dimField.getText().trim(),
                    statoField.getText().trim(),
                    soggetto);
        }

        archivio.aggiungiFoto(foto);
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
