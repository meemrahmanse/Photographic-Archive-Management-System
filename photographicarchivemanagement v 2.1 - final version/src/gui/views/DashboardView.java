package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
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

public class DashboardView extends JPanel {

    public DashboardView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Theme.CONTENT_BG);
        mainContainer.setBorder(new EmptyBorder(30, 40, 40, 40));

        // 1. Welcome & Presentation Header
        mainContainer.add(createHeaderSection());
        mainContainer.add(Box.createVerticalStrut(30));

        // 2. Stats Section
        mainContainer.add(createStatsSection());
        mainContainer.add(Box.createVerticalStrut(40));

        // 3. Quick Actions
        mainContainer.add(createQuickActionsSection());
        mainContainer.add(Box.createVerticalStrut(40));

        // 4. System Advantages / Features
        mainContainer.add(createFeaturesSection());

        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderSection() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Welcome to ArchiveCloud Pro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel(
                "A modern, professional platform for managing massive photographic collections securely.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitle);

        return headerPanel;
    }

    private JPanel createStatsSection() {
        JPanel statsWrapper = new JPanel(new BorderLayout());
        statsWrapper.setBackground(Theme.CONTENT_BG);
        statsWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("System Overview");
        sectionTitle.setFont(Theme.FONT_TITLE);
        sectionTitle.setForeground(Theme.TEXT_PRIMARY);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        statsWrapper.add(sectionTitle, BorderLayout.NORTH);

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 25, 0));
        statsGrid.setBackground(Theme.CONTENT_BG);

        // Calculate real data
        int archCount = GestoreArchivi.getInstance().getArchivi().size();
        int totalPhotos = GestoreArchivi.getInstance().getArchivi().values().stream()
                .mapToInt(a -> a.getFotografie().size()).sum();
        int subjCount = CatalogoSoggetti.getInstance().dimensione();

        statsGrid.add(createStatCard("Total Archives", String.valueOf(archCount), new Color(236, 240, 255),
                Theme.ACCENT_COLOR));
        statsGrid.add(createStatCard("Photographs", String.valueOf(totalPhotos), new Color(220, 252, 231),
                Theme.SUCCESS_COLOR));
        statsGrid.add(createStatCard("Catalog Subjects", String.valueOf(subjCount), new Color(254, 243, 199),
                new Color(217, 119, 6))); // Amber
        statsGrid.add(createStatCard("System Uptime", "99.9%", new Color(243, 232, 255), new Color(147, 51, 234))); // Purple
                                                                                                                    // (Dummy
                                                                                                                    // realish
                                                                                                                    // data)

        statsWrapper.add(statsGrid, BorderLayout.CENTER);
        return statsWrapper;
    }

    private JPanel createStatCard(String title, String value, Color iconBg, Color valueColor) {
        RoundedPanel card = new RoundedPanel(20, Theme.CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(valueColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_BOLD);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);

        return card;
    }

    private JPanel createQuickActionsSection() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Theme.CONTENT_BG);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Quick Actions");
        sectionTitle.setFont(Theme.FONT_TITLE);
        sectionTitle.setForeground(Theme.TEXT_PRIMARY);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        wrap.add(sectionTitle, BorderLayout.NORTH);

        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        acts.setBackground(Theme.CONTENT_BG);

        JButton btnNewArch = createActionButton("New Archive", Theme.ACCENT_COLOR, Color.WHITE);
        btnNewArch.addActionListener(e -> navigateTo("archives"));

        JButton btnUpload = createActionButton("Upload Photograph", Theme.CARD_BG, Theme.TEXT_PRIMARY);
        btnUpload.addActionListener(e -> navigateTo("photos"));

        JButton btnManageSubj = createActionButton("Manage Subjects", Theme.CARD_BG, Theme.TEXT_PRIMARY);
        btnManageSubj.addActionListener(e -> navigateTo("subjects"));

        JButton btnReport = createActionButton("Generate Report", Theme.CARD_BG, Theme.TEXT_PRIMARY);
        btnReport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Generating system report... check your data/ folder soon.", "Report",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        acts.add(btnNewArch);
        acts.add(btnUpload);
        acts.add(btnManageSubj);
        acts.add(btnReport);

        wrap.add(acts, BorderLayout.CENTER);
        return wrap;
    }

    private void navigateTo(String viewName) {
        java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (win instanceof gui.MainApplication) {
            ((gui.MainApplication) win).switchView(viewName);
        }
    }

    private JButton createActionButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                new EmptyBorder(12, 24, 12, 24)));
        return btn;
    }

    private JPanel createFeaturesSection() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Theme.CONTENT_BG);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Core Capabilities");
        sectionTitle.setFont(Theme.FONT_TITLE);
        sectionTitle.setForeground(Theme.TEXT_PRIMARY);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        wrap.add(sectionTitle, BorderLayout.NORTH);

        JPanel featuresGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        featuresGrid.setBackground(Theme.CONTENT_BG);

        featuresGrid.add(createFeatureCard("Smart Organization",
                "Categorize and find photos instantly using advanced metadata and AI-ready tags.", Theme.ACCENT_COLOR));
        featuresGrid.add(createFeatureCard("Secure Cloud Sync",
                "Enterprise-grade encryption keeps your archives synced and strictly confidential.",
                Theme.SUCCESS_COLOR));
        featuresGrid.add(createFeatureCard("Role-Based Access",
                "Manage managers and assign granular permissions across multiple archives.", new Color(217, 119, 6)));
        featuresGrid.add(createFeatureCard("High-Resolution Previews",
                "Lossless rendering for fast, professional-grade image curation.", new Color(147, 51, 234)));

        wrap.add(featuresGrid, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel createFeatureCard(String title, String desc, Color accent) {
        RoundedPanel card = new RoundedPanel(15, Theme.CARD_BG);
        card.setLayout(new BorderLayout(15, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel iconHolder = new JPanel();
        iconHolder.setBackground(accent);
        iconHolder.setPreferredSize(new Dimension(8, 0));
        card.add(iconHolder, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Theme.CARD_BG);

        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tLabel.setForeground(Theme.TEXT_PRIMARY);

        // JTextArea used for multiline JLabel equivalent
        javax.swing.JTextArea dLabel = new javax.swing.JTextArea(desc);
        dLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dLabel.setForeground(Theme.TEXT_SECONDARY);
        dLabel.setWrapStyleWord(true);
        dLabel.setLineWrap(true);
        dLabel.setOpaque(false);
        dLabel.setEditable(false);
        dLabel.setFocusable(false);
        dLabel.setBorder(null);

        textPanel.add(tLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(dLabel);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    // Custom panel for rounded corners
    class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            cornerRadius = radius;
            backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow (subtle edge)
            graphics.setColor(new Color(0, 0, 0, 10));
            graphics.fillRoundRect(1, 1, width - 2, height - 1, arcs.width, arcs.height);

            // Draw background
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 1, height - 2, arcs.width, arcs.height);

            // Draw border
            graphics.setColor(Theme.BORDER_COLOR);
            graphics.drawRoundRect(0, 0, width - 1, height - 2, arcs.width, arcs.height);
        }
    }
}
