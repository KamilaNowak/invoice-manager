import com.nowak.demo.database.CountDates;
import com.nowak.demo.database.InvoicesDatabase;
import com.nowak.demo.database.InvoicesShowcaseDatabase;
import com.nowak.demo.database.UserDatabase;
import com.nowak.demo.models.items.ReceiverType;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

@EnabledOnJre(JRE.JAVA_8)
@DisplayName("Showcases test")
public class TestInvoicesShowcaseDatabase {

    private static InvoicesShowcaseDatabase showcaseDatabase;

    @BeforeAll
    static void setUp() {
        showcaseDatabase = new InvoicesShowcaseDatabase();
    }

    @Test
    void testGetCountDateCompanyInvoices() {
        ArrayList<CountDates> companyList = showcaseDatabase.getCountDateCompanyInvoices(ReceiverType.COMPANY);
        Assertions.assertNotNull(companyList);
        ArrayList<CountDates> personalList = showcaseDatabase.getCountDateCompanyInvoices(ReceiverType.PERSON);
        Assertions.assertNotNull(personalList);
    }
}
