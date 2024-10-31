package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.consultation.Consultation;
import seedu.address.model.consultation.exceptions.ConsultationNotFoundException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.exceptions.LessonNotFoundException;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;

/**
 * Wraps all data at the address-book level
 * Duplicate students are not allowed (by .isSameStudent comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueStudentList students;
    private final ObservableList<Consultation> consults;
    private final ObservableList<Lesson> lessons;

    /*
     * The 'unusual' code block below is a non-static initialization block,
     * sometimes used to avoid duplication
     * between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other
     * ways to avoid duplication
     * among constructors.
     */
    {
        students = new UniqueStudentList();
        consults = FXCollections.observableArrayList();
        lessons = FXCollections.observableArrayList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the data in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    /**
     * Replaces the contents of the consultation list with {@code consults}.
     */
    public void setConsults(List<Consultation> consults) {
        this.consults.setAll(consults);
    }

    /**
     * Replaces the contents of the lesson list with {@code lesson}.
     */
    public void setLessons(List<Lesson> lessons) {
        this.lessons.setAll(lessons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setStudents(newData.getStudentList());
        setConsults(newData.getConsultList());
        setLessons(newData.getLessonList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in
     * the address book.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Returns true if a consult with the same details as the given consult exists
     * in TAHub.
     *
     * @param consult The consultation to search for.
     * @return True if a consultation is found.
     */
    public boolean hasConsult(Consultation consult) {
        requireNonNull(consult);
        return consults.contains(consult);
    }

    /**
     * Adds a student to the address book.
     * The student must not already exist in the address book.
     */
    public void addStudent(Student p) {
        students.add(p);
    }

    /**
     * Adds a consultation to the address book and sorts the list by date.
     */
    public void addConsult(Consultation consult) {
        consults.add(consult);

        // Sort by date first, and by time if the dates are the same
        consults.sort((c1, c2) -> {
            int dateComparison = c1.getDate().compareTo(c2.getDate());
            if (dateComparison == 0) {
                return c1.getTime().compareTo(c2.getTime()); // Compare by time if dates are the same
            }
            return dateComparison; // Otherwise, compare by date
        });
    }

    /**
     * Replaces the given student {@code target} in the list with
     * {@code editedStudent}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same
     * as another existing student in the address book.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        students.setStudent(target, editedStudent);
    }

    /**
     * Replaces the given consultation {@code target} in the list with
     * {@code editedConsult}.
     * {@code target} must exist in TAHub.
     */
    public void setConsult(Consultation target, Consultation editedConsult) {
        requireAllNonNull(target, editedConsult);

        int index = consults.indexOf(target);
        if (index == -1) {
            throw new ConsultationNotFoundException();
        }

        consults.set(index, editedConsult);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * Also removes the student from all consultations and lessons.
     * {@code key} must exist in the address book.
     */
    public void removeStudent(Student key) {
        students.remove(key);
        // remove from consultations
        FilteredList<Consultation> consultsWithDeletedStudent = consults.filtered(c -> c.hasStudent(key));
        consultsWithDeletedStudent.forEach(c -> {
            Consultation newConsult = new Consultation(c);
            newConsult.removeStudent(key);
            setConsult(c, newConsult);
        });
        // remove from lessons
        FilteredList<Lesson> lessonsWithDeletedStudent = lessons.filtered(l -> l.hasStudent(key));
        lessonsWithDeletedStudent.forEach(l -> {
            Lesson newLesson = new Lesson(l);
            newLesson.removeStudent(key);
            setLesson(l, newLesson);
        });
    }

    /**
     * Removes {@code consult} from this {@code AddressBook}.
     * {@code consult} must exist in TAHub.
     *
     * @param consult The consult to be removed.
     */
    public void removeConsult(Consultation consult) {
        consults.remove(consult);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("students", students)
                .add("consults", consults)
                .toString();
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    public ObservableList<Consultation> getConsultList() {
        return FXCollections.unmodifiableObservableList(consults);
    }

    /**
     * Checks if this {@code AddressBook} is equal to another object.
     *
     * @param other The object to compare with.
     * @return true if both AddressBooks have the same students, consultations, and
     *         lessons, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return students.equals(otherAddressBook.students)
                && consults.equals(otherAddressBook.consults)
                && lessons.equals(otherAddressBook.lessons);
    }

    /**
     * Returns the hash code for this {@code AddressBook}.
     *
     * @return The hash code based on students, consultations, and lessons.
     */
    @Override
    public int hashCode() {
        return Objects.hash(students, consults, lessons);
    }

    /**
     * Checks if the address book contains the specified {@code Lesson}.
     *
     * @param lesson The lesson to check.
     * @return true if the lesson exists in the address book, false otherwise.
     * @throws NullPointerException if {@code lesson} is null.
     */
    public boolean hasLesson(Lesson lesson) {
        requireNonNull(lesson);
        return lessons.contains(lesson);
    }

    /**
     * Adds a {@code Lesson} to the address book and sorts the lesson list by date.
     * If two lessons have the same date, they are further sorted by time.
     *
     * @param lesson The lesson to add.
     * @throws NullPointerException if {@code lesson} is null.
     */
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);

        // Sort by date first, and then by time if the dates are the same
        lessons.sort((l1, l2) -> {
            int dateComparison = l1.getDate().compareTo(l2.getDate());
            if (dateComparison == 0) {
                return l1.getTime().compareTo(l2.getTime()); // Compare by time if dates are the same
            }
            return dateComparison; // Otherwise, compare by date
        });
    }

    /**
     * Removes a {@code Lesson} from the address book.
     *
     * @param lesson The lesson to remove.
     * @throws NullPointerException if {@code lesson} is null.
     */
    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    /**
     * Returns an unmodifiable view of the lesson list.
     *
     * @return An unmodifiable {@code ObservableList} containing all lessons in the
     *         address book.
     */
    public ObservableList<Lesson> getLessonList() {
        return FXCollections.unmodifiableObservableList(lessons);
    }

    /**
     * Replaces the given lesson {@code target} in the list with
     * {@code editedLesson}.
     * {@code target} must exist in the address book.
     *
     * @param target       The lesson to be replaced.
     * @param editedLesson The new lesson to replace the target.
     * @throws NullPointerException    if {@code target} or {@code editedLesson} is
     *                                 null.
     * @throws LessonNotFoundException if {@code target} could not be found in the
     *                                 list.
     */
    public void setLesson(Lesson target, Lesson editedLesson) {
        requireAllNonNull(target, editedLesson);

        int index = lessons.indexOf(target);
        if (index == -1) {
            throw new LessonNotFoundException(); // Custom exception to be created if it doesn't exist
        }

        lessons.set(index, editedLesson);
    }
}
