package seedu.address.model.student;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Course}'s {@code courseCode} matches any of the keywords given.
 */
public class CourseContainsKeywordsPredicate implements Predicate<Student> {
    private final List<String> keywords;

    public CourseContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Student student) {
        return keywords.stream()
                .anyMatch(keyword ->
                        student.getCourses().stream()
                                .anyMatch(course ->
                                        StringUtil.containsWordIgnoreCase(course.courseCode, keyword)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CourseContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((CourseContainsKeywordsPredicate) other).keywords)); // state check
    }
}
