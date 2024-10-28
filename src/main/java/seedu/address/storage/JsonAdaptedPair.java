package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson-friendly version of a student-attendance pair.
 */
class JsonAdaptedAttendance {

    private final JsonAdaptedStudent student;
    private final boolean attendance;

    /**
     * Constructs a {@code JsonAdaptedAttendance} with the given student and attendance details.
     */
    @JsonCreator
    public JsonAdaptedAttendance(@JsonProperty("student") JsonAdaptedStudent student,
                                 @JsonProperty("attendance") boolean attendance) {
        this.student = student;
        this.attendance = attendance;
    }

    /**
     * Converts this Jackson-friendly adapted attendance object into the model's {@code Student} and attendance.
     */
    public JsonAdaptedStudent getStudent() {
        return student;
    }

    public boolean getAttendance() {
        return attendance;
    }
}