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
import java.awt.RenderingHints;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import gestione.Archivio;
import gestione.GestoreArchivi;
import gui.Theme;

public class ArchivesView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    public ArchivesView() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(new EmptyBorder(35, 40, 35, 40));

        // ── Header ──────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(Theme.CONTENT_BG);

        JLabel title = new JLabel("Archives Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("View, add, and remove your photographic archives.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subtitle);
        headerPanel.add(titleBlock, BorderLayout.WEST);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setBackground(Theme.CONTENT_BG);
        btnPanel.add(makeBtn("Delete Selected", new Color(220, 53, 69), Color.WHITE, e -> deleteSelected()));
        btnPanel.add(makeBtn("+ Add Archive", Theme.ACCENT_COLOR, Color.WHITE, e -> addArchive()));
        headerPanel.add(btnPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ── Table inside rounded card ────────────────────────────────────────
        String[] columns = { "Archive Name", "Manager Name", "Phone", "Address", "Photos" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setBackground(Theme.CARD_BG);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(42);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(235, 244, 255));
        table.setSelectionForeground(Theme.TEXT_PRIMARY);

        // Header styling
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Row alternating colors via renderer
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 14));
                if (sel) {
                    setBackground(new Color(235, 244, 255));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                setForeground(Theme.TEXT_PRIMARY);
                return this;
            }
        };
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CARD_BG);

        RoundedCard card = new RoundedCard(16, Theme.CARD_BG);
        card.setLayout(new BorderLayout());
        card.add(scrollPane, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void addArchive() {
        AddArchiveDialog dialog = new AddArchiveDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isSaved())
            refreshData();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an archive to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "<html>Delete <b>" + name + "</b> and ALL its photos?<br>This action cannot be undone.</html>",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            GestoreArchivi.getInstance().rimuoviArchivio(name);
            refreshData();
            JOptionPane.showMessageDialog(this, "Archive \"" + name + "\" deleted successfully.", "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadData() {
        tableModel.setRowCount(0);
        Map<String, Archivio> archivi = GestoreArchivi.getInstance().getArchivi();
        for (Archivio a : archivi.values()) {
            tableModel.addRow(new Object[] {
                    a.getNomeArchivio(),
                    a.getResponsabile().getNome(),
                    a.getResponsabile().getTelefono(),
                    a.getResponsabile().getIndirizzo(),
                    a.getFotografie().size()
            });
        }
    }

    public void refreshData() {
        loadData();
    }

    private JButton makeBtn(String text, Color bg, Color fg, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        b.addActionListener(al);
        return b;
    }

    // ── Rounded card panel ──────────────────────────────────────────────────
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
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 3, radius, radius);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, radius, radius);
            g2.setColor(Theme.BORDER_COLOR);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 2, radius, radius);
            g2.dispose();
        }
    }
}
