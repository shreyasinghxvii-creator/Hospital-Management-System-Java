import java.awt.*;      // for layout, colors, font
import javax.swing.*;   // for swing components

public class Dashboard extends JFrame {

    public Dashboard() {

        // creating main window of dashboard
        setTitle("Hospital Dashboard");
        setSize(1000, 600);

        // this will open window in center of screen
        setLocationRelativeTo(null);

        // when we close dashboard whole app should close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // using BorderLayout because it is simple and easy to divide sections
        setLayout(new BorderLayout());

        // ================= HEADER SECTION =================

        // this is the top blue bar
        JPanel header = new JPanel();

        // blue color background
        header.setBackground(new Color(41, 128, 185));

        // adding some padding so text does not stick to edges
        header.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JLabel title = new JLabel("HOSPITAL MANAGEMENT SYSTEM");

        // using Segoe UI font because it looks modern
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        header.add(title);

        // adding header at top of frame
        add(header, BorderLayout.NORTH);

        // ================= MAIN AREA =================

        // here I used GridLayout so that 3 panels come side by side
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 40, 0));

        // light background color
        mainPanel.setBackground(new Color(236, 240, 241));

        // giving margin around panel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        // instead of writing same code 3 times
        // I made one method createCard()
        // and used it three times

        JPanel patientPanel = createCard(
                "Patient Management",
                "patient.png"
        );

        JPanel doctorPanel = createCard(
                "Doctor Management",
                "Doctor_appointment.jpeg"
        );

        JPanel appointmentPanel = createCard(
                "Appointment Management",
                "Appointment.jpeg"
        );

        // adding all cards into main panel
        mainPanel.add(patientPanel);
        mainPanel.add(doctorPanel);
        mainPanel.add(appointmentPanel);

        add(mainPanel, BorderLayout.CENTER);

        // finally making dashboard visible
        setVisible(true);
    }

    // this method creates one card (image + button)
    // so I don’t have to repeat code
    private JPanel createCard(String text, String imagePath) {

        JPanel panel = new JPanel();

        // using border layout for image on top and button below
        panel.setLayout(new BorderLayout());

        panel.setBackground(Color.WHITE);

        // simple border so it looks like card
        panel.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));

        // -------- IMAGE PART --------

        // loading image from project folder
        ImageIcon icon = new ImageIcon(imagePath);

        // resizing image so it fits nicely
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(img));

        // center aligning image
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // -------- BUTTON PART --------

        JButton button = new JButton(text);

        // styling button
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        // setting size so all buttons look same
        button.setPreferredSize(new Dimension(200, 50));

        // deciding which window to open
        // I am checking text name to decide
        if(text.contains("Patient")) {

            // open patient screen
            button.addActionListener(e -> new PatientManagement());
        }
        else if(text.contains("Doctor")) {

            // open doctor screen
            button.addActionListener(e -> new DoctorManagement());
        }
        else {

            // open appointment screen
            button.addActionListener(e -> new AppointmentManagement());
        }

        // adding image in center
        panel.add(imageLabel, BorderLayout.CENTER);

        // adding button at bottom
        panel.add(button, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {

        // program starts from here
        new Dashboard();
    }
}