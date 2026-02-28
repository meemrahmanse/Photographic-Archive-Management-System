package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import gestione.GestoreArchivi;
import gui.Theme;
import progettoarchivio.CatalogoSoggetti;

public class SettingsView extends JPanel {

    public SettingsView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(new EmptyBorder(35, 40, 35, 40));

        // ── Header ──────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Settings & Maintenance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel sub = new JLabel("Manage application data, defaults, and system utilities.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(Theme.TEXT_SECONDARY);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(sub);
        add(headerPanel, BorderLayout.NORTH);

        // ── Settings content ─────────────────────────────────────────────────
        JPanel scrollableContent = new JPanel();
        scrollableContent.setLayout(new BoxLayout(scrollableContent, BoxLayout.Y_AXIS));
        scrollableContent.setBackground(Theme.CONTENT_BG);

        // Section: Data & Sync
        scrollableContent.add(makeSectionLabel("Data & Synchronization"));
        scrollableContent.add(Box.createVerticalStrut(12));
        scrollableContent.add(makeSettingCard(
                "Manual Synchronization",
                "Force an immediate write of all data to disk. System auto-saves on every change.",
                "Sync Now", Theme.ACCENT_COLOR, Color.WHITE,
                e -> {
                    try {
                        GestoreArchivi.getInstance().salvaSuFile();
                        CatalogoSoggetti.getInstance().salvaSuFile();
                        JOptionPane.showMessageDialog(this,
                                "Manual synchronization complete. All files are up to date.", "Synced",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Sync Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }));
        scrollableContent.add(Box.createVerticalStrut(16));

        // Section: System Info
        scrollableContent.add(makeSectionLabel("System Information"));
        scrollableContent.add(Box.createVerticalStrut(12));
        scrollableContent.add(makeInfoCard());

        JScrollPane scroll = new JScrollPane(scrollableContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JLabel makeSectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 2, 0));
        return lbl;
    }

    private JPanel makeSettingCard(String title, String description, String btnText, Color btnBg, Color btnFg,
            java.awt.event.ActionListener action) {
        RoundedCard card = new RoundedCard(14, Theme.CARD_BG);
        card.setLayout(new BorderLayout(20, 0));
        card.setBorder(new EmptyBorder(20, 24, 20, 24));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Theme.CARD_BG);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(Theme.TEXT_PRIMARY);

        JLabel descLbl = new JLabel(description);
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLbl.setForeground(Theme.TEXT_SECONDARY);

        textPanel.add(titleLbl);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLbl);

        JButton btn = new JButton(btnText);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(btnBg);
        btn.setForeground(btnFg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btn.addActionListener(action);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(btn, BorderLayout.EAST);

        return card;
    }

    private JPanel makeInfoCard() {
        RoundedCard card = new RoundedCard(14, Theme.CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 24, 20, 24));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        addInfoRow(card, "Application", "ArchiveCloud Pro v2.0");
        addInfoRow(card, "Platform", "Java Swing / Gson");
        addInfoRow(card, "Data Store", "JSON flat files (data/)");
        addInfoRow(card, "Build", "2026-02-24");

        return card;
    }

    private void addInfoRow(JPanel parent, String key, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Theme.CARD_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel k = new JLabel(key);
        k.setFont(new Font("Segoe UI", Font.BOLD, 13));
        k.setForeground(Theme.TEXT_SECONDARY);
        k.setPreferredSize(new Dimension(130, 28));

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        v.setForeground(Theme.TEXT_PRIMARY);

        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        parent.add(row);
        parent.add(Box.createVerticalStrut(6));
    }

    public void refreshData() {
        /* nothing live to refresh */ }

    static class RoundedCard extends JPanel {
        private final int radius;
        private final Color bg;

        RoundedCard(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 3, radius, radius);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, radius, radius);
            g2.setColor(Theme.BORDER_COLOR);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 2, radius, radius);
            g2.dispose();
        }
    }
}
