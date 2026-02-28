package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Cursor;

import gui.Theme;
import progettoarchivio.CatalogoSoggetti;
import progettoarchivio.Soggetto;

public class SubjectsView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    // Color map for subject types
    private static final Map<String, Color> TYPE_COLORS = new HashMap<>();
    static {
        TYPE_COLORS.put("Personaggio", new Color(59, 130, 246));
        TYPE_COLORS.put("Politico", new Color(168, 85, 247));
        TYPE_COLORS.put("Artista", new Color(236, 72, 153));
        TYPE_COLORS.put("Luogo", new Color(34, 197, 94));
        TYPE_COLORS.put("Oggetto", new Color(245, 158, 11));
    }

    public SubjectsView() {
        initUI();
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

        JLabel title = new JLabel("Subjects Catalog");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel sub = new JLabel("All subjects linked to your photographic collection.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(Theme.TEXT_SECONDARY);

        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(sub);
        headerPanel.add(titleBlock, BorderLayout.WEST);

        // Add Subject Button
        JButton addSubjectBtn = new JButton("+ Add New Subject");
        addSubjectBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addSubjectBtn.setBackground(new Color(59, 130, 246));
        addSubjectBtn.setForeground(Color.WHITE);
        addSubjectBtn.setFocusPainted(false);
        addSubjectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addSubjectBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        addSubjectBtn.addActionListener(e -> openAddSubjectDialog());

        headerPanel.add(addSubjectBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────────────────
        String[] columns = { "Key", "Type", "Description" };
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
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(235, 244, 255));
        table.setSelectionForeground(Theme.TEXT_PRIMARY);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Custom renderer: color-coded badge for "Type" column
        DefaultTableCellRenderer baseRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 14));
                setBackground(sel ? new Color(235, 244, 255) : (row % 2 == 0 ? Color.WHITE : new Color(250, 252, 255)));
                setForeground(Theme.TEXT_PRIMARY);
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return this;
            }
        };

        DefaultTableCellRenderer typeRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 14));
                setBackground(sel ? new Color(235, 244, 255) : (row % 2 == 0 ? Color.WHITE : new Color(250, 252, 255)));
                Color c = TYPE_COLORS.getOrDefault(String.valueOf(val), Theme.TEXT_SECONDARY);
                setForeground(c);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return this;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(baseRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(typeRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(baseRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CARD_BG);

        ArchivesView.RoundedCard card = new ArchivesView.RoundedCard(16, Theme.CARD_BG);
        card.setLayout(new BorderLayout());
        card.add(scrollPane, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void openAddSubjectDialog() {
        java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(this);
        AddSubjectDialog dialog = new AddSubjectDialog(win);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshData();
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Soggetto s : CatalogoSoggetti.getInstance().tuttiSoggetti()) {
            tableModel.addRow(new Object[] { s.getKey(), s.getClass().getSimpleName(), s.getDescription() });
        }
    }
}
