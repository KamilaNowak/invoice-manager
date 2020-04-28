
import com.nowak.demo.controllers.LoginController;
import com.nowak.demo.models.invoices.CompanyInvoice;
import com.nowak.demo.pdf.PDFGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@EnabledOnJre(JRE.JAVA_8)
@DisplayName("Login Controller tests")
public class TestLoginController {

    private static LoginController loginController;

    @BeforeAll
    static void setUp() {
        loginController = new LoginController();
    }

    @Test
    void testGetLoggedUser() {
        String username = "test";
        Long userId = loginController.getLoggedUser(username);

        Assertions.assertNotNull(userId);
        Assertions.assertTrue(userId > 0);
    }

    @Test
    void testLogin() {
        String username = "test", password = "test";
        boolean shouldReturnTrue = loginController.login(username, password);

        Assertions.assertTrue(shouldReturnTrue);
    }


}
