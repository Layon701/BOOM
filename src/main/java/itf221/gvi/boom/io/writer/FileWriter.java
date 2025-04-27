package itf221.gvi.boom.io.writer;

import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for writing data into files.
 */
public interface FileWriter {

    /**
     * Writes a student plan to the specified file path.
     * @param path the file path to write to.
     * @param students the list of students.
     * @throws IOException if an I/O error occurs.
     */
    void writeStudentPlan(Path path, List<Student> students) throws IOException;

    /**
     * Writes a presentation attendance list to the specified file path.
     * @param path the file path to write to.
     * @param presentations the list of planned presentations.
     * @throws IOException if an I/O error occurs.
     */
    void writePresentationAttendance(Path path, List<PlannedPresentation> presentations) throws IOException;

    /**
     * Writes a room timetable plan to the specified file path.
     * @param path the file path to write to.
     * @param presentations the list of offered presentations.
     * @throws IOException if an I/O error occurs.
     */
    void writeRoomTimetable(Path path, List<OfferedPresentation> presentations) throws IOException;
}
