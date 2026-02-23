package gui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gestione.GestoreArchivi;
import gui.Theme;
import progettoarchivio.CatalogoSoggetti;

public class DashboardView extends JPanel {

    public DashboardView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Theme.CONTENT_BG);
        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(title);

        add(headerPanel, BorderLayout.NORTH);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(Theme.CONTENT_BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        statsPanel.add(
                createStatCard("Total Archives", String.valueOf(GestoreArchivi.getInstance().getArchivi().size())));

        int totalPhotos = GestoreArchivi.getInstance().getArchivi().values().stream()
                .mapToInt(a -> a.getFotografie().size())
                .sum();
        statsPanel.add(createStatCard("Total Photos", String.valueOf(totalPhotos)));

        statsPanel.add(createStatCard("Subjects", String.valueOf(CatalogoSoggetti.getInstance().dimensione())));

        // Wrap statsPanel to top-align
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Theme.CONTENT_BG);
        centerWrapper.add(statsPanel, BorderLayout.NORTH);

        add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 36));
        valueLabel.setForeground(Theme.ACCENT_COLOR);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_REGULAR);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        card.add(valueLabel);
        card.add(javax.swing.Box.createVerticalStrut(10));
        card.add(titleLabel);

        return card;
    }

    public void refresh() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }
}
