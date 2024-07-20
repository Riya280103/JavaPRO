import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorAssignmentPanel extends JPanel {

    private JComboBox<String> patientComboBox;
    private JComboBox<String> doctorComboBox;

    public DoctorAssignmentPanel() {
        setLayout(new BorderLayout());

        // Fetch list of patients and doctors from database
        ArrayList<String> patientNames = fetchPatientNames();
        ArrayList<String> doctorNames = new ArrayList<>(fetchDoctorNames().keySet());

        // Patient selection
        JPanel patientPanel = new JPanel();
        patientPanel.add(new JLabel("Select Patient:"));
        patientComboBox = new JComboBox<>(patientNames.toArray(new String[0]));
        patientPanel.add(patientComboBox);

        // Doctor selection
        JPanel doctorPanel = new JPanel();
        doctorPanel.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>(doctorNames.toArray(new String[0]));
        doctorPanel.add(doctorComboBox);

        // Assign button
        JButton assignButton = new JButton("Assign Doctor");
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignDoctor();
            }
        });

        // Add components to panel
        JPanel formPanel = new JPanel(new GridLayout(3, 1));
        formPanel.add(patientPanel);
        formPanel.add(doctorPanel);
        formPanel.add(assignButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private ArrayList<String> fetchPatientNames() {
        ArrayList<String> patientNames = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM patients ORDER BY name");

            while (rs.next()) {
                patientNames.add(rs.getString("name"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientNames;
    }

    private Map<String, Integer> fetchDoctorNames() {
        Map<String, Integer> doctorMap = new HashMap<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT doctor_id, doctor_name FROM doctors ORDER BY doctor_name");

            while (rs.next()) {
                int doctorId = rs.getInt("doctor_id");
                String doctorName = rs.getString("doctor_name");
                doctorMap.put(doctorName, doctorId);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorMap;
    }

    private void assignDoctor() {
        String selectedPatient = (String) patientComboBox.getSelectedItem();
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();

        if (selectedPatient == null || selectedDoctor == null) {
            JOptionPane.showMessageDialog(this, "Please select both patient and doctor.");
            return;
        }

        // Retrieve doctorId from doctorMap
        Map<String, Integer> doctorMap = fetchDoctorNames();
        Integer doctorId = doctorMap.get(selectedDoctor);

        if (doctorId == null) {
            JOptionPane.showMessageDialog(this, "Selected doctor not found in database.");
            return;
        }

        /*
         * Connection conn = null;
         * PreparedStatement stmt = null;
         */

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE patients SET doctor_id = ? WHERE name = ?");
            stmt.setInt(1, doctorId);
            stmt.setString(2, selectedPatient);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Doctor assigned successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign doctor.");
            }

            stmt.close();
            conn.close(); // Close connection after use
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

}
