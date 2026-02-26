package view;

import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

/**
 * Finestra principale del sistema di gestione archivi fotografici.
 */
public class ArchivioFrame extends JFrame {

    private static final Color BG_DARK    = new Color(30, 30, 40);
    private static final Color BG_PANEL   = new Color(42, 42, 58);
    private static final Color BG_CARD    = new Color(55, 55, 75);
    private static final Color ACCENT     = new Color(100, 149, 237);
    private static final Color ACCENT_RED = new Color(220, 80, 80);
    private static final Color TEXT_MAIN  = new Color(230, 230, 240);
    private static final Color TEXT_SUB   = new Color(160, 160, 180);
    private static final Font  FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_BTN   = new Font("Segoe UI", Font.BOLD, 13);

    private final GestoreArchivi gestore;
    private String archivioSelezionato = null;

    private DefaultListModel<String> listaArchiviModel;
    private JList<String>            listaArchivi;
    private FotografiaTableModel     tableModel;
    private JTable                   tabella;
    private JLabel                   labelArchivioCorrente;
    private JLabel                   labelStats;

    public ArchivioFrame(GestoreArchivi gestore) {
        this.gestore = gestore;
        setTitle("Photographic Archive Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { gestore.salvaSuFile(); }
        });
        initUI();
        aggiornaListaArchivi();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildSidebar(),   BorderLayout.WEST);
        add(buildMainPanel(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(20, 20, 30));
        p.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("  Archivio Fotografico");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_MAIN);
        labelStats = new JLabel("0 archivi  |  0 fotografie");
        labelStats.setFont(FONT_LABEL);
        labelStats.setForeground(TEXT_SUB);
        p.add(title,      BorderLayout.WEST);
        p.add(labelStats, BorderLayout.EAST);
        return p;
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_PANEL);
        p.setPreferredSize(new Dimension(230, 0));
        p.setBorder(new EmptyBorder(16, 12, 16, 12));

        JLabel lbl = new JLabel("ARCHIVI");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(TEXT_SUB);
        lbl.setBorder(new EmptyBorder(0, 4, 6, 0));

        listaArchiviModel = new DefaultListModel<>();
        listaArchivi = new JList<>(listaArchiviModel);
        styleList(listaArchivi);
        listaArchivi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                archivioSelezionato = listaArchivi.getSelectedValue();
                aggiornaTabella();
                aggiornaLabelArchivio();
            }
        });

        JScrollPane scroll = new JScrollPane(listaArchivi);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));
        scroll.getViewport().setBackground(BG_CARD);

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        btnPanel.setBackground(BG_PANEL);
        btnPanel.add(makeBtn("+ Nuovo Archivio",   ACCENT,      e -> dialogNuovoArchivio()));
        btnPanel.add(makeBtn("- Elimina Archivio", ACCENT_RED,  e -> eliminaArchivio()));

        p.add(lbl,      BorderLayout.NORTH);
        p.add(scroll,   BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildMainPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        labelArchivioCorrente = new JLabel("<- Seleziona un archivio");
        labelArchivioCorrente.setFont(new Font("Segoe UI", Font.BOLD, 15));
        labelArchivioCorrente.setForeground(TEXT_MAIN);

        tableModel = new FotografiaTableModel(new ArrayList<>());
        tabella = new JTable(tableModel);
        styleTable(tabella);

        JScrollPane scrollTab = new JScrollPane(tabella);
        scrollTab.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
        scrollTab.getViewport().setBackground(BG_CARD);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setBackground(BG_DARK);
        top.add(labelArchivioCorrente, BorderLayout.NORTH);
        top.add(buildSearchBar(),      BorderLayout.SOUTH);

        p.add(top,       BorderLayout.NORTH);
        p.add(scrollTab, BorderLayout.CENTER);
        p.add(buildToolbar(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildToolbar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setBackground(BG_DARK);
        p.add(makeBtn("+ Aggiungi Foto",  ACCENT,     e -> dialogAggiungiFoto()));
        p.add(makeBtn("- Rimuovi Foto",   ACCENT_RED, e -> rimuoviFoto()));
        p.add(makeBtn("i  Dettagli Foto", BG_CARD,    e -> mostraDettagli()));
        return p;
    }

    private JPanel buildSearchBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setBackground(BG_DARK);
        JLabel lbl = new JLabel("Cerca:");
        lbl.setForeground(TEXT_SUB);
        lbl.setFont(FONT_LABEL);
        JTextField tfRicerca = new JTextField(18);
        styleField(tfRicerca);
        String[] opzioni = {"Per autore", "Per stato", "Tutte le foto"};
        JComboBox<String> combo = new JComboBox<>(opzioni);
        styleCombo(combo);
        JButton btnCerca = makeBtn("Cerca", ACCENT, null);
        btnCerca.addActionListener(e -> {
            String query = tfRicerca.getText().trim();
            int sel = combo.getSelectedIndex();
            List<Fotografia> risultati;
            if (sel == 0) {
                risultati = gestore.cercaPerAutore(query);
            } else if (sel == 1) {
                try {
                    StatoConservazione stato = StatoConservazione.fromString(query);
                    risultati = gestore.filtraPerStato(stato);
                } catch (IllegalArgumentException ex) {
                    mostraErrore("Stato non valido. Usa: BUONO, DANEGGIATO, PESSIMO, RESTAURATO");
                    return;
                }
            } else {
                risultati = gestore.getTutteLeFotografie();
            }
            tableModel.aggiornaDati(risultati);
            labelArchivioCorrente.setText("Risultati: " + risultati.size() + " foto trovate");
        });
        JButton btnReset = makeBtn("Reset", BG_CARD, e -> {
            tfRicerca.setText("");
            aggiornaTabella();
            aggiornaLabelArchivio();
        });
        p.add(lbl); p.add(tfRicerca); p.add(combo); p.add(btnCerca); p.add(btnReset);
        return p;
    }

    // ── Aggiornamenti stato ──────────────────────────────────────────────────

    private void aggiornaListaArchivi() {
        String sel = archivioSelezionato;
        listaArchiviModel.clear();
        gestore.getArchivi().keySet().stream().sorted().forEach(listaArchiviModel::addElement);
        int tot = gestore.getTutteLeFotografie().size();
        labelStats.setText(listaArchiviModel.size() + " archivi  |  " + tot + " fotografie");
        if (sel != null && listaArchiviModel.contains(sel)) {
            listaArchivi.setSelectedValue(sel, true);
        }
    }

    private void aggiornaTabella() {
        if (archivioSelezionato == null) { tableModel.aggiornaDati(new ArrayList<>()); return; }
        Archivio a = gestore.getArchivio(archivioSelezionato);
        if (a != null) tableModel.aggiornaDati(new ArrayList<>(a.getFotografie()));
        aggiornaListaArchivi();
    }

    private void aggiornaLabelArchivio() {
        if (archivioSelezionato == null) {
            labelArchivioCorrente.setText("<- Seleziona un archivio");
        } else {
            Archivio a = gestore.getArchivio(archivioSelezionato);
            int n = (a != null) ? a.getFotografie().size() : 0;
            labelArchivioCorrente.setText("Archivio: " + archivioSelezionato + "   (" + n + " fotografie)");
        }
    }

    // ── Dialog: Nuovo Archivio ───────────────────────────────────────────────

    private void dialogNuovoArchivio() {
        JDialog d = new JDialog(this, "Nuovo Archivio", true);
        d.setSize(420, 340);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(BG_PANEL);
        d.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBackground(BG_PANEL);
        form.setBorder(new EmptyBorder(16, 16, 8, 16));

        JTextField tfNome      = addFormRow(form, "Nome archivio*:");
        JTextField tfRespons   = addFormRow(form, "Responsabile*:");
        JTextField tfIndirizzo = addFormRow(form, "Indirizzo*:");
        JTextField tfTelefono  = addFormRow(form, "Telefono*:");
        JTextField tfOrario    = addFormRow(form, "Orario (es. 09:00 - 18:00)*:");

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(BG_PANEL);
        btnRow.setBorder(new EmptyBorder(0, 0, 12, 12));
        btnRow.add(makeBtn("Annulla", BG_CARD, e -> d.dispose()));
        btnRow.add(makeBtn("Crea", ACCENT, e -> {
            try {
                Responsabile r = new Responsabile(
                    tfRespons.getText(), tfIndirizzo.getText(),
                    tfTelefono.getText(), tfOrario.getText());
                Archivio a = new Archivio(tfNome.getText(), r);
                gestore.aggiungiArchivio(a);
                aggiornaListaArchivi();
                d.dispose();
                mostraInfo("Archivio creato!");
            } catch (Exception ex) { mostraErrore(ex.getMessage()); }
        }));

        d.add(form,   BorderLayout.CENTER);
        d.add(btnRow, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void eliminaArchivio() {
        if (archivioSelezionato == null) { mostraAvviso("Seleziona un archivio."); return; }
        int r = JOptionPane.showConfirmDialog(this,
            "Eliminare l'archivio \"" + archivioSelezionato + "\"?\nTutte le fotografie verranno perse.",
            "Conferma eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            gestore.eliminaArchivio(archivioSelezionato);
            archivioSelezionato = null;
            aggiornaListaArchivi();
            aggiornaTabella();
            aggiornaLabelArchivio();
        }
    }

    // ── Dialog: Aggiungi Foto ────────────────────────────────────────────────

    private void dialogAggiungiFoto() {
        if (archivioSelezionato == null) { mostraAvviso("Seleziona un archivio."); return; }

        JDialog d = new JDialog(this, "Aggiungi Fotografia", true);
        d.setSize(500, 620);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(BG_PANEL);
        d.setLayout(new BorderLayout(10, 10));

        // ── Pannello campi fissi (sempre visibili) ──────────────────────────
        JPanel formFisso = new JPanel(new GridLayout(0, 2, 8, 8));
        formFisso.setBackground(BG_PANEL);
        formFisso.setBorder(new EmptyBorder(16, 16, 8, 16));

        JTextField tfId     = addFormRow(formFisso, "ID Foto*:");
        JTextField tfTitolo = addFormRow(formFisso, "Titolo*:");
        JTextField tfAutore = addFormRow(formFisso, "Autore*:");
        JTextField tfData   = addFormRow(formFisso, "Data (AAAA-MM-GG)*:");
        JTextField tfAlt    = addFormRow(formFisso, "Altezza (px)*:");
        JTextField tfLarg   = addFormRow(formFisso, "Larghezza (px)*:");

        formFisso.add(label("Stato conservazione*:"));
        JComboBox<StatoConservazione> comboStato = new JComboBox<>(StatoConservazione.values());
        styleCombo(comboStato);
        formFisso.add(comboStato);

        formFisso.add(label("Tipo soggetto*:"));
        JComboBox<String> comboTipo = new JComboBox<>(
            new String[]{"Luogo", "Personaggio", "Artista", "Oggetto", "Opera d'Arte"});
        styleCombo(comboTipo);
        formFisso.add(comboTipo);

        // ── Pannello campi dinamici (cambia in base al tipo soggetto) ───────
        JPanel formDinamico = new JPanel(new GridLayout(0, 2, 8, 8));
        formDinamico.setBackground(BG_PANEL);
        formDinamico.setBorder(new EmptyBorder(0, 16, 8, 16));

        // Mappa che tiene i JTextField dei campi dinamici correnti
        // Usiamo un array di riferimenti per poterli leggere nel bottone "Aggiungi"
        JTextField[] campiDinamici = new JTextField[6];

        // Metodo che ricostruisce il pannello dinamico in base al tipo scelto
        Runnable aggiornaCampiDinamici = () -> {
            formDinamico.removeAll();
            String tipo = (String) comboTipo.getSelectedItem();

            switch (tipo) {
                case "Luogo" -> {
                    campiDinamici[0] = addFormRow(formDinamico, "Chiave* (A-Z 0-9):");
                    campiDinamici[1] = addFormRow(formDinamico, "Nome luogo*:");
                    campiDinamici[2] = addFormRow(formDinamico, "Descrizione (opz.):");
                    campiDinamici[3] = campiDinamici[4] = campiDinamici[5] = null;
                }
                case "Personaggio" -> {
                    campiDinamici[0] = addFormRow(formDinamico, "Chiave* (A-Z 0-9):");
                    campiDinamici[1] = addFormRow(formDinamico, "Nome*:");
                    campiDinamici[2] = addFormRow(formDinamico, "Sesso (M/F/A)*:");
                    campiDinamici[3] = addFormRow(formDinamico, "Anno nascita*:");
                    campiDinamici[4] = addFormRow(formDinamico, "Deceduto (true/false)*:");
                    campiDinamici[5] = null;
                }
                case "Artista" -> {
                    campiDinamici[0] = addFormRow(formDinamico, "Chiave* (A-Z 0-9):");
                    campiDinamici[1] = addFormRow(formDinamico, "Nome*:");
                    campiDinamici[2] = addFormRow(formDinamico, "Sesso (M/F/A)*:");
                    campiDinamici[3] = addFormRow(formDinamico, "Anno nascita*:");
                    campiDinamici[4] = addFormRow(formDinamico, "Deceduto (true/false)*:");
                    campiDinamici[5] = addFormRow(formDinamico, "Attività (es. Pittore)*:");
                }
                case "Oggetto" -> {
                    campiDinamici[0] = addFormRow(formDinamico, "Chiave* (A-Z 0-9):");
                    campiDinamici[1] = addFormRow(formDinamico, "Nome oggetto*:");
                    campiDinamici[2] = addFormRow(formDinamico, "Descrizione (opz.):");
                    campiDinamici[3] = campiDinamici[4] = campiDinamici[5] = null;
                }
                case "Opera d'Arte" -> {
                    campiDinamici[0] = addFormRow(formDinamico, "Chiave* (A-Z 0-9):");
                    campiDinamici[1] = addFormRow(formDinamico, "Nome opera*:");
                    campiDinamici[2] = addFormRow(formDinamico, "Artista*:");
                    campiDinamici[3] = addFormRow(formDinamico, "Luogo*:");
                    campiDinamici[4] = addFormRow(formDinamico, "Anno creazione*:");
                    campiDinamici[5] = null;
                }
            }

            // Ridisegna il pannello
            formDinamico.revalidate();
            formDinamico.repaint();
            d.revalidate();
            d.repaint();
        };

        // Aggiorna i campi ogni volta che l'utente cambia tipo soggetto
        comboTipo.addActionListener(e -> aggiornaCampiDinamici.run());

        // Carica i campi per il tipo di default (Luogo)
        aggiornaCampiDinamici.run();

        // ── Bottone Aggiungi ────────────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(BG_PANEL);
        btnRow.setBorder(new EmptyBorder(0, 0, 12, 12));
        btnRow.add(makeBtn("Annulla", BG_CARD, e -> d.dispose()));
        btnRow.add(makeBtn("Aggiungi", ACCENT, e -> {
            try {
                // Costruisce il soggetto leggendo i campiDinamici
                Soggetto soggetto = creaSoggettoAvanzato(
                    (String) comboTipo.getSelectedItem(), campiDinamici);

                Fotografia foto = new Fotografia(
                    tfId.getText(),
                    Integer.parseInt(tfAlt.getText().trim()),
                    Integer.parseInt(tfLarg.getText().trim()),
                    (StatoConservazione) comboStato.getSelectedItem(),
                    soggetto,
                    tfTitolo.getText(),
                    tfAutore.getText(),
                    LocalDate.parse(tfData.getText().trim())
                );
                gestore.aggiungiFotografia(archivioSelezionato, foto);
                gestore.salvaSuFile();
                aggiornaTabella();
                d.dispose();
                mostraInfo("Fotografia aggiunta!");
            } catch (ArchivioException ex) {
                mostraErrore(ex.getMessage());
            } catch (NumberFormatException ex) {
                mostraErrore("I campi numerici (altezza, larghezza, anno) devono essere numeri interi.");
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());
            }
        }));

        // ── Assembla tutto in un unico pannello scrollabile ─────────────────
        JPanel tuttoIlForm = new JPanel(new BorderLayout());
        tuttoIlForm.setBackground(BG_PANEL);
        tuttoIlForm.add(formFisso,    BorderLayout.NORTH);
        tuttoIlForm.add(formDinamico, BorderLayout.CENTER);

        JScrollPane scrollForm = new JScrollPane(tuttoIlForm);
        scrollForm.setBorder(null);
        scrollForm.getViewport().setBackground(BG_PANEL);

        d.add(scrollForm, BorderLayout.CENTER);
        d.add(btnRow,     BorderLayout.SOUTH);
        d.setVisible(true);
    }
    
    
    private Soggetto creaSoggettoAvanzato(String tipo, JTextField[] c) {
        // c[0] = chiave, c[1..5] = campi specifici per tipo
        return switch (tipo) {
            case "Luogo" ->
                new Luogo(
                    c[0].getText(),   // chiave
                    c[1].getText(),   // nome
                    c[2] != null ? c[2].getText() : ""  // descrizione opzionale
                );
            case "Personaggio" ->
                new Personaggio(
                    c[0].getText(),                        // chiave
                    c[1].getText(),                        // nome
                    c[2].getText().trim().charAt(0),       // sesso (M/F/A)
                    Boolean.parseBoolean(c[4].getText()),  // deceduto
                    Integer.parseInt(c[3].getText().trim()) // anno nascita
                );
            case "Artista" ->
                new Artista(
                    c[0].getText(),                        // chiave
                    c[1].getText(),                        // nome
                    c[2].getText().trim().charAt(0),       // sesso
                    Boolean.parseBoolean(c[4].getText()),  // deceduto
                    Integer.parseInt(c[3].getText().trim()), // anno nascita
                    c[5].getText()                         // attività
                );
            case "Oggetto" ->
                new Oggetto(
                    c[0].getText(),   // chiave
                    c[1].getText(),   // nome
                    c[2] != null ? c[2].getText() : ""  // descrizione opzionale
                );
            case "Opera d'Arte" ->
                new OperaArte(
                    c[0].getText(),                        // chiave
                    c[1].getText(),                        // nome opera
                    c[2].getText(),                        // artista
                    c[3].getText(),                        // luogo
                    Integer.parseInt(c[4].getText().trim()) // anno
                );
            default -> throw new IllegalArgumentException("Tipo soggetto non riconosciuto.");
        };
    }

    private void rimuoviFoto() {
        if (archivioSelezionato == null) { mostraAvviso("Seleziona un archivio."); return; }
        int row = tabella.getSelectedRow();
        if (row < 0) { mostraAvviso("Seleziona una fotografia dalla tabella."); return; }
        String idFoto = tableModel.getValueAt(row, 0).toString();
        int r = JOptionPane.showConfirmDialog(this,
            "Eliminare la fotografia con ID \"" + idFoto + "\"?",
            "Conferma", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            try {
                gestore.rimuoviFotografia(archivioSelezionato, idFoto);
                gestore.salvaSuFile();
                aggiornaTabella();
            } catch (ArchivioException ex) { mostraErrore(ex.getMessage()); }
        }
    }

    private void mostraDettagli() {
        if (archivioSelezionato == null) { mostraAvviso("Seleziona un archivio."); return; }
        int row = tabella.getSelectedRow();
        if (row < 0) { mostraAvviso("Seleziona una fotografia."); return; }
        String idFoto = tableModel.getValueAt(row, 0).toString();
        Archivio a = gestore.getArchivio(archivioSelezionato);
        if (a == null) return;
        Fotografia f = a.cercaFoto(idFoto);
        if (f == null) return;
        Responsabile resp = a.getResponsabile();

        JTextArea ta = new JTextArea(
            "ID: "         + f.getIdFoto() + "\n" +
            "Titolo: "     + f.getTitolo() + "\n" +
            "Autore: "     + f.getAutore() + "\n" +
            "Data: "       + f.getData() + "\n" +
            "Dimensioni: " + f.getAltezza() + " x " + f.getLarghezza() + " px\n" +
            "Stato: "      + f.getStatoConservazione() + "\n" +
            "Soggetto: "   + (f.getSoggetto() != null ? f.getSoggetto().getDescription() : "N/D") + "\n\n" +
            "Archivio: "   + archivioSelezionato + "\n" +
            "Responsabile: " + (resp != null ? resp.getNome() + " | " + resp.getTelefono() + " | " + resp.getOrarioApertura() : "N/D")
        );
        ta.setEditable(false);
        ta.setBackground(BG_PANEL);
        ta.setForeground(TEXT_MAIN);
        ta.setFont(FONT_LABEL);
        ta.setBorder(new EmptyBorder(16, 16, 16, 16));

        JDialog d = new JDialog(this, "Dettagli Fotografia - " + f.getTitolo(), true);
        d.setSize(420, 300);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(BG_PANEL);
        d.getContentPane().add(ta);
        d.setVisible(true);
    }

    // ── Helpers UI ──────────────────────────────────────────────────────────

    private JTextField addFormRow(JPanel p, String labelText) {
        p.add(label(labelText));
        JTextField tf = new JTextField();
        styleField(tf);
        p.add(tf);
        return tf;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_SUB);
        l.setFont(FONT_LABEL);
        return l;
    }

    private JButton makeBtn(String text, Color bg, ActionListener action) {
        JButton b = new JButton(text);
        b.setFont(FONT_BTN);
        b.setBackground(bg);
        b.setForeground(TEXT_MAIN);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setOpaque(true);
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(bg.brighter()); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(bg); }
        });
        if (action != null) b.addActionListener(action);
        return b;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_MAIN);
        tf.setCaretColor(TEXT_MAIN);
        tf.setFont(FONT_LABEL);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 100)),
            new EmptyBorder(4, 8, 4, 8)));
    }

    private void styleCombo(JComboBox<?> c) {
        c.setBackground(BG_CARD);
        c.setForeground(TEXT_MAIN);
        c.setFont(FONT_LABEL);
    }

    private void styleList(JList<String> list) {
        list.setBackground(BG_CARD);
        list.setForeground(TEXT_MAIN);
        list.setFont(FONT_LABEL);
        list.setSelectionBackground(ACCENT);
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(32);
        list.setBorder(new EmptyBorder(4, 8, 4, 8));
    }

    private void styleTable(JTable t) {
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_MAIN);
        t.setFont(FONT_LABEL);
        t.setRowHeight(28);
        t.setSelectionBackground(ACCENT);
        t.setSelectionForeground(Color.WHITE);
        t.setGridColor(new Color(60, 60, 80));
        t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(new Color(35, 35, 50));
        t.getTableHeader().setForeground(TEXT_SUB);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setFillsViewportHeight(true);
    }

    private void mostraErrore(String msg)  { JOptionPane.showMessageDialog(this, msg, "Errore",     JOptionPane.ERROR_MESSAGE);       }
    private void mostraAvviso(String msg)  { JOptionPane.showMessageDialog(this, msg, "Attenzione", JOptionPane.WARNING_MESSAGE);      }
    private void mostraInfo(String msg)    { JOptionPane.showMessageDialog(this, msg, "OK",         JOptionPane.INFORMATION_MESSAGE);  }
}