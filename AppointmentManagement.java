import java.awt.*;                  // for layout, color, font
import java.awt.event.*;            // for mouse events
import javax.swing.*;               // for swing components
import javax.swing.table.DefaultTableModel;  // for table model
import java.sql.*;                  // for database connection

public class AppointmentManagement extends JFrame {

    // text fields to take input from user
    JTextField txtAppId, txtPatientId, txtDoctorId, txtDate, txtTime;

    // model connects JTable with database data
    DefaultTableModel model;
    JTable table;

    public AppointmentManagement() {

        // basic frame setup
        setTitle("Appointment Management");
        setSize(1000, 600);

        // this will open window in center
        setLocationRelativeTo(null);

        // this window will close only this screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // using BorderLayout because it is simple to divide top, center, bottom
        setLayout(new BorderLayout());

        // ================= HEADER SECTION =================

        JPanel header = new JPanel();

        // blue header color
        header.setBackground(new Color(41, 128, 185));

        // adding some spacing so it looks clean
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel title = new JLabel("Appointment Management");

        // setting font style
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        header.add(title);

        // adding header at top
        add(header, BorderLayout.NORTH);

        // ================= MAIN PANEL =================

        JPanel mainPanel = new JPanel(new BorderLayout());

        // light background
        mainPanel.setBackground(new Color(236, 240, 241));

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // ================= FORM PANEL =================

        // using GridLayout for proper alignment
        JPanel formPanel = new JPanel(new GridLayout(5,2,15,15));

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(20,20,20,20)
            )
        );

        // creating input fields
        txtAppId = createField();
        txtPatientId = createField();
        txtDoctorId = createField();
        txtDate = createField();
        txtTime = createField();

        // adding labels and fields
        formPanel.add(createLabel("Appointment ID"));
        formPanel.add(txtAppId);

        formPanel.add(createLabel("Patient ID"));
        formPanel.add(txtPatientId);

        formPanel.add(createLabel("Doctor ID"));
        formPanel.add(txtDoctorId);

        // telling user format so no error happens
        formPanel.add(createLabel("Date (yyyy-mm-dd)"));
        formPanel.add(txtDate);

        formPanel.add(createLabel("Time (HH:mm:ss)"));
        formPanel.add(txtTime);

        // placing form at left side
        mainPanel.add(formPanel, BorderLayout.WEST);

        // ================= TABLE SECTION =================

        // table columns
        String[] columns = {"App ID","Patient ID","Doctor ID","Date","Time"};

        model = new DefaultTableModel(columns,0);
        table = new JTable(model);

        // styling table
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(new Color(41,128,185));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);

        // adding table at center
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON SECTION =================

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236, 240, 241));
        buttonPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 0, 0, 0)
        );

        JButton btnBook = createButton("Book Appointment");
        JButton btnCancel = createButton("Cancel Appointment");

        buttonPanel.add(btnBook);
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ================= DATABASE LOGIC =================

        // when booking appointment
        btnBook.addActionListener(e -> {
            try {

                // getting database connection
                Connection con = DBConnection.getConnection();

                // simple insert query
                String sql = "INSERT INTO appointment VALUES (?,?,?,?,?)";

                PreparedStatement pst = con.prepareStatement(sql);

                // converting text to proper data types
                pst.setInt(1, Integer.parseInt(txtAppId.getText()));
                pst.setInt(2, Integer.parseInt(txtPatientId.getText()));
                pst.setInt(3, Integer.parseInt(txtDoctorId.getText()));

                // converting string to SQL Date and Time
                pst.setDate(4, Date.valueOf(txtDate.getText()));
                pst.setTime(5, Time.valueOf(txtTime.getText()));

                pst.executeUpdate();

                con.close();

                // refresh table after insert
                loadAppointments();

                // clear input fields
                clearFields();

            } catch(Exception ex) {

                // if format is wrong or DB error
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // when canceling appointment
        btnCancel.addActionListener(e -> {
            try {

                Connection con = DBConnection.getConnection();

                String sql = 
                    "DELETE FROM appointment WHERE app_id=?";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(txtAppId.getText()));

                pst.executeUpdate();

                con.close();

                loadAppointments();
                clearFields();

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // if user clicks row in table
        // data will automatically fill in text fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                txtAppId.setText(model.getValueAt(row,0).toString());
                txtPatientId.setText(model.getValueAt(row,1).toString());
                txtDoctorId.setText(model.getValueAt(row,2).toString());
                txtDate.setText(model.getValueAt(row,3).toString());
                txtTime.setText(model.getValueAt(row,4).toString());
            }
        });

        // loading data when window opens
        loadAppointments();

        setVisible(true);
    }

    // this method loads all appointments from database
    void loadAppointments() {
        try {

            model.setRowCount(0); // clearing old data

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = 
                st.executeQuery("SELECT * FROM appointment");

            while(rs.next()) {

                String[] row = {
                    String.valueOf(rs.getInt("app_id")),
                    String.valueOf(rs.getInt("patient_id")),
                    String.valueOf(rs.getInt("doctor_id")),
                    rs.getDate("app_date").toString(),
                    rs.getTime("app_time").toString()
                };

                model.addRow(row);
            }

            con.close();

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // clearing all input fields
    void clearFields(){
        txtAppId.setText("");
        txtPatientId.setText("");
        txtDoctorId.setText("");
        txtDate.setText("");
        txtTime.setText("");
    }

    // reusable label method
    JLabel createLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(44,62,80));
        return lbl;
    }

    // reusable text field method
    JTextField createField(){
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(
            BorderFactory.createLineBorder(new Color(200,200,200))
        );
        return txt;
    }

    // reusable button method
    JButton createButton(String text){
        JButton btn = new JButton(text);
        btn.setBackground(new Color(41,128,185));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(180,40));
        return btn;
    }

    public static void main(String[] args) {

        // starting point of this screen
        new AppointmentManagement();
    }
}