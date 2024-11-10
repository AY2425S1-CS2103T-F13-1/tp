package seedu.address.ui;

import java.util.Comparator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.student.Student;

/**
 * A UI component that displays information of a {@code Student}.
 */
public class StudentCard extends UiPart<Region> {

    private static final String FXML = "StudentListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Student student;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane courses;

    /**
     * Creates a {@code StudentCode} with the given {@code Student} and index to display.
     */
    public StudentCard(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText(student.getPhone().value);
        email.setText(student.getEmail().value);
        student.getCourses().stream()
                .sorted(Comparator.comparing(course -> course.courseCode))
                .forEach(course -> courses.getChildren().add(new Label(course.courseCode)));

        // Delay UI by 0.1s
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Platform.runLater(() -> cardPane.getParent().requestLayout());
        }

        // Ui Doesn't Properly Update Unless this Line of Code is run
        Platform.runLater(() -> {
            if (cardPane.getParent() != null) {
                cardPane.getParent().requestLayout();
            }
        });
    }
}
