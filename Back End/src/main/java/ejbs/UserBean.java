package ejbs;

import entities.User;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Random;

@Stateless
public class UserBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    private EmailBean emailBean;

    public User authenticate(final String username, final String password) throws
            Exception {
        User user = em.find(User.class, username);
        if (user != null &&
                user.getPassword().equals(User.hashPassword(password))) {
            return user;
        }
        throw new Exception("Failed logging in with username '" + username + "': unknown username or wrong password");
    }

    public List find(String username, String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.username LIKE :userUsername OR u.email LIKE :userEmail")
                .setParameter("userUsername", username)
                .setParameter("userEmail", email)
                .setMaxResults(1)
                .getResultList();
    }

    public void sendEmailWithCredentials(String email, String username, String password){
        emailBean.send(
                email,
                "[Website Notification] Your new password.",
                "Notficition from website: " + "\n Email: " + email + "\n Username: " + username + "\nPassword: " + password
        );
    }

    public String generateNewPassword(String username) throws MyEntityNotFoundException {
        User user = em.find(User.class, username);
        if (user == null) throw new MyEntityNotFoundException("User with username"+ username+ " not found.");

        String newPassword = generatePassword(6).toString();

        try {
            user.setPassword(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newPassword;
    }

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }
}
