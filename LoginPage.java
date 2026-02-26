import java.awt.*;              // for layout, colors, fonts
import javax.swing.*;           // for swing components
import java.sql.Connection;     // for database connection
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage {

    public LoginPage() {

        // creating main login window
        JFrame frame = new JFrame("Hospital Login");

        frame.setSize(900, 500);

        // I am using null layout so I can manually place components
        frame.setLayout(null);

        // when this window closes, whole program should close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this opens window in center of screen
        frame.setLocationRelativeTo(null);

        // ================= LEFT PANEL (JUST DESIGN) =================

        // this side is only for design and welcome message
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 450, 500);
        leftPanel.setBackground(new Color(173, 216, 230)); // light blue color
        leftPanel.setLayout(null);

        // big hello text
        JLabel helloLabel = new JLabel("HELLO!");

        // I used Arial font here because it looks simple
        helloLabel.setFont(new Font("Arial", Font.BOLD, 40));
        helloLabel.setBounds(120, 50, 300, 50);
        leftPanel.add(helloLabel);

        // small instruction text
        JLabel textLabel = new JLabel("Please enter your details to continue");
        textLabel.setBounds(90, 110, 300, 30);
        leftPanel.add(textLabel);

        // loading image from project folder
        ImageIcon icon = new ImageIcon("Login.png");

        // resizing image so it fits nicely
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(300, 250, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageLabel.setBounds(75, 150, 300, 250);
        leftPanel.add(imageLabel);

        frame.add(leftPanel);

        // ================= RIGHT PANEL (LOGIN FORM) =================

        // this side contains username and password fields
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(450, 0, 450, 500);
        rightPanel.setLayout(null);
        rightPanel.setBackground(Color.WHITE);

        JLabel loginTitle = new JLabel("Hospital Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 22));
        loginTitle.setBounds(140, 50, 200, 30);
        rightPanel.add(loginTitle);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(80, 120, 150, 25);
        rightPanel.add(userLabel);

        // text field where user enters username
        JTextField userText = new JTextField();
        userText.setBounds(80, 150, 250, 30);
        rightPanel.add(userText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(80, 200, 100, 25);
        rightPanel.add(passLabel);

        // password field hides the typed characters
        JPasswordField passText = new JPasswordField();
        passText.setBounds(80, 230, 250, 30);
        rightPanel.add(passText);

        // login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(80, 280, 250, 35);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        rightPanel.add(loginButton);

        frame.add(rightPanel);

        // ================= LOGIN BUTTON LOGIC =================

        // when login button is clicked
        loginButton.addActionListener(e -> {

            // getting text entered by user
            String username = userText.getText().trim();

            // converting password char array to string
            String password = new String(passText.getPassword()).trim();

            // checking if any field is empty
            if (username.isEmpty() || password.isEmpty()) {

                // showing message if fields are empty
                JOptionPane.showMessageDialog(frame, 
                        "Please enter username and password!");

                return; // stopping further execution
            }

            try {
                // getting connection from DBConnection class
                Connection con = DBConnection.getConnection();

                // writing SQL query to check if user exists
                // using ? so it is safer (PreparedStatement)
                String query = 
                    "SELECT * FROM users WHERE username=? AND password=?";

                PreparedStatement pst = con.prepareStatement(query);

                // setting values inside query
                pst.setString(1, username);
                pst.setString(2, password);

                // executing query
                ResultSet rs = pst.executeQuery();

                // if rs.next() is true, that means record is found
                if (rs.next()) {

                    JOptionPane.showMessageDialog(frame, 
                            "Login Successful!");

                    // closing login window
                    frame.dispose();

                    // opening dashboard
                    new Dashboard();
                }
                else {
                    // if no record found
                    JOptionPane.showMessageDialog(frame, 
                            "Invalid Username or Password!");
                }

                // closing connection after work is done
                con.close();

            } catch (Exception ex) {

                // if any database error happens
                JOptionPane.showMessageDialog(frame, 
                        "Database Error: " + ex.getMessage());
            }
        });

        // finally showing login window
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        // program starts here
        new LoginPage();
    }
}