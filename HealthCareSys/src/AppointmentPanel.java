import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AppointmentPanel extends JPanel {

    private JComboBox<String> patientComboBox;
    private JComboBox<String> doctorComboBox;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField purposeField;
    private Map<String, Integer> doctorNames; // Declare doctorNames as a class field

    public AppointmentPanel() {
        setLayout(new BorderLayout());

        // Fetch list of patients and doctors from database
        ArrayList<String> patientNames = fetchPatientNames();
        doctorNames = fetchDoctorNames(); // Initialize doctorNames

        // Patient selection
        JPanel patientPanel = new JPanel();
        patientPanel.add(new JLabel("Select Patient:"));
        patientComboBox = new JComboBox<>(patientNames.toArray(new String[0]));
        patientPanel.add(patientComboBox);

        // Doctor selection
        JPanel doctorPanel = new JPanel();
        doctorPanel.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>(new Vector<>(doctorNames.keySet()));
        doctorPanel.add(doctorComboBox);

        // Appointment details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2));
        detailsPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        detailsPanel.add(dateField);

        detailsPanel.add(new JLabel("Time (HH:MM):"));
        timeField = new JTextField();
        detailsPanel.add(timeField);

        detailsPanel.add(new JLabel("Purpose:"));
        purposeField = new JTextField();
        detailsPanel.add(purposeField);

        // Schedule button
        JButton scheduleButton = new JButton("Schedule Appointment");
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleAppointment();
            }
        });

        // Add components to panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.add(patientPanel, BorderLayout.NORTH);
        formPanel.add(doctorPanel, BorderLayout.CENTER);
        formPanel.add(detailsPanel, BorderLayout.CENTER);
        formPanel.add(scheduleButton, BorderLayout.SOUTH);

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
            conn.close(); // Close connection after use
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientNames;
    }

    private Map<String, Integer> fetchDoctorNames() {
        Map<String, Integer> doctorNames = new HashMap<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT doctor_id, doctor_name FROM doctors ORDER BY doctor_name");

            while (rs.next()) {
                doctorNames.put(rs.getString("doctor_name"), rs.getInt("doctor_id"));
            }

            rs.close();
            stmt.close();
            conn.close(); // Close connection after use
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorNames;
    }

    private void scheduleAppointment() {
        String selectedPatient = (String) patientComboBox.getSelectedItem();
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String purpose = purposeField.getText().trim();

        // Validate input
        if (selectedPatient == null || selectedDoctor == null || date.isEmpty() || time.isEmpty()
                || purpose.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        int doctorId = doctorNames.get(selectedDoctor); // Get doctor ID from the map

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO appointments (patient_id, doctor_id, date, time, appointment_purpose) " +
                            "VALUES ((SELECT patient_id FROM patients WHERE name =?),?,?,?,?)");
            stmt.setString(1, selectedPatient);
            stmt.setInt(2, doctorId);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setTime(4, Time.valueOf(time + ":00")); // Assuming seconds are always :00
            stmt.setString(5, purpose);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Appointment scheduled successfully.");
                // Clear fields after successful scheduling
                dateField.setText("");
                timeField.setText("");
                purposeField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to schedule appointment.");
            }

            stmt.close();
            conn.close(); // Close connection after
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error.");
            e.printStackTrace();
        }
    }

}