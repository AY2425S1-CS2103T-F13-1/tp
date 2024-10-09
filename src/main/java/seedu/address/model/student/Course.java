package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's course in the system.
 * Guarantees: immutable; is valid as declared in {@link #isValidCourse(String)}
 */
public class Course {
<<<<<<< HEAD
    public static final String MESSAGE_CONSTRAINTS = "Courses should be in the format of two letters "
        + "followed by four digits, e.g., CS2103T.";
=======

    public static final String MESSAGE_CONSTRAINTS = "Courses should be in the format of two letters followed by four digits, e.g., CS2103T.";
    
>>>>>>> parent of ec0de729 (edit tests)
    /*
     * The course code must follow the format of two letters followed by four digits.
     * It can optionally have a trailing letter. E.g., "CS2103T", "cs2100".
     */
    public static final String VALIDATION_REGEX = "[a-zA-Z]{2}\\d{4}[a-zA-Z]?";

    public final String courseCode;

    /**
     * Constructs a {@code Course}.
     *
     * @param courseCode A valid course code.
     */
    public Course(String courseCode) {
        requireNonNull(courseCode);
        checkArgument(isValidCourse(courseCode), MESSAGE_CONSTRAINTS);
        this.courseCode = courseCode;
    }

    /**
     * Returns true if a given string is a valid course code.
     */
    public static boolean isValidCourse(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return courseCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Course)) {
            return false;
        }

        Course otherCourse = (Course) other;
        return courseCode.equals(otherCourse.courseCode);
    }

    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }
}