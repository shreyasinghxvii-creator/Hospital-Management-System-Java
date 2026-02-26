import java.awt.*;        // for layouts, colors, fonts
import java.awt.event.*;  // for mouse events
import java.io.File;      // for loading image file
import javax.imageio.ImageIO;
import javax.swing.*;     // for swing components
import javax.swing.table.DefaultTableModel;
import java.sql.*;        // for database connection

public class PatientManagement extends JFrame {

    // here I declared all text fields for patient details
    JTextField txtId, txtName, txtAge, txtGender, txtPhone, txtDisease;

    // this model will store data for table
    DefaultTableModel model;

    // table to show patient records
    JTable table;

    public PatientManagement() {

        // setting basic frame properties
        setTitle("Patient Management");
        setSize(1000, 600);
        setLocationRelativeTo(null); // makes window open in center
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(230, 242, 255)); // light blue background

        // ================= HEADER =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(173, 216, 230));

        JLabel title = new JLabel("PATIENT MANAGEMENT", JLabel.CENTER);

        // I used Segoe UI font because it looks simple and clean
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(33, 37, 41));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // loading logo image (if file is present in folder)
        JLabel imageLabel;
        try {
            Image img = ImageIO.read(new File("logo.png"));
            ImageIcon icon = new ImageIcon(img.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        } catch (Exception ex) {
            // if image not found then just show empty label
            imageLabel = new JLabel();
        }

        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(imageLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ================= FORM SECTION =================
        // using GridLayout so labels and fields align properly
        JPanel formPanel = new JPanel(new GridLayout(6,2,15,15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // creating all text fields
        txtId = createField();
        txtName = createField();
        txtAge = createField();
        txtGender = createField();
        txtPhone = createField();
        txtDisease = createField();

        // adding labels and text fields one by one
        formPanel.add(createLabel("Patient ID")); formPanel.add(txtId);
        formPanel.add(createLabel("Name")); formPanel.add(txtName);
        formPanel.add(createLabel("Age")); formPanel.add(txtAge);
        formPanel.add(createLabel("Gender")); formPanel.add(txtGender);
        formPanel.add(createLabel("Phone")); formPanel.add(txtPhone);
        formPanel.add(createLabel("Disease")); formPanel.add(txtDisease);

        // ================= TABLE SECTION =================
        // this table will show all patients from database
        String[] columns = {"ID","Name","Age","Gender","Phone","Disease"};
        model = new DefaultTableModel(columns,0);
        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        // when I click on any row, it will fill form automatically
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();

                txtId.setText(model.getValueAt(row,0).toString());
                txtName.setText(model.getValueAt(row,1).toString());
                txtAge.setText(model.getValueAt(row,2).toString());
                txtGender.setText(model.getValueAt(row,3).toString());
                txtPhone.setText(model.getValueAt(row,4).toString());
                txtDisease.setText(model.getValueAt(row,5).toString());
            }
        });

        JPanel centerPanel = new JPanel(new GridLayout(1,2));
        centerPanel.setBackground(new Color(230, 242, 255));
        centerPanel.add(formPanel);
        centerPanel.add(scrollPane);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= BUTTON SECTION =================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 242, 255));

        JButton btnAdd = createButton("Add");
        JButton btnUpdate = createButton("Update");
        JButton btnDelete = createButton("Delete");
        JButton btnSearch = createButton("Search");
        JButton btnClear = createButton("Clear");
        JButton btnBack = createButton("Back");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ================= DATABASE OPERATIONS =================

        // when Add button clicked
        btnAdd.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                // inserting new patient into database
                String sql = "INSERT INTO patient VALUES (?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(txtId.getText())); // converting id to int
                pst.setString(2, txtName.getText());
                pst.setInt(3, Integer.parseInt(txtAge.getText()));
                pst.setString(4, txtGender.getText());
                pst.setString(5, txtPhone.getText());
                pst.setString(6, txtDisease.getText());

                pst.executeUpdate(); // execute insert
                con.close();

                loadPatients(); // refresh table
                clearFields();  // clear form

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // update existing patient
        btnUpdate.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                String sql = "UPDATE patient SET name=?, age=?, gender=?, phone=?, disease=? WHERE id=?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, txtName.getText());
                pst.setInt(2, Integer.parseInt(txtAge.getText()));
                pst.setString(3, txtGender.getText());
                pst.setString(4, txtPhone.getText());
                pst.setString(5, txtDisease.getText());
                pst.setInt(6, Integer.parseInt(txtId.getText()));

                pst.executeUpdate();
                con.close();

                loadPatients();
                clearFields();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // delete patient using ID
        btnDelete.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                String sql = "DELETE FROM patient WHERE id=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));

                pst.executeUpdate();
                con.close();

                loadPatients();
                clearFields();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // search patient by id
        btnSearch.addActionListener(e -> {
            try {
                model.setRowCount(0); // clearing table first

                Connection con = DBConnection.getConnection();
                String sql = "SELECT * FROM patient WHERE id=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));

                ResultSet rs = pst.executeQuery();

                while(rs.next()){
                    String[] row = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        String.valueOf(rs.getInt("age")),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("disease")
                    };
                    model.addRow(row);
                }

                con.close();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> dispose());

        // loading data when window opens
        loadPatients();
        setVisible(true);
    }

    // this method loads all patients into table
    void loadPatients() {
        try {
            model.setRowCount(0); // clear previous rows

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM patient");

            while (rs.next()) {
                String[] row = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    String.valueOf(rs.getInt("age")),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("disease")
                };
                model.addRow(row);
            }

            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // clears all text fields
    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtGender.setText("");
        txtPhone.setText("");
        txtDisease.setText("");
    }

    // creating label with same style everywhere
    JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(33, 37, 41));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return lbl;
    }

    // creating text field
    JTextField createField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return txt;
    }

    // creating button
    JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }
}