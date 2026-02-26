import java.awt.*;           // for layouts, colors, fonts etc
import java.awt.event.*;     // for mouse events
import javax.swing.*;        // swing components
import javax.swing.table.*;  // for JTable
import java.sql.*;           // for database connection

public class DoctorManagement extends JFrame {

    // here I declared all text fields for doctor details
    JTextField txtId, txtName, txtSpecialization, txtPhone, txtEmail, txtRoom;

    // this model will store data of table
    DefaultTableModel model;

    // table to show doctor records
    JTable table;

    public DoctorManagement() {

        // setting basic properties of frame
        setTitle("Doctor Management");
        setSize(1100, 650);
        setLocationRelativeTo(null); // this makes window open in center
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); // using border layout for better structure

        // ===== HEADER PART =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 152, 219)); // blue color
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Doctor Management System");

        // here I used Segoe UI font because it looks clean
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241)); // light background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // form panel for entering doctor information
        JPanel formCard = new JPanel(new GridLayout(6,2,15,15));
        formCard.setBackground(Color.WHITE);

        // just adding border so it looks like card
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(20,20,20,20)
        ));

        // creating input boxes
        txtId = createField();
        txtName = createField();
        txtSpecialization = createField();
        txtPhone = createField();
        txtEmail = createField();
        txtRoom = createField();

        // adding labels and text fields one by one
        formCard.add(createLabel("Doctor ID")); formCard.add(txtId);
        formCard.add(createLabel("Name")); formCard.add(txtName);
        formCard.add(createLabel("Specialization")); formCard.add(txtSpecialization);
        formCard.add(createLabel("Phone")); formCard.add(txtPhone);
        formCard.add(createLabel("Email")); formCard.add(txtEmail);
        formCard.add(createLabel("Room No")); formCard.add(txtRoom);

        // ===== TABLE PART =====
        String[] columns = {"ID","Name","Specialization","Phone","Email","Room"};

        // model stores rows and columns
        model = new DefaultTableModel(columns,0);

        table = new JTable(model);

        // setting table design
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // making header bold
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52,152,219));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);

        // using split pane so left form and right table come side by side
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formCard, scrollPane);
        splitPane.setDividerLocation(450);
        splitPane.setDividerSize(5);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236,240,241));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnAdd = createButton("Add");
        JButton btnUpdate = createButton("Update");
        JButton btnDelete = createButton("Delete");
        JButton btnClear = createButton("Clear");
        JButton btnBack = createButton("Back");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // ===== DATABASE LOGIC =====

        // when Add button clicked
        btnAdd.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                // inserting new doctor into database
                String sql = "INSERT INTO doctor VALUES (?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, Integer.parseInt(txtId.getText())); // converting id to int
                pst.setString(2, txtName.getText());
                pst.setString(3, txtSpecialization.getText());
                pst.setString(4, txtPhone.getText());
                pst.setString(5, txtEmail.getText());
                pst.setString(6, txtRoom.getText());

                pst.executeUpdate(); // executing insert query
                con.close();

                loadDoctors(); // refresh table
                clearFields(); // clear form

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // when Update button clicked
        btnUpdate.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                String sql = "UPDATE doctor SET name=?, specialization=?, phone=?, email=?, room=? WHERE id=?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, txtName.getText());
                pst.setString(2, txtSpecialization.getText());
                pst.setString(3, txtPhone.getText());
                pst.setString(4, txtEmail.getText());
                pst.setString(5, txtRoom.getText());
                pst.setInt(6, Integer.parseInt(txtId.getText()));

                pst.executeUpdate();
                con.close();

                loadDoctors();
                clearFields();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // when Delete button clicked
        btnDelete.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                String sql = "DELETE FROM doctor WHERE id=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.executeUpdate();
                con.close();

                loadDoctors();
                clearFields();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // just clears text fields
        btnClear.addActionListener(e -> clearFields());

        // closes this window
        btnBack.addActionListener(e -> dispose());

        // when we click on table row, data comes back into text fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row,0).toString());
                txtName.setText(model.getValueAt(row,1).toString());
                txtSpecialization.setText(model.getValueAt(row,2).toString());
                txtPhone.setText(model.getValueAt(row,3).toString());
                txtEmail.setText(model.getValueAt(row,4).toString());
                txtRoom.setText(model.getValueAt(row,5).toString());
            }
        });

        // loading data when window opens
        loadDoctors();

        setVisible(true);
    }

    // this method loads all doctor data from database into table
    void loadDoctors() {
        try {
            model.setRowCount(0); // clearing old rows first

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM doctor");

            while(rs.next()) {
                String[] row = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("room")
                };
                model.addRow(row);
            }

            con.close();

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // this clears all input boxes
    void clearFields(){
        txtId.setText("");
        txtName.setText("");
        txtSpecialization.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtRoom.setText("");
    }

    // creating label with some styling
    JLabel createLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // normal font
        lbl.setForeground(new Color(44,62,80));
        return lbl;
    }

    // creating text field with border
    JTextField createField(){
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        return txt;
    }

    // creating button with blue color
    JButton createButton(String text){
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52,152,219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110,40));
        return btn;
    }

    public static void main(String[] args) {
        new DoctorManagement();
    }
}