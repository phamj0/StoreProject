import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The LoginView class that represents the login interface of the application.
 */
public class LoginView extends JFrame {
    private JTextField usernameField = new JTextField(10);
    private JPasswordField passwordField = new JPasswordField(10);
    private JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"Customer", "Seller"});
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");

    /**
     * LoginView window constructor that implements UI components for username, password,
     * user type, login button, and registration button.
     */
    public LoginView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("User Type:"));
        formPanel.add(userTypeComboBox);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        styleButton(loginButton, new Color(100, 149, 237));
        styleButton(registerButton, new Color(30, 144, 255));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Styling for the JButton
     * @param button  is the JButton to style.
     * @param color  is the background color for the button.
     */
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    /**
     * Getters for Username, Password, and UserType.
     * @return  the entered Username, Password, and UserType.
     */
    public String getUsername() {
        return usernameField.getText();
    }
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    public String getUserType() {
        return userTypeComboBox.getSelectedItem().toString();
    }

    /**
     * ActionListener for the login and registration buttons.
     */
    void addLoginListener(ActionListener listenForLoginButton) {
        loginButton.addActionListener(listenForLoginButton);
    }
    void addRegisterListener(ActionListener listenForRegisterButton) {
        registerButton.addActionListener(listenForRegisterButton);
    }

    /**
     * Display for an error message with a specified error.
     * @param errorMessage  is the error message that will be displayed.
     */
    void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }
}
