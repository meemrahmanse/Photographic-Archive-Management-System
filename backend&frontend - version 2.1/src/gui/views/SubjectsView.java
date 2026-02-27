package gui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.Theme;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


import progettoarchivio.CatalogoSoggetti;
import progettoarchivio.Soggetto;

public class SubjectsView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    public SubjectsView() {
        initUI();
        refreshData();  
        
    }
    private void initUI() {
    	
    	
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

     // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Subjects Catalog");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(title, BorderLayout.WEST);

        // Buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Theme.CONTENT_BG);

        JButton addBtn = new JButton("Add Subject");
        addBtn.setFont(Theme.FONT_BOLD);
        addBtn.setBackground(Theme.ACCENT_COLOR);
        addBtn.setForeground(java.awt.Color.WHITE);
        addBtn.setFocusPainted(false);
        actionsPanel.add(addBtn);
        
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        
     // Table
        String[] columns = { "Key", "Type", "Description" };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        addBtn.addActionListener(e -> {
            AddSubjectDialog dialog = new AddSubjectDialog(
                    SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                refreshData();
            }
        });
        
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(30);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(Theme.CARD_BG);

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Soggetto s : CatalogoSoggetti.getInstance().tuttiSoggetti()) {
            tableModel.addRow(new Object[] {
                    s.getKey(),
                    s.getClass().getSimpleName(),
                    s.getDescription()
            });
        }
    }
}
