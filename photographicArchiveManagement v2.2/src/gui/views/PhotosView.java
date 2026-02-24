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
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
        setBorder(new EmptyBorder(35, 40, 35, 40));

        // ── Header ──────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CONTENT_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 18, 0));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(Theme.CONTENT_BG);
        JLabel title = new JLabel("Photographs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_PRIMARY);
        JLabel sub = new JLabel("Browse and manage photographs across your archives.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(Theme.TEXT_SECONDARY);
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(sub);
        headerPanel.add(titleBlock, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ── Toolbar ──────────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setBackground(Theme.CONTENT_BG);
        toolbar.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel archiveLabel = new JLabel("Archive:");
        archiveLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        archiveLabel.setForeground(Theme.TEXT_SECONDARY);
        toolbar.add(archiveLabel);

        archiveComboBox = new JComboBox<>();
        archiveComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        archiveComboBox.setBackground(Theme.CARD_BG);
        archiveComboBox.setPreferredSize(new Dimension(180, 36));
        archiveComboBox.addActionListener(e -> loadData((String) archiveComboBox.getSelectedItem()));
        toolbar.add(archiveComboBox);

        toolbar.add(Box.createHorizontalStrut(8));

        searchField = new JTextField(16);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setToolTipText("Search by Photo ID…");
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        searchField.setPreferredSize(new Dimension(180, 36));
        toolbar.add(searchField);

        JButton searchBtn = makeBtn("Search", Theme.ACCENT_HOVER, Color.WHITE, this::performSearch);
        searchBtn.setPreferredSize(new Dimension(90, 36));
        toolbar.add(searchBtn);

        toolbar.add(Box.createHorizontalStrut(16));

        JButton deleteBtn = makeBtn("Delete Photo", new Color(220, 53, 69), Color.WHITE, e -> deleteSelected());
        JButton addBtn = makeBtn("+ Add Photo", Theme.ACCENT_COLOR, Color.WHITE, e -> addPhoto());
        toolbar.add(deleteBtn);
        toolbar.add(addBtn);

        add(toolbar, BorderLayout.BEFORE_FIRST_LINE);
        // insert toolbar between header and center using a north-wrapper
        JPanel northWrap = new JPanel(new BorderLayout());
        northWrap.setBackground(Theme.CONTENT_BG);
        northWrap.add(headerPanel, BorderLayout.NORTH);
        northWrap.add(toolbar, BorderLayout.CENTER);
        // replace the north we already added
        remove(headerPanel);
        add(northWrap, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────────────────
        String[] columns = { "ID", "Size", "Condition", "Subject", "Type", "Print Type", "Archive" };
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

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

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
                // Color badge for type
                if (col == 4) {
                    setForeground("Color".equals(val) ? new Color(22, 163, 74) : Theme.TEXT_SECONDARY);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    setForeground(Theme.TEXT_PRIMARY);
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
                return this;
            }
        };
        for (int i = 0; i < columns.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CARD_BG);

        ArchivesView.RoundedCard card = new ArchivesView.RoundedCard(16, Theme.CARD_BG);
        card.setLayout(new BorderLayout());
        card.add(scrollPane, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void addPhoto() {
        String selectedArchive = (String) archiveComboBox.getSelectedItem();
        if (selectedArchive == null || selectedArchive.equals("All Archives") || selectedArchive.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a specific archive first.", "Archive Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        AddPhotoDialog dialog = new AddPhotoDialog(SwingUtilities.getWindowAncestor(this), selectedArchive);
        dialog.setVisible(true);
        if (dialog.isSaved())
            loadData(selectedArchive);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a photo to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String photoId = String.valueOf(tableModel.getValueAt(row, 0));
        String archName = String.valueOf(tableModel.getValueAt(row, 6));
        int confirm = JOptionPane.showConfirmDialog(this,
                "<html>Delete photo ID <b>" + photoId + "</b> from archive <b>" + archName + "</b>?</html>",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            Archivio a = GestoreArchivi.getInstance().getArchivio(archName);
            if (a != null) {
                a.rimuoviFoto(photoId);
                loadData((String) archiveComboBox.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Photo deleted successfully.", "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void loadArchives() {
        archiveComboBox.removeAllItems();
        archiveComboBox.addItem("All Archives");
        for (String n : GestoreArchivi.getInstance().getArchivi().keySet())
            archiveComboBox.addItem(n);
    }

    public void loadData(String archiveFilter) {
        tableModel.setRowCount(0);
        Collection<Archivio> archivi = GestoreArchivi.getInstance().getArchivi().values();
        if (archiveFilter != null && !archiveFilter.equals("All Archives")) {
            Archivio a = GestoreArchivi.getInstance().getArchivio(archiveFilter);
            if (a != null)
                for (Fotografia f : a.getFotografie())
                    addPhotoToTable(f, a.getNomeArchivio());
        } else {
            for (Archivio a : archivi)
                for (Fotografia f : a.getFotografie())
                    addPhotoToTable(f, a.getNomeArchivio());
        }
    }

    private void addPhotoToTable(Fotografia f, String archiveName) {
        String type = "B/W", printType = "—";
        if (f instanceof FotoAColore) {
            type = "Color";
            printType = ((FotoAColore) f).getTipoStampa();
        }
        tableModel.addRow(new Object[] { f.getIdFoto(), f.getDimensione(), f.getStatoConservazione(),
                f.getSoggetto().getKey(), type, printType, archiveName });
    }

    private void performSearch(ActionEvent e) {
        String q = searchField.getText().trim();
        if (q.isEmpty()) {
            loadData((String) archiveComboBox.getSelectedItem());
            return;
        }
        tableModel.setRowCount(0);
        for (Archivio a : GestoreArchivi.getInstance().getArchivi().values()) {
            Fotografia f = a.cercaFoto(q);
            if (f != null) {
                addPhotoToTable(f, a.getNomeArchivio());
                break;
            }
        }
    }

    public void refreshData() {
        loadArchives();
        loadData((String) archiveComboBox.getSelectedItem());
    }

    private JButton makeBtn(String text, Color bg, Color fg, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.addActionListener(al);
        return b;
    }
}
