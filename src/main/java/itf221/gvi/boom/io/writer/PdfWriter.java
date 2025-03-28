package itf221.gvi.boom.io.writer;

import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PdfWriter implements FileWriter{

    public void writeStudentPlan(Path path, List<Student> students) throws IOException{
        // open file writer

        // write title

        for(Student student : students)
        {
            writeStudent(student);
        }

        // close file writer
    }

    public void writePresentationAttendance(Path path, List<PlannedPresentation> presentations) throws IOException{
        // open file writer

        // write title

        for(PlannedPresentation presentation : presentations)
        {
            writePresentation(presentation);
        }

        // close file writer
    }

    public void writeRoomTimetable(Path path, List<PlannedPresentation> presentations) throws IOException{
        // open file writer

        // write title

        for(PlannedPresentation presentation : presentations)
        {
            presentation.getRoom();
        }

        // close file writer
    }

    protected void writeStudent(Student student) throws IOException
    {

    }

    protected void writePresentation(PlannedPresentation presentation) throws IOException
    {

    }

}
