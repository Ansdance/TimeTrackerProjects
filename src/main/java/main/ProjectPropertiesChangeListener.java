package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import main.model.Project;
import main.ui.ProjektDialog;

public class ProjectPropertiesChangeListener extends MouseAdapter implements FocusListener {

    Project project;

    ProjectTimeTracker cwp;

    void doRename() {
        ProjektDialog pd = new ProjektDialog();
        boolean hasResult = pd.open(project.getProjektName(), project.getProjektNumber());

        if (hasResult) {

            String name = pd.getProjectName();
            String number = pd.getProjectNumber();

            if (name == null) {
                return;
            }

            if (!cwp.isProjectNameAvailable(project, name, number)) {
                return;
            }

            if (name.length() > 15) {
                JOptionPane.showMessageDialog(cwp.getWindow(),
                        "Project cannot be renamed, maximum of 15 characters exceeded.");
                return;
            }

            if (name.trim().length() < 1) {
                JOptionPane.showMessageDialog(cwp.getWindow(),
                        "New name too short. Project cannot be renamed.");
                return;
            }

            project.renameTo(name, number);
        }
    }

    public ProjectPropertiesChangeListener(Project project, ProjectTimeTracker clockwork) {
        this.project = project;
        cwp = clockwork;
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        project.save();
    }

    public void mouseClicked(final MouseEvent me) {

        Object src = me.getSource();

        if (me.getButton() == 3) {
            final JPopupMenu menu = new JPopupMenu();

            if (src instanceof JRadioButton) {
                JMenuItem itm_maximize = menu.add("Change project...");
                itm_maximize.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doRename();
                        cwp.updateProject(project);
                    }
                });
            } else if (src instanceof JLabel) {
                JMenuItem itm_maximize = menu.add("Change comment...");
                itm_maximize.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cwp.editComment(project, project.getCurrentWorkAmount());
                        cwp.updateProject(project);
                    }
                });
            }
            menu.show(me.getComponent(), me.getX(), me.getY());
        }
    }

}
