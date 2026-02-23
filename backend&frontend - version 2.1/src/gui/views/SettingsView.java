package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gestione.GestoreArchivi;
import gui.Theme;
import progettoarchivio.Artista;
import progettoarchivio.CatalogoSoggetti;
import progettoarchivio.Luogo;
import progettoarchivio.Oggetto;
import progettoarchivio.Personaggio;
import progettoarchivio.Politico;

public class SettingsView extends JPanel {

    public SettingsView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Settings & Database Maintenance");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(title);
        headerPanel.add(Box.createHorizontalGlue());

        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.CARD_BG);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));

        // Save Button
        JButton saveBtn = createStyledButton("Save All Data to JSON");
        saveBtn.addActionListener(e -> {
            try {
                GestoreArchivi.getInstance().salvaSuFile();
                CatalogoSoggetti.getInstance().salvaSuFile();
                JOptionPane.showMessageDialog(this, "Data successfully written to JSON files!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sample Data Button
        JButton sampleBtn = createStyledButton("Load Sample Subjects Data");
        sampleBtn.addActionListener(e -> {
            try {
                CatalogoSoggetti.getInstance().aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
                CatalogoSoggetti.getInstance()
                        .aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
                CatalogoSoggetti.getInstance()
                        .aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
                CatalogoSoggetti.getInstance()
                        .aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
                CatalogoSoggetti.getInstance().aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
                JOptionPane.showMessageDialog(this, "Sample Subjects added! Go to Subjects Catalog to view.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Sample data already exists or an error occurred.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        contentPanel.add(saveBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(sampleBtn);

        // Wrap to push settings to top
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.CONTENT_BG);
        wrapper.add(contentPanel, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BOLD);
        btn.setBackground(Theme.SIDEBAR_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    public void refreshData() {
        // Nothing to refresh currently
    }
}
