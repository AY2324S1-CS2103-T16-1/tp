package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEPARTMENT_LOGISTIC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalEmployees.BENSON;
import static seedu.address.testutil.TypicalEmployees.ELLE;
import static seedu.address.testutil.TypicalEmployees.getTypicalManageHr;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.department.Department;
import seedu.address.model.employee.Employee;
import seedu.address.model.employee.exceptions.DuplicateEmployeeException;
import seedu.address.model.employee.exceptions.SubordinatePresentException;
import seedu.address.testutil.EmployeeBuilder;

public class ManageHrTest {

    private final ManageHr manageHr = new ManageHr();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), manageHr.getEmployeeList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> manageHr.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyManageHr_replacesData() {
        ManageHr newData = getTypicalManageHr();
        manageHr.resetData(newData);
        assertEquals(newData, manageHr);
    }

    @Test
    public void resetData_withDuplicateEmployees_throwsDuplicateEmployeeException() {
        // Two people with the same identity fields
        Employee editedElle = new EmployeeBuilder(ELLE).withAddress(VALID_ADDRESS_BOB)
                .build();
        List<Employee> newPeople = Arrays.asList(ELLE, editedElle);
        ManageHrStub newData = new ManageHrStub(newPeople);

        assertThrows(DuplicateEmployeeException.class, () -> manageHr.resetData(newData));
    }

    @Test
    public void setEmployee_employeeWithSubordinatesChangesName_throwsSubordinatePresentException() {
        ManageHr typicalManageHr = getTypicalManageHr();
        Employee editedBenson = new EmployeeBuilder(BENSON).withName(VALID_NAME_BOB).build();
        assertThrows(SubordinatePresentException.class, () -> typicalManageHr.setEmployee(BENSON, editedBenson));
    }

    @Test
    public void setEmployee_employeeWithSubordinatesChangesRole_throwsCommandException() {
        ManageHr typicalManageHr = getTypicalManageHr();
        Employee editedBenson = new EmployeeBuilder(BENSON).withRole(VALID_ROLE_BOB).build();
        assertThrows(CommandException.class, () -> typicalManageHr.setEmployee(BENSON, editedBenson));
    }

    @Test
    public void hasEmployee_nullEmployee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> manageHr.hasEmployee(null));
    }

    @Test
    public void hasEmployee_employeeNotInManageHr_returnsFalse() {
        assertFalse(manageHr.hasEmployee(ELLE));
    }

    @Test
    public void hasEmployee_employeeInManageHr_returnsTrue() {
        manageHr.addEmployee(ELLE);
        assertTrue(manageHr.hasEmployee(ELLE));
    }

    @Test
    public void hasEmployee_employeeWithSameIdentityFieldsInManageHr_returnsTrue() {
        manageHr.addEmployee(ELLE);
        Employee editedElle = new EmployeeBuilder(ELLE).withAddress(VALID_ADDRESS_BOB)
                .withDepartments(VALID_DEPARTMENT_LOGISTIC)
                .build();
        assertTrue(manageHr.hasEmployee(editedElle));
    }

    @Test
    public void getEmployeeList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> manageHr.getEmployeeList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = ManageHr.class.getCanonicalName() + "{employees=" + manageHr.getEmployeeList() + "}";
        assertEquals(expected, manageHr.toString());
    }

    @Test
    public void equals() {
        // different type -> returns false
        assertFalse(manageHr.equals(new Object()));

        // same values -> returns true
        ManageHr other = new ManageHr();
        assertTrue(manageHr.equals(other));

        // different values -> returns false
        other.addEmployee(ELLE);
        assertFalse(manageHr.equals(other));

        // same object -> returns true
        assertTrue(manageHr.equals(manageHr));

        // null -> returns false
        assertFalse(manageHr.equals(null));
    }

    /**
     * A stub ReadOnlyManageHr whose people list can violate interface constraints.
     */
    private static class ManageHrStub implements ReadOnlyManageHr {
        private final ObservableList<Employee> people = FXCollections.observableArrayList();
        private final ObservableList<Department> departments = FXCollections.observableArrayList();

        ManageHrStub(Collection<Employee> people) {
            this.people.setAll(people);
        }

        @Override
        public ObservableList<Employee> getEmployeeList() {
            return people;
        }

        @Override
        public ObservableList<Department> getDepartmentList() {
            return departments;
        }
    }

}
