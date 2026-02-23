package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gestione.GestoreArchivi;
import gui.views.ArchivesView;
import gui.views.DashboardView;
import gui.views.PhotosView;
import gui.views.SettingsView;
import gui.views.SubjectsView;
import progettoarchivio.CatalogoSoggetti;

public class MainApplication extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private Sidebar sidebar;

    // View references for refresh
    private DashboardView dashboardView;
    private ArchivesView archivesView;
    private PhotosView photosView;
    private SubjectsView subjectsView;
    private SettingsView settingsView;

    public MainApplication() {
        setTitle("Photographic Archive Management System");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GestoreArchivi.getInstance().salvaSuFile();
                CatalogoSoggetti.getInstance().salvaSuFile();
                System.exit(0);
            }
        });

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.CONTENT_BG);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Theme.CONTENT_BG);

        sidebar = new Sidebar(this);

        // Initialize views
        dashboardView = new DashboardView();
        archivesView = new ArchivesView();
        photosView = new PhotosView();
        subjectsView = new SubjectsView();
        settingsView = new SettingsView();

        // Add views
        mainContentPanel.add(dashboardView, "dashboard");
        mainContentPanel.add(archivesView, "archives");
        mainContentPanel.add(photosView, "photos");
        mainContentPanel.add(subjectsView, "subjects");
        mainContentPanel.add(settingsView, "settings");

        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    public void switchView(String name) {
        // Refresh data before showing the view
        switch (name) {
            case "dashboard":
                dashboardView.refresh();
                break;
            case "archives":
                archivesView.refreshData();
                break;
            case "photos":
                photosView.refreshData();
                break;
            case "subjects":
                subjectsView.refreshData();
                break;
            case "settings":
                settingsView.refreshData();
                break;
        }
        cardLayout.show(mainContentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.setVisible(true);
            // Default select dashboard
            app.switchView("dashboard");
        });
    }
}
