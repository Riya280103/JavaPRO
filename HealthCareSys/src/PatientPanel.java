import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientPanel extends JPanel {

    private JTextField nameField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField addressField;

    public PatientPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);

        formPanel.add(new JLabel("Gender:"));
        genderField = new JTextField();
        formPanel.add(genderField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        JButton registerButton = new JButton("Register Patient");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerPatient();
            }
        });
        formPanel.add(registerButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void registerPatient() {
        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = genderField.getText().trim();
        String address = addressField.getText().trim();

        // Validate input
        if (name.isEmpty() || ageStr.isEmpty() || gender.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO patients (name, age, gender, address) VALUES (?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, address);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Patient registered successfully.");
                // Clear fields after successful registration
                nameField.setText("");
                ageField.setText("");
                genderField.setText("");
                addressField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register patient.");
            }

            stmt.close();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Invalid age format or database error.");
            e.printStackTrace();
        }
    }
}
