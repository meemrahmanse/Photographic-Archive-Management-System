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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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
        super(owner, "New Archive", ModalityType.APPLICATION_MODAL);
        initUI();
    }

    private void initUI() {
        setSize(520, 530);
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
                g2.setColor(Theme.ACCENT_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        headerBar.setLayout(new BoxLayout(headerBar, BoxLayout.Y_AXIS));
        headerBar.setBorder(new EmptyBorder(22, 28, 20, 28));
        headerBar.setOpaque(false);

        JLabel headerTitle = new JLabel("Create New Archive");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerTitle.setForeground(Color.WHITE);
        JLabel headerSub = new JLabel("Fill in the archive details below to get started.");
        headerSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerSub.setForeground(new Color(186, 230, 253));
        headerBar.add(headerTitle);
        headerBar.add(Box.createVerticalStrut(4));
        headerBar.add(headerSub);
        add(headerBar, BorderLayout.NORTH);

        // ── Form ───────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARD_BG);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new Insets(0, 0, 6, 0);

        nomeArchivioField = addRow(form, gc, 0, "Archive Name", "e.g. Rome 1970s");
        respNomeField = addRow(form, gc, 1, "Manager Name", "e.g. Maria Rossi");
        respTelefonoField = addRow(form, gc, 2, "Phone", "e.g. +39 06 1234567");
        respIndirizzoField = addRow(form, gc, 3, "Address", "Street, City, Country");
        respOrarioField = addRow(form, gc, 4, "Opening Hours", "e.g. Mon-Fri 09:00-18:00");

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

        JButton saveBtn = new JButton("Create Archive");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBackground(Theme.ACCENT_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new EmptyBorder(10, 28, 10, 28));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveArchive());

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

    private JTextField addRow(JPanel form, GridBagConstraints gc, int row, String label, String placeholder) {
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
        field.setToolTipText(placeholder);
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(4, 12, 4, 12)));

        // Focus border effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 2, true),
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

    private void saveArchive() {
        String nomeArchivio = nomeArchivioField.getText().trim();
        String respNome = respNomeField.getText().trim();
        String respIndirizzo = respIndirizzoField.getText().trim();
        String respTel = respTelefonoField.getText().trim();
        String respOrari = respOrarioField.getText().trim();

        if (nomeArchivio.isEmpty() || respNome.isEmpty() || respIndirizzo.isEmpty() || respTel.isEmpty()
                || respOrari.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!nomeArchivio.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
            JOptionPane.showMessageDialog(this, "Archive Name must be 3-50 alphanumeric characters.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!respNome.matches("^[a-zA-Z\\s.]{3,50}$")) {
            JOptionPane.showMessageDialog(this, "Manager Name must contain only letters, spaces, or dots (3-50 chars).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (respIndirizzo.length() < 5 || respIndirizzo.length() > 100) {
            JOptionPane.showMessageDialog(this, "Address must be between 5 and 100 characters.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!respTel.matches("^\\+?[0-9\\s\\-\\(\\)]{7,15}$")) {
            JOptionPane.showMessageDialog(this,
                    "Phone number is invalid. Allowed: digits, spaces, -, (), and optional +.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (respOrari.length() < 3 || respOrari.length() > 50) {
            JOptionPane.showMessageDialog(this, "Opening hours must be between 3 and 50 characters.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (GestoreArchivi.getInstance().getArchivio(nomeArchivio) != null) {
            JOptionPane.showMessageDialog(this, "An archive with this name already exists.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Responsabile resp = new Responsabile(respNome, respIndirizzo, respTel, respOrari);
        Archivio archivio = new Archivio(nomeArchivio, resp);
        GestoreArchivi.getInstance().aggiungiArchivio(archivio);
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
