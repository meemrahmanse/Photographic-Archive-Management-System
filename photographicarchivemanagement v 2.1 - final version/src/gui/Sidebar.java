package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Sidebar extends JPanel {

    private final MainApplication app;
    private NavItem currentActive;

    public Sidebar(MainApplication app) {
        this.app = app;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR_BG);
        setPreferredSize(new Dimension(240, 0));

        // ── Brand header ───────────────────────────────────────────────────
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(37, 99, 235), 0, getHeight(), new Color(30, 41, 59)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(28, 22, 24, 22));

        JLabel brand = new JLabel("ArchiveCloud");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(LEFT_ALIGNMENT);

        JLabel brandSub = new JLabel("Pro   v2.0");
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        brandSub.setForeground(new Color(147, 197, 253)); // blue-200
        brandSub.setAlignmentX(LEFT_ALIGNMENT);

        header.add(brand);
        header.add(Box.createVerticalStrut(3));
        header.add(brandSub);
        add(header, BorderLayout.NORTH);

        // ── Navigation items ───────────────────────────────────────────────
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(Theme.SIDEBAR_BG);
        nav.setBorder(new EmptyBorder(18, 0, 0, 0));

        NavItem dash = addNavItem(nav, "\uD83D\uDCC8", "Dashboard", "dashboard");
        NavItem arch = addNavItem(nav, "\uD83D\uDDC2", "Archives", "archives");
        NavItem photos = addNavItem(nav, "\uD83D\uDDBC", "Photographs", "photos");
        NavItem subj = addNavItem(nav, "\uD83D\uDCC4", "Subjects Catalog", "subjects");
        NavItem sett = addNavItem(nav, "\u2699", "Settings", "settings");
        nav.add(Box.createVerticalGlue());
        add(nav, BorderLayout.CENTER);

        // activate dashboard by default
        activateItem(dash);

        // ── Footer ──────────────────────────────────────────────────────────
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(new Color(15, 23, 42)); // darker
        footer.setBorder(new EmptyBorder(14, 14, 18, 14));

        // Quit button
        NavItem quitItem = new NavItem("\u23FB", "Exit Application", null);
        quitItem.setAlignmentX(LEFT_ALIGNMENT);
        quitItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.dispatchEvent(new java.awt.event.WindowEvent(app, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                quitItem.setBackground(new Color(127, 29, 29));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                quitItem.setBackground(new Color(15, 23, 42));
            }
        });
        quitItem.setBackground(new Color(15, 23, 42));

        footer.add(quitItem);
        add(footer, BorderLayout.SOUTH);
    }

    private NavItem addNavItem(JPanel container, String icon, String label, String view) {
        NavItem item = new NavItem(icon, label, view);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activateItem(item);
                app.switchView(view);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentActive != item)
                    item.setBackground(Theme.SIDEBAR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (currentActive != item)
                    item.setBackground(Theme.SIDEBAR_BG);
            }
        });
        container.add(item);
        container.add(Box.createVerticalStrut(2));
        return item;
    }

    private void activateItem(NavItem item) {
        if (currentActive != null) {
            currentActive.setBackground(Theme.SIDEBAR_BG);
            currentActive.setActive(false);
        }
        item.setBackground(Theme.SIDEBAR_HOVER);
        item.setActive(true);
        currentActive = item;
    }

    // ── Custom nav item ────────────────────────────────────────────────────
    class NavItem extends JPanel {
        private boolean active = false;

        NavItem(String icon, String label, String view) {
            setLayout(new BorderLayout());
            setBackground(Theme.SIDEBAR_BG);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(240, 46));
            setPreferredSize(new Dimension(240, 46));
            setOpaque(true);

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(0, 20, 0, 16));

            JLabel iconLbl = new JLabel(icon);
            iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
            iconLbl.setForeground(new Color(148, 163, 184));

            JLabel textLbl = new JLabel(label);
            textLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLbl.setForeground(Theme.SIDEBAR_TEXT);

            inner.add(iconLbl);
            inner.add(Box.createHorizontalStrut(12));
            inner.add(textLbl);

            add(inner, BorderLayout.CENTER);
        }

        void setActive(boolean a) {
            this.active = a;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (active) {
                // Left accent bar
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT_COLOR);
                g2.fillRoundRect(0, 8, 4, getHeight() - 16, 4, 4);
                g2.dispose();
            }
        }
    }
}
