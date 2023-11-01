package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.department.Department;
import seedu.address.model.department.UniqueDepartmentList;
import seedu.address.model.employee.Employee;
import seedu.address.model.employee.UniqueEmployeeList;
import seedu.address.model.employee.exceptions.SubordinatePresentException;
import seedu.address.model.employee.exceptions.SupervisorNotFoundException;

/**
 * Wraps all data at the ManageHR level
 * Duplicates are not allowed (by .isSameEmployee comparison)
 */
public class ManageHr implements ReadOnlyManageHr {

    private final UniqueEmployeeList employees;

    private final UniqueDepartmentList departments;
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        departments = new UniqueDepartmentList();
        employees = new UniqueEmployeeList();
    }

    public ManageHr() {}

    /**
     * Creates a ManageHR instance using the Employees in the {@code toBeCopied}
     */
    public ManageHr(ReadOnlyManageHr toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the employee list with {@code people}.
     * {@code people} must not contain duplicate people.
     */
    public void setEmployees(List<Employee> people) {
        for (Employee employee : people) {
            employee.checkValidDepartments(departments);
        }
        this.employees.setEmployees(people);
    }

    /**
     * Resets the existing data of this {@code ManageHr} with {@code newData}.
     */
    public void resetData(ReadOnlyManageHr newData) {
        requireNonNull(newData);
        setEmployees(newData.getEmployeeList());
    }

    //// employee-level operations

    /**
     * Returns true if an employee with the same identity as {@code employee} exists in the storage.
     */
    public boolean hasEmployee(Employee employee) {
        requireNonNull(employee);
        return employees.contains(employee);
    }

    /**
     * Adds an employee to ManageHR.
     * The employee must not already exist in ManageHR.
     *
     * @param p The employee to be added to the list.
     * @throws SupervisorNotFoundException If the supervisor of the employee is not found in the list.
     */

    public void addEmployee(Employee p) {
        requireNonNull(p);
        p.checkValidDepartments(departments);
        if (!employees.containsManager(p)) {
            throw new SupervisorNotFoundException();
        }
        employees.add(p);
    }

    /**
     * Replaces the given employee {@code target} in the list with {@code editedEmployee}.
     * {@code target} must exist in the ManageHR.
     * The employee identity of {@code editedEmployee} must not be the same as another existing employee in ManageHR.
     *
     * @param target The original employee to be updated.
     * @param editedEmployee The updated employee.
     * @throws SubordinatePresentException If the original employee manages subordinates, preventing the update.
     * @throws SupervisorNotFoundException If the target employee is the supervisor of the editedEmployee.
     */
    public void setEmployee(Employee target, Employee editedEmployee) {
        requireNonNull(editedEmployee);
        editedEmployee.checkValidDepartments(departments);
        if (employees.hasSubordinates(target)) {
            throw new SubordinatePresentException();
        }
        if (!employees.containsManager(editedEmployee)) {
            throw new SupervisorNotFoundException();
        }
        if (target.isSupervisorOf(editedEmployee)) {
            throw new SupervisorNotFoundException();
        }

        employees.setEmployee(target, editedEmployee);
    }

    /**
     * Removes {@code key} from this {@code ManageHr}.
     * {@code key} must exist in the ManageHr.
     *
     * @param key The employee to be removed.
     * @throws SubordinatePresentException If the employee manages subordinates, preventing removal.
     */
    public void removeEmployee(Employee key) {
        if (employees.hasSubordinates(key)) {
            throw new SubordinatePresentException();
        }
        employees.remove(key);
    }

    /**
     * Adds an department to ManageHR.
     * The employee must not already exist in ManageHR.
     */
    public boolean hasDepartment(Department department) {
        requireNonNull(department);
        return departments.contains(department);
    }

    /**
     * Adds an department to ManageHR.
     * The employee must not already exist in ManageHR.
     */
    public void addDepartment(Department d) {
        departments.add(d);
    }

    /**
     * Replaces the given employee {@code target} in the list with {@code editedEmployee}.
     * {@code target} must exist in the ManageHR.
     * The employee identity of {@code editedEmployee} must not be the same as another existing employee in ManageHR.
     */
    public void setDepartment(Department target, Department editedDepartment) {
        requireNonNull(editedDepartment);
        departments.setDepartment(target, editedDepartment);
    }

    /**
     * Removes {@code key} from this {@code ManageHr}.
     * {@code key} must exist in the ManageHr.
     */
    public void removeDepartment(Department key) {
        departments.remove(key);
    }
    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employees", employees)
                .toString();
    }

    @Override
    public ObservableList<Employee> getEmployeeList() {
        return employees.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Department> getDepartmentList() {
        return departments.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ManageHr)) {
            return false;
        }

        ManageHr otherManageHr = (ManageHr) other;
        return employees.equals(otherManageHr.employees);
    }

    @Override
    public int hashCode() {
        return employees.hashCode();
    }
}
