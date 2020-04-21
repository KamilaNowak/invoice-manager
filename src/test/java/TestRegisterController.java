import com.nowak.demo.controllers.LoginController;
import com.nowak.demo.controllers.RegisterController;
import com.nowak.demo.database.UserDatabase;
import com.nowak.demo.models.login.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.time.LocalDate;

@EnabledOnJre(JRE.JAVA_8)
@DisplayName("Register Controller tests")
public class TestRegisterController {

    private static RegisterController registerController;
    private static UserDatabase userDatabase;

    @BeforeAll
    static void setUp() {
        userDatabase = new UserDatabase();
        registerController = new RegisterController();
    }
    @Test
    void testRegister(){
        User user = new User(1, "test", "test",
                "test@gmail.com", LocalDate.of(2000, 1, 1), LocalDate.of(2020, 4, 18));
        boolean shouldReturnFalse = registerController.register(user.getUsername(),user.getPassword(),user.getEmail(),user.getBirthDate());

        Assertions.assertFalse(shouldReturnFalse);

        User user2 = new User(123, "qyv43n4g9w", "n45gvsk427",
                "90sg5p63zb@kbrm5u4v1q.com", LocalDate.of(2000, 1, 1), LocalDate.of(2020, 4, 18));

        boolean shouldReturnTrue = registerController.register(user2.getUsername(), user2.getPassword(),user2.getEmail(),user2.getBirthDate());
        userDatabase.deleteUserByUsername("qyv43n4g9w");

        Assertions.assertTrue(shouldReturnTrue);
    }
}
