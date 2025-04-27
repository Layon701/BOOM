package itf221.gvi.boom.io.writer;

import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This class is intended for future extension to generate PDF documents.
 */
public class PdfWriter implements FileWriter{

    /**
     * Writes a student plan to the specified file path.
     * @param path the file path to write to.
     * @param students the list of students.
     * @throws IOException if an I/O error occurs.
     */
    public void writeStudentPlan(Path path, List<Student> students) throws IOException{
        // open file writer
        // write title
        for(Student student : students)
        {
            writeStudent(student);
        }
        // close file writer
    }

    /**
     * Writes a presentation attendance list to the specified file path.
     * @param path the file path to write to.
     * @param presentations the list of planned presentations.
     * @throws IOException if an I/O error occurs.
     */
    public void writePresentationAttendance(Path path, List<PlannedPresentation> presentations) throws IOException{
        // open file writer
        // write title
        for(PlannedPresentation presentation : presentations)
        {
            writePresentation(presentation);
        }
        // close file writer
    }

    /**
     * Writes a room timetable plan to the specified file path.
     * @param path the file path to write to.
     * @param presentations the list of offered presentations.
     * @throws IOException if an I/O error occurs.
     */
    public void writeRoomTimetable(Path path, List<OfferedPresentation> presentations) throws IOException{
        // open file writer

        // write title
        for(OfferedPresentation presentation : presentations) { }
        // close file writer
    }

    /**
     * Helper method to write a single student's information into the PDF.
     *
     * @param student the student to write.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeStudent(Student student) throws IOException { }

    /**
     * Helper method to write a single planned presentation's information into the PDF.
     *
     * @param presentation the planned presentation to write.
     * @throws IOException if an I/O error occurs.
     */
    protected void writePresentation(PlannedPresentation presentation) throws IOException { }
}
