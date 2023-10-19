package seedu.address.testutil;

import seedu.address.model.ManageHr;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private ManageHr manageHr;

    public AddressBookBuilder() {
        manageHr = new ManageHr();
    }

    public AddressBookBuilder(ManageHr manageHr) {
        this.manageHr = manageHr;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withPerson(Person person) {
        manageHr.addPerson(person);
        return this;
    }

    public ManageHr build() {
        return manageHr;
    }
}
