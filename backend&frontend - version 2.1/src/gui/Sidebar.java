package gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Sidebar extends JPanel {

    private final MainApplication app;
    private JPanel currentActiveMenu;

    public Sidebar(MainApplication app) {
        this.app = app;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR_BG);
        setPreferredSize(new Dimension(250, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));

        // Logo / Title area
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Theme.SIDEBAR_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));

        JLabel titleLabel = new JLabel("P.A.M.S.");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.SIDEBAR_TEXT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Photo Archive System");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Theme.SIDEBAR_BG);

        addMenuItem(menuPanel, "Dashboard", "dashboard");
        addMenuItem(menuPanel, "Archives", "archives");
        addMenuItem(menuPanel, "Photographs", "photos");
        addMenuItem(menuPanel, "Subjects Catalog", "subjects");
        addMenuItem(menuPanel, "Settings", "settings");

        add(menuPanel, BorderLayout.CENTER);

        // Footer (Quit button)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Theme.SIDEBAR_BG);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JPanel quitBtn = createMenuButton("Exit Application");
        quitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Trigger window closing event
                app.dispatchEvent(new java.awt.event.WindowEvent(app, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });
        footerPanel.add(quitBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addMenuItem(JPanel container, String text, String viewName) {
        JPanel menuItem = createMenuButton(text);

        // Navigation logic
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentActiveMenu != null) {
                    currentActiveMenu.setBackground(Theme.SIDEBAR_BG);
                }
                menuItem.setBackground(Theme.SIDEBAR_HOVER);
                currentActiveMenu = menuItem;
                app.switchView(viewName);
            }
        });

        container.add(menuItem);
        container.add(Box.createVerticalStrut(10));
    }

    private JPanel createMenuButton(String text) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        panel.setBackground(Theme.SIDEBAR_BG);
        panel.setMaximumSize(new Dimension(230, 45));
        panel.setPreferredSize(new Dimension(230, 45));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add padding instead of empty borders
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        contentPanel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BOLD);
        label.setForeground(Theme.SIDEBAR_TEXT);
        contentPanel.add(label);

        panel.add(contentPanel, BorderLayout.CENTER);

        // Hover effects
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentActiveMenu != panel) {
                    panel.setBackground(Theme.SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (currentActiveMenu != panel) {
                    panel.setBackground(Theme.SIDEBAR_BG);
                }
            }
        });

        return panel;
    }
}
