package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import main.model.Project;
import main.ui.ProjectListWindow;
import main.ui.ProjektDialog;

public class ProjectListWindowListener implements ActionListener {

    private ProjectListWindow projectListWindow;
    private ProjectTimeTracker cw;

    public ProjectListWindowListener(ProjectTimeTracker cw, ProjectListWindow project) {
        this.cw = cw;
        projectListWindow = project;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == projectListWindow.getBtnCreateProject()) {
            ProjektDialog pd = new ProjektDialog();
            boolean hasResult = pd.open(null, null);

            if (!hasResult) {
                return;
            }

            String name = pd.getProjectName();
            String number = pd.getProjectNumber();

            if (name.length() > 15) {
                JOptionPane.showMessageDialog(projectListWindow,
                        "Project cannot be created, maximum number of 15 characters exceeded.");
                return;
            }

            if (name.trim().length() < 1) {
                JOptionPane.showMessageDialog(projectListWindow, "Project cannot be created without a name.");
                return;
            }

            cw.createProject(name, number);
        }

        if (src == projectListWindow.getBtnDeleteProject()) {
            // Projekt suchen das gel�scht werden soll
            Project toDelete = projectListWindow.getActiveProject();

            if (toDelete != null && JOptionPane.showConfirmDialog(projectListWindow,
                    "Should the project " + toDelete.getProjektName() + " really be deleted?", "Danger",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cw.deleteProject(toDelete);
            }

        }

        if (src == projectListWindow.getExportCombo()) {

            final int selectedIndex = projectListWindow.getExportCombo().getSelectedIndex();

            new Thread() {

                public void run() {

                    if (selectedIndex == 1) {
                        cw.exportReportInNotepad();
                    }

                    if (selectedIndex == 2) {
                        cw.exportReportToScreen();
                    }

                }

            }.start();

        }

    }
}
