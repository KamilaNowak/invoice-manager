import com.nowak.demo.database.DatabaseUtils;
import com.nowak.demo.database.InvoiceDatabase;
import com.nowak.demo.models.login.User;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@EnabledOnJre(JRE.JAVA_8)
@DisplayName("Database tests")
class TestInvoiceDatabase {

    private static InvoiceDatabase invoiceDatabase;
    private static Connection connection;

    @BeforeAll
    static void setUp() {
        invoiceDatabase = new InvoiceDatabase();
        connection = DatabaseUtils.Connector.getConnection();
    }
    @Test
    void testConnection(){
        assertNotNull(connection);
    }
    @Test
    void testFindUserByUsername() {
        User user = new User(1, "test", "$2a$10$HHNwc6T2ZTPHqp/DZX0WcOdt34VeIqoFSFL.3J3zs87AJ31Syap72",
                "test@email.com", LocalDate.of(2000, 1, 1),
                LocalDate.of(2020, 4, 18));
        User userFromDb = invoiceDatabase.findUserByUsername("test");

        Assertions.assertEquals(user.getId(), userFromDb.getId());
        Assertions.assertEquals(user.getUsername(), userFromDb.getUsername());
        Assertions.assertEquals(user.getPassword(),userFromDb.getPassword());
        Assertions.assertEquals(user.getEmail(), userFromDb.getEmail());
        Assertions.assertEquals(user.getBirthDate(), userFromDb.getBirthDate());
        Assertions.assertEquals(user.getCreatedAt(), userFromDb.getCreatedAt());
    }

    @Test
    void testfindAllUsers() {
        ArrayList userList = invoiceDatabase.findAllUsers();

        Assertions.assertFalse(userList.isEmpty());

        User user = new User(1, "test", "$2a$10$HHNwc6T2ZTPHqp/DZX0WcOdt34VeIqoFSFL.3J3zs87AJ31Syap72",
                "test@email.com", LocalDate.of(2000, 1, 1),
                LocalDate.of(2020, 4, 18));

        assertThat(userList.contains(user));
    }

    @Test
    void testFindUserById() {
        User user = invoiceDatabase.findUserById(2);

        Assertions.assertEquals("johndoe", user.getUsername());
        Assertions.assertEquals("john@gmail.com", user.getEmail());
        Assertions.assertEquals("$2a$10$vFevsUj4CaxjDbVPp6qnW.TI9uclfhTi8X3CUHxsxpp5fC7mtKw/G", user.getPassword());
        Assertions.assertEquals(LocalDate.of(1999, 4, 2), user.getBirthDate());
    }

    @Test
    void testCheckIfAccountExists() {
        User user = new User(1, "test", "test",
                "test@gmail.com", LocalDate.of(2000, 1, 1),
                LocalDate.of(2020, 4, 18));
        boolean result = invoiceDatabase.checkIfAccountExists(user.getUsername(), user.getPassword());

        Assertions.assertTrue(result);
    }

    @Test
    void testCheckUsernameEmailAvailability() {
        boolean shouldReturnTrue = invoiceDatabase.checkUsernameEmailAvailability("test", "john@gmail.com");
        Assertions.assertTrue(shouldReturnTrue);

        boolean shouldReturnFalse = invoiceDatabase.checkUsernameEmailAvailability("ayrjwbqknt", "ibkffwbnap@emial.com");
        Assertions.assertFalse(shouldReturnFalse);
    }

    @Test
    void testInsertNewUser() {
        invoiceDatabase.insertNewUser("clarayu", "password123", "clarayu@mail.com",
                LocalDate.of(1980, 2, 1));
        User user = invoiceDatabase.findUserByUsername("clarayu");
        invoiceDatabase.deleteUserByUsername("clarayu");

        Assertions.assertTrue(user.getId() > 0);
        Assertions.assertTrue(user.getPassword().length() >= 60);
        Assertions.assertEquals("clarayu@mail.com", user.getEmail());
        Assertions.assertEquals(LocalDate.of(1980, 2, 1), user.getBirthDate());
    }

    @Test
    void testUpdateUser() {
        Integer userId = 3;
        String username = "aliciasmith", email = "asmith@yahoo.com";
        LocalDate birthDate = LocalDate.of(1990, 4, 23);
        boolean shouldReturnTrue = invoiceDatabase.updateUser(3, username, email, birthDate, "asmith");

        Assertions.assertTrue(shouldReturnTrue);
    }

    @Test
    void testGetPasswordByUserId() {
        String password;
        Integer userId = 1;
        password =invoiceDatabase.getPasswordByUserId(1);

        Assertions.assertTrue(password.length()>=60);
        Assertions.assertEquals("$2a$10$HHNwc6T2ZTPHqp/DZX0WcOdt34VeIqoFSFL.3J3zs87AJ31Syap72", password);
    }

    @Test
    void testDeleteUserByUsername(){
        invoiceDatabase.insertNewUser("6527040qv0xdbq0", "5thdzl8sxqmrg2n", "6527040qv0xdbq0@mail.com",
                LocalDate.of(1980, 2, 1));
        boolean shouldReturnTrue = invoiceDatabase.deleteUserByUsername("6527040qv0xdbq0");

        Assertions.assertTrue(shouldReturnTrue);
    }
}