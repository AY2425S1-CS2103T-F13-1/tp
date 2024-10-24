package seedu.address.logic.commands.consultation;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CommandType;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.consultation.Consultation;
import seedu.address.testutil.ConsultationBuilder;
import seedu.address.testutil.ModelStub;

public class AddConsultCommandTest {

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddConsultCommand(null));
    }

    @Test
    public void execute_consultAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingConsultAdded modelStub = new ModelStubAcceptingConsultAdded();
        Consultation consult = new ConsultationBuilder().build();
        CommandResult commandResult = new AddConsultCommand(consult).execute(modelStub);

        assertEquals(String.format(AddConsultCommand.MESSAGE_SUCCESS, Messages.format(consult)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(consult), modelStub.consultsAdded);
    }

    @Test
    public void equals() {
        Consultation jan1stConsult = new ConsultationBuilder().withDate("2024-01-01").build();
        Consultation feb2ndConsult = new ConsultationBuilder().withDate("2024-02-02").build();

        AddConsultCommand addJan1stConsult = new AddConsultCommand(jan1stConsult);
        AddConsultCommand addFeb2ndConsult = new AddConsultCommand(feb2ndConsult);

        // same object -> returns true
        assertTrue(addJan1stConsult.equals(addJan1stConsult));

        // same values -> returns true
        AddConsultCommand addJan1stConsultCopy = new AddConsultCommand(jan1stConsult);
        assertTrue(addJan1stConsult.equals(addJan1stConsultCopy));

        // different types -> returns false
        assertFalse(addJan1stConsult.equals(1));

        // null -> returns false
        assertFalse(addJan1stConsult.equals(null));

        // different date -> returns false
        assertFalse(addJan1stConsult.equals(addFeb2ndConsult));

        Consultation consultCS2103T = new ConsultationBuilder().withCourse("CS2103T").build();
        Consultation consultCS2040S = new ConsultationBuilder().withCourse("CS2040S").build();

        AddConsultCommand addCS2103TConsult = new AddConsultCommand(consultCS2103T);
        AddConsultCommand addCS2040SConsult = new AddConsultCommand(consultCS2040S);

        // same object -> returns true
        assertTrue(addCS2103TConsult.equals(addCS2103TConsult));

        // same values -> returns true
        AddConsultCommand addCS2040SConsultCopy = new AddConsultCommand(consultCS2040S);
        assertTrue(addCS2040SConsult.equals(addCS2040SConsultCopy));

        // different types -> returns false
        assertFalse(addCS2040SConsult.equals(1));

        // null -> returns false
        assertFalse(addCS2040SConsult.equals(null));

        // different course -> returns false
        assertFalse(addCS2040SConsult.equals(addCS2103TConsult));
    }

    @Test
    public void toStringMethod() {
        Consultation consult = new ConsultationBuilder().build();
        AddConsultCommand addConsultCommand = new AddConsultCommand(consult);
        String expected = AddConsultCommand.class.getCanonicalName() + "{newConsult=" + consult + "}";
        assertEquals(expected, addConsultCommand.toString());
    }

    @Test
    public void getCommandTypeMethod() {
        Consultation consult = new ConsultationBuilder().build();
        AddConsultCommand addConsultCommand = new AddConsultCommand(consult);
        assertEquals(addConsultCommand.getCommandType(), CommandType.ADDCONSULT);
    }

    /**
     * A Model stub that always accept the consult being added.
     */
    private class ModelStubAcceptingConsultAdded extends ModelStub {
        final ArrayList<Consultation> consultsAdded = new ArrayList<>();

        @Override
        public boolean hasConsult(Consultation consult) {
            requireNonNull(consult);
            return consultsAdded.stream().anyMatch(consult::equals);
        }

        @Override
        public void addConsult(Consultation consult) {
            requireNonNull(consult);
            consultsAdded.add(consult);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
