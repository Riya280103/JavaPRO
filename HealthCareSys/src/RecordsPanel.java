import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class RecordsPanel extends JPanel {

    private JTable recordsTable;

    public RecordsPanel() {
        setLayout(new BorderLayout());

        // Initialize the table with an empty model
        recordsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load records from the database
        loadRecords();
    }

    private void loadRecords() {
        // Clear existing table data
        DefaultTableModel model = new DefaultTableModel();
        recordsTable.setModel(model);

        // Add columns to the table model
        model.addColumn("Patient ID");
        model.addColumn("Patient Name");
        model.addColumn("Age");
        model.addColumn("Gender");
        model.addColumn("Address");
        model.addColumn("Doctor");

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT p.patient_id, p.name, p.age, p.gender, p.address, d.doctor_name " +
                            "FROM patients p " +
                            "LEFT JOIN doctors d ON p.doctor_id = d.doctor_id " +
                            "ORDER BY p.patient_id");
            ResultSet rs = stmt.executeQuery();

            // Populate the table model with data
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("patient_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("age"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("address"));
                row.add(rs.getString("doctor_name"));
                model.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading records: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
