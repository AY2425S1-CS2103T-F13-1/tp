package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEFAULT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_singleDeleteValidArgs_returnsDeleteCommand() {
        Set<Index> firstIndexSet = new HashSet<>();
        firstIndexSet.add(INDEX_FIRST_STUDENT);
        assertParseSuccess(parser, "1", new DeleteCommand(firstIndexSet));
    }

    @Test
    public void parse_multiDeleteValidArgs_returnsDeleteCommand() {
        Set<Index> indexSet = new HashSet<>();
        indexSet.add(INDEX_FIRST_STUDENT);
        indexSet.add(INDEX_SECOND_STUDENT);
        assertParseSuccess(parser, "1" + PREFIX_DEFAULT + " 2",
                new DeleteCommand(indexSet));
    }

    @Test
    public void parse_noArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }



    @Test
    public void parse_invalidArgsNoTag_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_someValidSomeInvalidArgsNoTag_throwsParseException() {
        assertParseFailure(parser, "1" + PREFIX_DEFAULT + " a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsNoNum_throwsParseException() {
        assertParseFailure(parser, PREFIX_DEFAULT.toString(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsSomeNoNum_throwsParseException() {
        assertParseFailure(parser, PREFIX_DEFAULT + " 1" + PREFIX_DEFAULT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
