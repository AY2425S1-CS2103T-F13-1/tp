package seedu.address.logic.parser.consultation;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.consultation.AddConsultCommand;
import seedu.address.model.consultation.Consultation;
import seedu.address.model.consultation.Date;
import seedu.address.model.consultation.Time;
import seedu.address.model.course.Course;

public class AddConsultCommandParserTest {
    private AddConsultCommandParser parser = new AddConsultCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AddConsultCommand expectedCommand = new AddConsultCommand(
                new Consultation(new Date("2024-10-20"), new Time("14:00"),
                        new Course("CS2103T"), List.of()));

        // no whitespace (besides the first initial blank)
        assertParseSuccess(parser, " " + PREFIX_DATE + "2024-10-20 "
                + PREFIX_TIME + "14:00 "
                + PREFIX_COURSE + "CS2103T", expectedCommand);

        // random whitespace
        assertParseSuccess(parser, "  \n \t " + PREFIX_DATE + "2024-10-20 \t\n "
                + PREFIX_TIME + "14:00  \n "
                + PREFIX_COURSE + "CS2103T", expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddConsultCommand.MESSAGE_USAGE);

        // missing date prefix
        assertParseFailure(parser, " " + PREFIX_TIME + "14:00 "
                + PREFIX_COURSE + "CS2103T", expectedMessage);

        // missing time prefix
        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                + PREFIX_COURSE + "CS2103T", expectedMessage);

        // missing course prefix
        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                + PREFIX_TIME + "14:00", expectedMessage);


        // all prefixes missing
        assertParseFailure(parser, " ", expectedMessage);
    }

    @Test
    public void parse_duplicateFields_failure() {

        // duplicate date prefix
        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                + PREFIX_DATE + "2024-10-21 "
                + PREFIX_TIME + "14:00 "
                + PREFIX_COURSE + "CS2103T",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));


        // duplicate time prefix
        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                        + PREFIX_TIME + "13:00 "
                        + PREFIX_TIME + "14:00 "
                        + PREFIX_COURSE + "CS2103T",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));


        // duplicate course prefix
        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                        + PREFIX_TIME + "14:00 "
                        + PREFIX_COURSE + "CS2040S "
                        + PREFIX_COURSE + "CS2103T",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COURSE));
    }

    @Test
    public void parse_invalidValue_failure() {

        assertParseFailure(parser, " " + PREFIX_DATE + "20-10-2024 "
                        + PREFIX_TIME + "14:00 "
                        + PREFIX_COURSE + "CS2103T",
                Date.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                        + PREFIX_TIME + "25:00 "
                        + PREFIX_COURSE + "CS2103T",
                Time.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, " " + PREFIX_DATE + "2024-10-20 "
                        + PREFIX_TIME + "14:00 "
                        + PREFIX_COURSE + "CS2103TTTTTTTT",
                Course.MESSAGE_CONSTRAINTS);
    }
}
