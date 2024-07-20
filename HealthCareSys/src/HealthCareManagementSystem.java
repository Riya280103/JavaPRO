import javax.swing.*;

public class HealthCareManagementSystem extends JFrame {

    public HealthCareManagementSystem() {
        super("Health Care Management System"); // Set window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // initComponents();
        pack(); // Sizes the frame based on preferred sizes of subcomponents
        setLocationRelativeTo(null); // Center window on screen

        JPanel patientPanel = new PatientPanel();
        JPanel doctorAssignmentPanel = new DoctorAssignmentPanel();
        JPanel appointmentPanel = new AppointmentPanel();
        JPanel recordsPanel = new RecordsPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Register Patient", patientPanel);
        tabbedPane.addTab("Assign Doctor", doctorAssignmentPanel); // Added new tab for doctor assignment
        tabbedPane.addTab("Schedule Appointment", appointmentPanel);
        tabbedPane.addTab("View Records", recordsPanel);
        // tabbedPane.addTab("View Records", doctorPanel);

        add(tabbedPane);

        setVisible(true);
    }

    /*
     * private void initComponents() {
     * JPanel mainPanel = new JPanel();
     * mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical
     * layout
     * 
     * JLabel titleLabel = new JLabel("Welcome to Health Care Management System");
     * mainPanel.add(titleLabel);
     * 
     * JButton registerPatientButton = new JButton("Register Patient");
     * mainPanel.add(registerPatientButton);
     * 
     * registerPatientButton.addActionListener(e -> {
     * // Prompt user for patient details (using JOptionPane)
     * String name = JOptionPane.showInputDialog(this, "Enter patient name:");
     * String address = JOptionPane.showInputDialog(this, "Enter patient address:");
     * int age = 0;
     * try {
     * age = Integer.parseInt(JOptionPane.showInputDialog(this,
     * "Enter patient age:"));
     * } catch (NumberFormatException ex) {
     * JOptionPane.showMessageDialog(this,
     * "Invalid age format. Please enter a number.", "Error",
     * JOptionPane.ERROR_MESSAGE);
     * return; // Exit the action listener if age input is invalid
     * }
     * 
     * // Insert patient details into the patients table
     * String sql = "INSERT INTO patients (name, address, age) VALUES (?, ?, ?)";
     * try (Connection conn = DatabaseConnection.getConnection();
     * PreparedStatement pstmt = conn.prepareStatement(sql)) {
     * pstmt.setString(1, name);
     * pstmt.setString(2, address);
     * pstmt.setInt(3, age);
     * int rowsInserted = pstmt.executeUpdate();
     * 
     * if (rowsInserted > 0) {
     * JOptionPane.showMessageDialog(this, "Patient registered successfully!");
     * } else {
     * JOptionPane.showMessageDialog(this, "Failed to register patient.", "Error",
     * JOptionPane.ERROR_MESSAGE);
     * }
     * 
     * } catch (SQLException ex) {
     * ex.printStackTrace();
     * JOptionPane.showMessageDialog(this, "Error registering patient: " +
     * ex.getMessage(), "Error",
     * JOptionPane.ERROR_MESSAGE);
     * }
     * });
     * 
     * getContentPane().add(mainPanel);
     * }
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HealthCareManagementSystem gui = new HealthCareManagementSystem();
            gui.setVisible(true);

        });
    }
}