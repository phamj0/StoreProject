/**
 * Shopping Cart Application
 * Main class that initializes the login view and the controller
 * Also displays the login view.
 */
public class Application {
    public static void main(String[] args) {
        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.setVisible(true);
    }
}
