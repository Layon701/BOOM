package itf221.gvi.boom.io.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;
import itf221.gvi.boom.io.writer.FileWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class XlsxWriter implements FileWriter{

    public void writeStudentPlan(Path path, List<Student> students) throws IOException{

        // open file writer
        FileOutputStream file = new FileOutputStream(path.toFile());
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        // write title
        sheet.



        for(Student student : students)
        {
            file.
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
