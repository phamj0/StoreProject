import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private LoginView loginView;
    private Map<String, User> userDatabase;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userDatabase = new HashMap<>();

        this.loginView.addLoginListener(new LoginListener());
        this.loginView.addRegisterListener(new RegisterListener());
    }

    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String userType = loginView.getUserType();

            if (userDatabase.containsKey(username) && userDatabase.get(username).getPassword().equals(password)) {
                if ("Customer".equals(userDatabase.get(username).getUserType())) {
                    loginView.displayErrorMessage("Login Successful as Customer!");
                    ProductView productView = new ProductView();
                    productView.setVisible(true);
                    loginView.dispose(); // Close the login view
                } else if ("Seller".equals(userDatabase.get(username).getUserType())) {
                    loginView.displayErrorMessage("Login Successful as Seller!");
                    SellerView sellerView = new SellerView();
                    sellerView.setVisible(true);
                    loginView.dispose();
                } else {
                    loginView.displayErrorMessage("Invalid user type!");
                }
            } else {
                loginView.displayErrorMessage("Invalid credentials!");
            }
        }
    }

    class RegisterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String userType = loginView.getUserType();

            if (!userDatabase.containsKey(username)) {
                userDatabase.put(username, new User(username, password, userType));
                loginView.displayErrorMessage("Registration Successful as " + userType + "!");
            } else {
                loginView.displayErrorMessage("Username already exists!");
            }
        }
    }
}
