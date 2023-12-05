import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Login controller that manages the user authentication and registration of the application.
 */
public class LoginController {
    private LoginView loginView;
    private Map<String, User> userDatabase;

    /**
     * LoginController constructor with the specified LoginView.
     * @param loginView view associated with this controller.
     */
    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userDatabase = new HashMap<>();

        this.loginView.addLoginListener(new LoginListener());
        this.loginView.addRegisterListener(new RegisterListener());
    }

    /**
     * Login listener that implements an actionListener to handle login attempts.
     * The Observer pattern used when listening to the login events.
     */
    class LoginListener implements ActionListener {
        
        /**
         * An action that is invoked when the login button is clicked.
         * @param e is the ActionEvent that represents the button click.
         */
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String userType = loginView.getUserType();

            if (userDatabase.containsKey(username) && userDatabase.get(username).getPassword().equals(password)) {
                if ("Customer".equals(userDatabase.get(username).getUserType())) {
                    loginView.displayErrorMessage("Login Successful as Customer!");
                    ProductView productView = new ProductView();
                    productView.setVisible(true);
                    loginView.dispose();
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

    /**
     * Registration listener that implements an actionListener to handle user registration.
     * The Observer pattern is used when listening to registration events.
     */
    class RegisterListener implements ActionListener {

        /**
         * An action that is Invoked when the register button is clicked.
         * @param e is the ActionEvent that represents the button click.
         */
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
