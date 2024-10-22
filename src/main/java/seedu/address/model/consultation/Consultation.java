package seedu.address.model.consultation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.model.course.Course;
import seedu.address.model.student.Student;

/**
 * Represents a Consultation in the system.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Consultation {

    private final Date date;
    private final Time time;
    private final Course course;
    private final List<Student> students;

    /**
     * Constructs a {@code Consultation}.
     *
     * @param date The date of the consultation.
     * @param time The time of the consultation.
     * @param students A list of students attending the consultation.
     *                 This list can be empty but must not be null.
     * @throws NullPointerException if {@code date} or {@code time} is null.
     */
    public Consultation(Date date, Time time, Course course, List<Student> students) {
        requireAllNonNull(date, time, course);
        this.date = date;
        this.time = time;
        this.course = course;

        this.students = students != null ? new ArrayList<>(students) : new ArrayList<>();
    }

    /**
     * Returns the date of the consultation.
     *
     * @return The date of the consultation.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the time of the consultation.
     *
     * @return The time of the consultation.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Returns the course the consultation is for.
     *
     * @return The course the consultation is for.
     */
    public Course getCourse() {
        return course;
    }


    /**
     * Returns an immutable list of students attending the consultation.
     *
     * @return A list of students attending the consultation.
     * @throws UnsupportedOperationException if an attempt is made to modify the returned list.
     */
    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Adds a student to the consultation.
     *
     * @param student The student to add.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Removes a student from the consultation.
     *
     * @param student The student to remove.
     */
    public void removeStudent(Student student) {
        students.remove(student);
    }

    /**
     * Returns true if the consultation contains the specified student.
     *
     * @param student The student to check for.
     * @return True if the student is attending the consultation, false otherwise.
     */
    public boolean hasStudent(Student student) {
        return students.contains(student);
    }

    /**
     * Returns true if both consultations have the same date, time, and students.
     *
     * @param other The other consultation to compare.
     * @return True if both consultations are the same, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Consultation)) {
            return false;
        }

        Consultation otherConsultation = (Consultation) other;
        return date.equals(otherConsultation.date)
                && time.equals(otherConsultation.time)
                && course.equals(otherConsultation.course)
                && students.equals(otherConsultation.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, course, students);
    }

    @Override
    public String toString() {
        return String.format("Consultation[date=%s, time=%s, course=%s, students=%s]", date, time, course, students);
    }

    /**
     * Ensures that none of the arguments passed to the constructor are null, except the student list can be empty.
     *
     * @param objects The objects to check for null values.
     * @throws NullPointerException if any object is null.
     */
    private void requireAllNonNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new NullPointerException("Fields date, time and course must be non-null");
            }
        }
    }
}
