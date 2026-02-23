package gui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Archives Management");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(title, BorderLayout.WEST);

        // Add Button
        JButton addBtn = new JButton("Add Archive");
        addBtn.setFont(Theme.FONT_BOLD);
        addBtn.setBackground(Theme.ACCENT_COLOR);
        addBtn.setForeground(java.awt.Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> {
            AddArchiveDialog dialog = new AddArchiveDialog(javax.swing.SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshData();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Theme.CONTENT_BG);
        btnPanel.add(addBtn);
        headerPanel.add(btnPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Archive Name", "Manager Name", "Phone", "Address", "Photos Count" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(30);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(Theme.CARD_BG);
        table.setSelectionBackground(Theme.ACCENT_HOVER);
        table.setSelectionForeground(java.awt.Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);
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
}
