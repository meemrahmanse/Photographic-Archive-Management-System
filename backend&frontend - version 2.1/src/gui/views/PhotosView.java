package gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import gestione.Archivio;
import gestione.FotoAColore;
import gestione.Fotografia;
import gestione.GestoreArchivi;
import gui.Theme;

public class PhotosView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> archiveComboBox;
    private JTextField searchField;

    public PhotosView() {
        initUI();
        loadArchives();
        loadData(null);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Photographs Management");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(title, BorderLayout.WEST);

        // Filters and Actions
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filtersPanel.setBackground(Theme.CONTENT_BG);

        // Archive Selector
        filtersPanel.add(new JLabel("Archive:"));
        archiveComboBox = new JComboBox<>();
        archiveComboBox.setBackground(Theme.CARD_BG);
        archiveComboBox.addActionListener(e -> loadData((String) archiveComboBox.getSelectedItem()));
        filtersPanel.add(archiveComboBox);

        // Search
        searchField = new JTextField(15);
        searchField.setToolTipText("Search by Photo ID");
        filtersPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(this::performSearch);
        filtersPanel.add(searchBtn);

        // Add Photo Button
        JButton addBtn = new JButton("Add Photo");
        addBtn.setFont(Theme.FONT_BOLD);
        addBtn.setBackground(Theme.ACCENT_COLOR);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> {
            String selectedArchive = (String) archiveComboBox.getSelectedItem();
            if (selectedArchive == null || selectedArchive.equals("All Archives") || selectedArchive.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an archive first.");
                return;
            }
            AddPhotoDialog dialog = new AddPhotoDialog(javax.swing.SwingUtilities.getWindowAncestor(this),
                    selectedArchive);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadData(selectedArchive);
            }
        });
        filtersPanel.add(addBtn);
        headerPanel.add(filtersPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Size", "Condition", "Subject", "Type", "Print Type", "Archive" };
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadArchives() {
        archiveComboBox.removeAllItems();
        archiveComboBox.addItem("All Archives");
        for (String archName : GestoreArchivi.getInstance().getArchivi().keySet()) {
            archiveComboBox.addItem(archName);
        }
    }

    public void loadData(String archiveFilter) {
        tableModel.setRowCount(0);
        Collection<Archivio> archivi = GestoreArchivi.getInstance().getArchivi().values();

        if (archiveFilter != null && !archiveFilter.equals("All Archives")) {
            Archivio a = GestoreArchivi.getInstance().getArchivio(archiveFilter);
            if (a != null) {
                for (Fotografia f : a.getFotografie()) {
                    addPhotoToTable(f, a.getNomeArchivio());
                }
            }
        } else {
            for (Archivio a : archivi) {
                for (Fotografia f : a.getFotografie()) {
                    addPhotoToTable(f, a.getNomeArchivio());
                }
            }
        }
    }

    private void addPhotoToTable(Fotografia f, String archiveName) {
        String type = "B/W";
        String printType = "-";
        if (f instanceof FotoAColore) {
            FotoAColore fc = (FotoAColore) f;
            type = "Color";
            printType = fc.getTipoStampa();
        }

        tableModel.addRow(new Object[] {
                f.getIdFoto(),
                f.getDimensione(),
                f.getStatoConservazione(),
                f.getSoggetto().getKey(),
                type,
                printType,
                archiveName
        });
    }

    private void performSearch(ActionEvent e) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadData((String) archiveComboBox.getSelectedItem());
            return;
        }

        tableModel.setRowCount(0);
        for (Archivio archivio : GestoreArchivi.getInstance().getArchivi().values()) {
            Fotografia foto = archivio.cercaFoto(query);
            if (foto != null) {
                addPhotoToTable(foto, archivio.getNomeArchivio());
                break; // Found it
            }
        }
    }

    public void refreshData() {
        loadArchives();
        loadData((String) archiveComboBox.getSelectedItem());
    }
}
