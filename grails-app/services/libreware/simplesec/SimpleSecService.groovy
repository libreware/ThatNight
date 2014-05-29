package libreware.simplesec

import libreware.thatnight.common.GeneralService
import libreware.thatnight.user.User
import org.springframework.web.context.request.RequestContextHolder

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * While reading this do not forget that a service is a singleton.
 * This is the sec
 */
class SimpleSecService {
    SessionService sessionService

    /**
     * Monitors the connected users
     */
    Map<User,Date> connectedUsers

    SimpleSecService(){
        connectedUsers = new HashMap<User,Date>()
    }

    /**
     * Encrypts a passwords using MD5
     * @param String password the password to encrypt
     * @return hashedPassword
     * @throws NoSuchAlgorithmException if the JVM doesn't implement the MD5 Algorithm
     */
    public static String encrypt(password) throws NoSuchAlgorithmException{
        StringBuilder hashString = new StringBuilder();
        byte[] uniqueKey = password.getBytes();
        byte[] hash = MessageDigest.getInstance("MD5").digest(uniqueKey);

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }

    /**
     * Checks if a user is logged in and retruns true if yes
     * @param user the user
     * @return true if the user is logged in
     */
    public boolean isUserLoggedIn(User user){
        return (user != null && connectedUsers.containsKey(user))
    }

    /**
     * Checks if a user login + password are correct
     *
     * @param email the email
     * @param password the password
     * @param the session id (use SessionService.getId(session))
     *
     * @return true if the user data is correct
     * @throws IllegalAccessException if the user is not allowed to make a login attempt
     */
    public boolean attemptLogin(String email, String password) throws IllegalAccessException{
        def user = User.findByEmailAndHashedPassword(email,encrypt(password))
        def lastLoginAttempt = sessionService.getLastConnectionTime()

        if (GeneralService.getTime() < lastLoginAttempt){
            throw new IllegalAccessException("This session is not allowed to access this item until "+ lastLoginAttempt)
        } else {
            if ( user != null ) {
                sessionService.eraseAttempts()
                this.connectedUsers.put(user,GeneralService.getTime())
                sessionService.setUser(user)
                return true
            }else {
                sessionService.recordAttempt()
                return false
            }
        }
    }

    /**
     * logs a user out of the application
     * @return true if success false if the user is not logged in
     */
    public boolean logout(){
        def user = sessionService.getUser()
        if (user != null) {
            if (this.connectedUsers.containsKey(user)) this.connectedUsers.remove(user)
            sessionService.clean()
            // safe but not recognized by intellij
            RequestContextHolder.currentRequestAttributes().getSession().invalidate()
            return true
        }
        return false
    }
}
