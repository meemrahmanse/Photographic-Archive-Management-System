package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.util.NoSuchElementException;
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
        super(owner, "Add Photo — " + selectedArchive, ModalityType.APPLICATION_MODAL);
        this.selectedArchive = selectedArchive;
        initUI();
    }

    private void initUI() {
        setSize(520, 520);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Theme.CARD_BG);

        // ── Header bar ─────────────────────────────────────────────────────
        JPanel headerBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(22, 163, 74)); // green accent
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        headerBar.setLayout(new BoxLayout(headerBar, BoxLayout.Y_AXIS));
        headerBar.setBorder(new EmptyBorder(20, 28, 18, 28));
        headerBar.setOpaque(false);

        JLabel headerTitle = new JLabel("Add Photograph");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerTitle.setForeground(Color.WHITE);
        JLabel headerSub = new JLabel("Archive:  " + selectedArchive);
        headerSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerSub.setForeground(new Color(187, 247, 208)); // green-200
        headerBar.add(headerTitle);
        headerBar.add(Box.createVerticalStrut(4));
        headerBar.add(headerSub);
        add(headerBar, BorderLayout.NORTH);

        // ── Form ───────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARD_BG);
        form.setBorder(new EmptyBorder(22, 28, 8, 28));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 6, 0);
        gc.weightx = 1.0;

        idField = addRow(form, gc, 0, "Photo ID (integer)", "e.g. 1001");
        dimField = addRow(form, gc, 1, "Size", "e.g. 10x15 or 10x15 cm");
        statoField = addRow(form, gc, 2, "Condition", "e.g. Good, Damaged");

        // Subject combo
        gc.gridy = 6;
        JLabel subjLbl = new JLabel("Subject");
        subjLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subjLbl.setForeground(Theme.TEXT_PRIMARY);
        form.add(subjLbl, gc);

        gc.gridy = 7;
        soggettoCombo = new JComboBox<>();
        soggettoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        soggettoCombo.setBackground(Color.WHITE);
        soggettoCombo.setPreferredSize(new Dimension(0, 38));
        soggettoCombo.addItem("— Select Subject —");
        for (Soggetto s : CatalogoSoggetti.getInstance().tuttiSoggetti())
            soggettoCombo.addItem(s.getKey());
        form.add(soggettoCombo, gc);

        // Color check
        gc.gridy = 8;
        isColorCheck = new JCheckBox("This is a Color Photo");
        isColorCheck.setFont(new Font("Segoe UI", Font.BOLD, 13));
        isColorCheck.setForeground(Theme.TEXT_PRIMARY);
        isColorCheck.setBackground(Theme.CARD_BG);
        isColorCheck.setOpaque(true);
        isColorCheck.addActionListener(e -> printTypeField.setEnabled(isColorCheck.isSelected()));
        form.add(isColorCheck, gc);

        printTypeField = addRow(form, gc, 5, "Print Type (if color)", "e.g. Glossy, Matte");
        printTypeField.setEnabled(false);

        add(form, BorderLayout.CENTER);

        // ── Footer buttons ─────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Theme.CARD_BG);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_COLOR),
                new EmptyBorder(14, 24, 14, 24)));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setBackground(Theme.CARD_BG);
        cancelBtn.setForeground(Theme.TEXT_SECONDARY);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(new EmptyBorder(10, 22, 10, 22));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("Add Photo");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBackground(new Color(22, 163, 74));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new EmptyBorder(10, 28, 10, 28));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> savePhoto());

        JPanel btnRow = new JPanel();
        btnRow.setLayout(new BoxLayout(btnRow, BoxLayout.X_AXIS));
        btnRow.setBackground(Theme.CARD_BG);
        btnRow.add(Box.createHorizontalGlue());
        btnRow.add(cancelBtn);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(saveBtn);
        footer.add(btnRow);
        add(footer, BorderLayout.SOUTH);
    }

    private JTextField addRow(JPanel form, GridBagConstraints gc, int row, String label, String tooltip) {
        gc.gridy = row * 2;
        gc.gridx = 0;
        gc.insets = new Insets(row == 0 ? 0 : 10, 0, 4, 0);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Theme.TEXT_PRIMARY);
        form.add(lbl, gc);

        gc.gridy = row * 2 + 1;
        gc.insets = new Insets(0, 0, 0, 0);
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.TEXT_PRIMARY);
        field.setToolTipText(tooltip);
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(4, 12, 4, 12)));

        // Focus border effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(22, 163, 74), 2, true),
                        new EmptyBorder(3, 11, 3, 11)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(4, 12, 4, 12)));
            }
        });

        form.add(field, gc);
        return field;
    }

    private void savePhoto() {
        String id = idField.getText().trim();
        String dim = dimField.getText().trim();
        String stato = statoField.getText().trim();
        String printType = printTypeField.getText().trim();

        if (id.isEmpty() || dim.isEmpty() || stato.isEmpty() || soggettoCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "ID, Size, Condition, and Subject are all required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!id.matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(this, "Photo ID must be an integer only.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!dim.matches("^[0-9]+[xX][0-9]+\\s*([a-zA-Z]{1,5})?$")) {
            JOptionPane.showMessageDialog(this, "Size must be in a valid format like '10x15' or '10x15 cm'.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stato.length() < 3 || stato.length() > 50) {
            JOptionPane.showMessageDialog(this, "Condition must be between 3 and 50 characters.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isColorCheck.isSelected()) {
            if (printType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Print Type is required for color photos.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!printType.matches("^[a-zA-Z0-9\\s\\-]{3,30}$")) {
                JOptionPane.showMessageDialog(this,
                        "Print Type must be 3-30 characters (alphanumeric, spaces, dashes).", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
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

        Fotografia foto = isColorCheck.isSelected()
                ? new FotoAColore(id, dim, stato, soggetto, printType)
                : new Fotografia(id, dim, stato, soggetto);

        archivio.aggiungiFoto(foto);
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
