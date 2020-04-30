import com.nowak.demo.aws.S3Uploader;
import com.nowak.demo.database.InvoicesDatabase;
import com.nowak.demo.database.UserDatabase;
import com.nowak.demo.models.addresses.AddressDetails;
import com.nowak.demo.models.customers.Company;
import com.nowak.demo.models.customers.Customer;
import com.nowak.demo.models.customers.Owner;
import com.nowak.demo.models.invoices.CompanyInvoice;
import com.nowak.demo.models.invoices.PaymentMethod;
import com.nowak.demo.models.invoices.PersonalInvoice;
import com.nowak.demo.models.items.Item;
import com.nowak.demo.models.items.ItemCategory;
import com.nowak.demo.models.items.ReceiverType;
import com.nowak.demo.models.login.User;
import com.nowak.demo.pdf.PDFGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.nowak.demo.database.ResultSetUtilsKt.generateInvoiceNo;

@EnabledOnJre(JRE.JAVA_8)
@DisplayName("Invoices database Controller tests")
public class TestInvoicesDatabase {

    private static InvoicesDatabase invoicesDatabase;
    private static UserDatabase userDatabase;
    static AddressDetails sampleAddress = new AddressDetails(0, "Poland", "Krakow", "Mickiewicza", 188);
    static Owner sampleOwner = new Owner(0, "Maria", "Curie", "mcurie@email.com", 303606909, 102);

    @BeforeAll
    static void setUp() {
        invoicesDatabase = new InvoicesDatabase();
        userDatabase = new UserDatabase();
    }

    @Test
    void testInsertOwner() {
        Owner owner = new Owner(0, "Mark", "Dash", "mdash@email.com", 123456789, 593);
        int rowsAff = invoicesDatabase.insertOwner(owner);
        Assertions.assertTrue(rowsAff > 0);

        Owner inserted = invoicesDatabase.findOwnerByPid(owner.getPid());
        invoicesDatabase.deleteOwnerByPid(owner.getPid());

        Assertions.assertEquals(owner.getName(), inserted.getName());
        Assertions.assertEquals(owner.getSurname(), inserted.getSurname());
        Assertions.assertEquals(owner.getEmail(), inserted.getEmail());
        Assertions.assertEquals(owner.getPhoneNumber(), inserted.getPhoneNumber());
    }

    @Test
    void testInsertAddressDetails() {
        AddressDetails addressDetails = new AddressDetails(0, "England", "London",
                "Flower", 1);
        int rowsAff = invoicesDatabase.insertAddressDetails(addressDetails);

        Assertions.assertTrue(rowsAff > 0);

        AddressDetails fromDb = invoicesDatabase.findAddressDetails(addressDetails);
        invoicesDatabase.deleteAddressDetailsById(((int) fromDb.getId()));

        Assertions.assertNotNull(fromDb);
        Assertions.assertEquals(addressDetails.getCountry(), fromDb.getCountry());
        Assertions.assertEquals(addressDetails.getCity(), fromDb.getCity());
        Assertions.assertEquals(addressDetails.getStreet(), fromDb.getStreet());
        Assertions.assertEquals(addressDetails.getBuilding(), fromDb.getBuilding());
    }

    @Test
    void testFindOwnerByPid() {
        Owner owner = new Owner(0, "Mark", "Dash", "mdash@email.com", 123456789, 593);
        invoicesDatabase.insertOwner(owner);
        Owner ownerToFind = invoicesDatabase.findOwnerByPid(593);
        invoicesDatabase.deleteOwnerByPid(owner.getPid());

        Assertions.assertEquals(owner.getName(), ownerToFind.getName());
        Assertions.assertEquals(owner.getSurname(), ownerToFind.getSurname());
        Assertions.assertEquals(owner.getEmail(), ownerToFind.getEmail());
        Assertions.assertEquals(owner.getPhoneNumber(), ownerToFind.getPhoneNumber());
        Assertions.assertEquals(owner.getPid(), ownerToFind.getPid());
    }

    @Test
    void testDeleteOwnerByPid() {
        Owner owner = new Owner(0, "Maria", "Pe", "mpe@email.com", 104404707, 622);
        invoicesDatabase.insertOwner(owner);

        boolean shouldReturnTrue = invoicesDatabase.deleteOwnerByPid(owner.getPid());
        Assertions.assertTrue(shouldReturnTrue);
    }

    @Test
    void testFindAddressDetailsById() {
        invoicesDatabase.insertAddressDetails(sampleAddress);
        int addrID = (int) invoicesDatabase.findAddressDetails(sampleAddress).getId();

        AddressDetails addrFromDb = invoicesDatabase.findAddressDetailsById(addrID);
        invoicesDatabase.deleteAddressDetailsById(addrID);

        Assertions.assertNotNull(addrFromDb);
        Assertions.assertEquals(sampleAddress.getCity(), addrFromDb.getCity());
        Assertions.assertEquals(sampleAddress.getStreet(), addrFromDb.getStreet());
        Assertions.assertEquals(sampleAddress.getCountry(), addrFromDb.getCountry());
        Assertions.assertEquals(sampleAddress.getBuilding(), addrFromDb.getBuilding());
    }

    @Test
    void testFindAddressDetails() {
        invoicesDatabase.insertAddressDetails(sampleAddress);
        AddressDetails addrFromDb = invoicesDatabase.findAddressDetails(sampleAddress);

        Assertions.assertNotNull(addrFromDb);
        Assertions.assertEquals(sampleAddress.getCity(), addrFromDb.getCity());
        Assertions.assertEquals(sampleAddress.getStreet(), addrFromDb.getStreet());
        Assertions.assertEquals(sampleAddress.getCountry(), addrFromDb.getCountry());
        Assertions.assertEquals(sampleAddress.getBuilding(), addrFromDb.getBuilding());
    }

    @Test
    void testInsertCompany() {
        Company company = new Company(0, "ClothingCompany", 1000000000, sampleAddress, sampleOwner);
        boolean shouldReturnTrue = invoicesDatabase.insertCompany(company);
        invoicesDatabase.deleteCompanyByName(company.getCompanyName());

        Assertions.assertTrue(shouldReturnTrue);
    }

    @Test
    void testFindCompanyByName() {
        Company company = new Company(0, "ClothingCompany", 1000000000, sampleAddress, sampleOwner);
        invoicesDatabase.insertCompany(company);
        Company companyFromDb = invoicesDatabase.findCompanyByName("ClothingCompany");
        invoicesDatabase.deleteCompanyByName(company.getCompanyName());

        Assertions.assertNotNull(companyFromDb);
        Assertions.assertEquals(company.getCompanyName(), companyFromDb.getCompanyName());
        Assertions.assertEquals(company.getNip(), companyFromDb.getNip());
        Assertions.assertEquals(company.getAddress().getBuilding(), companyFromDb.getAddress().getBuilding());
        Assertions.assertEquals(company.getOwner().getPid(), companyFromDb.getOwner().getPid());
    }

    @Test
    void testDeleteCompanyByName() {
        Company company = new Company(0, "FoodCompany", 1000222222, sampleAddress, sampleOwner);
        invoicesDatabase.insertCompany(company);
        boolean shouldReturnTrue = invoicesDatabase.deleteCompanyByName(company.getCompanyName());

        Assertions.assertTrue(shouldReturnTrue);
    }

    @Test
    void testInsertCompanyInvoice() {
        Company company = new Company(0, "CarCompany", 1000222222, sampleAddress, sampleOwner);
        User sampleUser = userDatabase.findUserById(1);
        CompanyInvoice companyInvoice = new CompanyInvoice("INV2020419230912", LocalDate.now(), 1, PaymentMethod.PAYPAL, company, sampleUser);
        Item item = new Item(0, "Grey Wheels", 1223, 1, 18, ItemCategory.MATERIAL_ITEM, "");
        String invoiceNo = invoicesDatabase.insertCompanyInvoice(companyInvoice, FXCollections.observableArrayList(item));

        Assertions.assertNotNull(invoiceNo);

        CompanyInvoice inserted = invoicesDatabase.findCompanyInvoiceByInvoiceNo(invoiceNo);
        Assertions.assertNotNull(inserted);
        Assertions.assertEquals(companyInvoice.getCompany().getCompanyName(), Objects.requireNonNull(inserted).getCompany().getCompanyName());
        Assertions.assertEquals(company.getOwner().getEmail(), inserted.getCompany().getOwner().getEmail());
        Assertions.assertEquals(company.getAddress().getStreet(), inserted.getCompany().getAddress().getStreet());
        Assertions.assertEquals(company.getNip(), inserted.getCompany().getNip());
        Assertions.assertEquals(companyInvoice.getAmount(), inserted.getAmount());
        Assertions.assertEquals(companyInvoice.getDateOfIssue(), inserted.getDateOfIssue());
    }

    @Test
    void testInsertPersonalInvoice() {
        User sampleUser = userDatabase.findUserById(1);
        Customer customer = new Customer(0, "Mark", "Doe", "mdoe@email.com", 987654321, sampleAddress);
        PersonalInvoice personalInvoice = new PersonalInvoice("", LocalDate.now(), 1000, PaymentMethod.PAYPAL, 0, customer, sampleUser);
        Item item = new Item(0, "Grey Wheels", 1223, 1, 18, ItemCategory.MATERIAL_ITEM, "");
        String invoiceNo = invoicesDatabase.insertPersonalInvoice(personalInvoice, FXCollections.observableArrayList(item));

        Assertions.assertNotNull(invoiceNo);

        PersonalInvoice inserted = invoicesDatabase.findPersonalInvoiceByInvoiceNo(invoiceNo);
        Assertions.assertNotNull(inserted);
        Assertions.assertEquals(personalInvoice.getCustomer().getEmail(), inserted.getCustomer().getEmail());
        Assertions.assertEquals(personalInvoice.getCustomer().getAddress().getStreet(), inserted.getCustomer().getAddress().getStreet());
        Assertions.assertEquals(personalInvoice.getAmount(), inserted.getAmount());
        Assertions.assertEquals(personalInvoice.getDiscount(), inserted.getDiscount());
        Assertions.assertEquals(personalInvoice.getDateOfIssue(), inserted.getDateOfIssue());
    }

    @Test
    void testGenerateInvoiceNo() {
        String inv = generateInvoiceNo();
        Assertions.assertNotNull(inv);
    }
}
