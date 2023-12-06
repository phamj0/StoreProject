/**
 * User class that represents the user with a username, password, and 
 * user type: Customer or Seller.
 * 
 */
public class User {
    private String username;
    private String password;
    private String userType;

    /**
     * User constructor that creates a new user with the 
     * specified username, password, and user type.
     * @param username is the username of the user. Can't be empty or null.
     * @param password is the password of the user. Can't be empty or null.
     * @param userType is the user type: either Customer or Seller. Can't be null.
     */
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    /**
     * Gets the username of the user.
     * @return username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     * @return password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user type.
     * @return user type either "Customer" or "Seller".
     */
    public String getUserType() {
        return userType;
    }
}
