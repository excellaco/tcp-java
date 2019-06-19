package com.excella.reactor.validation;

import com.excella.reactor.domain.Bio;
import com.excella.reactor.domain.Contact;
import com.excella.reactor.domain.Employee;
import org.testng.annotations.*;

import javax.validation.Validation;
import javax.validation.Validator;

@Test
public class EmployeeValidationTests {

    private Employee employee;
    private Validator validator;

    @BeforeTest
    public void runBeforeTest() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @BeforeMethod
    public void runBeforeEachMethod() {
        employee = new Employee();
        var bio = new Bio();
        var contact = new Contact();
        employee.setBio(bio);
        employee.setContact(contact);
    }

    @AfterMethod
    public void runAfterEachMethod() {

    }

    @Test
    public void initialTest() {
        assert validator.validate(employee).isEmpty();
    }

}
