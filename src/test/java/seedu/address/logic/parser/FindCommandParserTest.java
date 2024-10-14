package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.student.CourseContainsKeywordsPredicate;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    // TODO: Fix test
    @Test
    public void parse_validArgs_returnsFindCommand() {
        try {
            // no leading and trailing whitespaces
            NameContainsKeywordsPredicate expectedPredicate =
                    new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
            FindCommand expectedFindCommand = new FindCommand(expectedPredicate);
            FindCommand actualCommand = parser.parse("n/Alice n/Bob");
            System.out.println("Expected: " + expectedFindCommand);
            System.out.println("Actual: " + actualCommand);
            assertParseSuccess(parser, "n/Alice n/Bob", expectedFindCommand);

            // multiple whitespaces between keywords
            assertParseSuccess(parser, " \n n/Alice \n \t n/Bob  \t", expectedFindCommand);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // TODO: Fix test
    @Test
    public void parse_multipleArgs_returnsFindCommand() {
        try {
            NameContainsKeywordsPredicate namePredicate =
                    new NameContainsKeywordsPredicate(Arrays.asList("Alice"));
            CourseContainsKeywordsPredicate coursePredicate =
                    new CourseContainsKeywordsPredicate(Arrays.asList("CS2103T"));
            Predicate<Student> expectedPredicate = namePredicate.and(coursePredicate);
            FindCommand expectedFindCommand = new FindCommand(expectedPredicate);
            FindCommand actualCommand = parser.parse("n/Alice c/CS2103T");
            System.out.println("Expected: " + expectedFindCommand);
            System.out.println("Actual: " + actualCommand);
            assertParseSuccess(parser, "n/Alice c/CS2103T", expectedFindCommand);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private Predicate<Student> preparePredicate(String userInput) {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_NAME, PREFIX_COURSE);
        Predicate<Student> predicate = student -> true;

        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            predicate = predicate.and(new NameContainsKeywordsPredicate(argMultimap.getAllValues(PREFIX_NAME)));
        }

        if (arePrefixesPresent(argMultimap, PREFIX_COURSE)) {
            predicate = predicate.and(new CourseContainsKeywordsPredicate(argMultimap.getAllValues(PREFIX_COURSE)));
        }

        return predicate;
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
