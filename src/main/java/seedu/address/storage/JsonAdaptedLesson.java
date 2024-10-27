package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.util.Pair;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.consultation.Date;
import seedu.address.model.consultation.Time;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.student.Student;

/**
 * Jackson-friendly version of {@link Lesson}.
 */
class JsonAdaptedLesson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Lesson's %s field is missing!";
    public static final String STUDENT_NOT_FOUND_MESSAGE = "Student %s"
            + " does not exist in the address book, or details do not match!";

    private final String date;
    private final String time;
    private final List<JsonAdaptedStudent> students;
    private final Map<JsonAdaptedStudent, Boolean> attendanceMap;

    /**
     * Constructs a {@code JsonAdaptedLesson} with the given lesson details.
     */
    @JsonCreator
    public JsonAdaptedLesson(@JsonProperty("date") String date,
            @JsonProperty("time") String time,
            @JsonProperty("students") List<JsonAdaptedStudent> students,
            @JsonProperty("attendanceMap") Set<Pair<JsonAdaptedStudent, Boolean>> attendanceMap) {
        this.date = date;
        this.time = time;
        this.students = new ArrayList<>();
        this.attendanceMap = new HashMap<>();
        // Null check required because the JsonCreator constructor may be called with null if the fields do not exist
        if (students != null) {
            this.students.addAll(students);
        }
        if (attendanceMap != null) {
            attendanceMap.forEach(pair -> this.attendanceMap.put(pair.getKey(), pair.getValue()));
        }

    }

    /**
     * Converts a given {@code Lesson} into this class for Jackson use.
     */
    public JsonAdaptedLesson(Lesson source) {
        date = source.getDate().toString();
        time = source.getTime().toString();
        students = new ArrayList<>();
        attendanceMap = new HashMap<>();

        for (Student student : source.getStudents()) {
            JsonAdaptedStudent jsonAdaptedStudent = new JsonAdaptedStudent(student);
            boolean attendance = source.getAttendance(student);
            students.add(jsonAdaptedStudent);
            attendanceMap.put(jsonAdaptedStudent, attendance);
            // attendanceMap.add(new Pair<>(jsonAdaptedStudent, attendance));
        }
    }

    /**
     * Returns the date of the lesson.
     *
     * @return A string representing the date of the lesson.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the time of the lesson.
     *
     * @return A string representing the time of the lesson.
     */
    public String getTime() {
        return time;
    }

    /**
     * Returns a list of students attending the lesson.
     *
     * @return A new list containing the {@code JsonAdaptedStudent} objects
     *         representing the students.
     */
    public List<JsonAdaptedStudent> getStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Converts this Jackson-friendly adapted lesson object into the model's
     * {@code Lesson} object.
     *
     * @param addressBook AddressBook instance to verify that the student(s) exist
     *                    in.
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted lesson.
     */
    public Lesson toModelType(AddressBook addressBook) throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Date"));
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }
        final Date modelDate = new Date(date);

        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Time"));
        }
        if (!Time.isValidTime(time)) {
            throw new IllegalValueException(Time.MESSAGE_CONSTRAINTS);
        }
        final Time modelTime = new Time(time);

        final List<Student> modelStudents = new ArrayList<>();
        final HashMap<Student, Boolean> modelAttendanceMap = new HashMap<>();

        for (JsonAdaptedStudent student : students) {
            Student modelStudent = student.toModelType();

            // Check to ensure student data matches an existing student
            if (!addressBook.hasStudent(modelStudent)) {
                throw new IllegalValueException(
                        String.format(STUDENT_NOT_FOUND_MESSAGE, modelStudent.getName().fullName));
            }

            modelStudents.add(modelStudent);
            // if the student is not found in attendanceMap, defaults to no attendance
            modelAttendanceMap.put(modelStudent, attendanceMap.getOrDefault(student, false));
        }

        return new Lesson(modelDate, modelTime, modelStudents, modelAttendanceMap);
    }
}
