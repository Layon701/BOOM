package itf221.gvi.boom.io.writer;

import itf221.gvi.boom.data.OfferedPresentation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;
import itf221.gvi.boom.io.writer.FileWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.valueOf;

public class XlsxWriter implements FileWriter{

    public void writeStudentPlan(Path path, List<Student> students) throws IOException{

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // write title
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0, CellType.STRING);
        titleCell.setCellValue("Schüler");

        // empty row
        sheet.createRow(1);

        // write students
        for(Student student : students)
        {
            addStudent(student, sheet);
        }

        if (Files.isDirectory(path)) {
            path = path.resolve("Laufzettel.xlsx");
        }

        File file = path.toFile();
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile())) {
            // write this workbook to an output stream
            workbook.write(fileOut);
            fileOut.flush();
        }
    }

    public void writePresentationAttendance(Path path, List<PlannedPresentation> presentations) throws IOException{

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // write title

        for(PlannedPresentation presentation : presentations)
        {
            addPresentation(presentation, sheet);
        }
        if (Files.isDirectory(path)) {
            path = path.resolve("Anwesenheitsliste.xlsx");
        }

        File file = path.toFile();
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile())) {
            // write this workbook to an output stream
            workbook.write(fileOut);
            fileOut.flush();
        }
    }

    public void writeRoomTimetable(Path path, List<OfferedPresentation> presentations) throws IOException{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // write title
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0, CellType.STRING);
        titleCell.setCellValue("Raumplan");

        // empty row
        sheet.createRow(1);

        for(OfferedPresentation presentation : presentations)
        {
            addRooms(presentation, sheet);
        }

        if (Files.isDirectory(path)) {
            path = path.resolve("Raum-und-Zeitplan.xlsx");
        }

        File file = path.toFile();
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile())) {
            // write this workbook to an output stream
            workbook.write(fileOut);
            fileOut.flush();
        }
    }

    protected void addStudent(Student student, Sheet sheet) throws IOException
    {
        int studentRowNum = sheet.getLastRowNum() + 2;
        Row studentClassRow = sheet.createRow(studentRowNum);
        studentClassRow.createCell(0).setCellValue(student.getSchoolClass());

        studentRowNum ++;
        Row studentDataRow = sheet.createRow(studentRowNum);
        studentDataRow.createCell(0).setCellValue(student.getId());
        studentDataRow.createCell(1).setCellValue(student.getName());
        studentDataRow.createCell(2).setCellValue(student.getSurname());

        studentRowNum ++;
        Row timeslotRow = sheet.createRow(studentRowNum);
        studentRowNum ++;
        Row presentationRow = sheet.createRow(studentRowNum);
        for(int i = 0; i < student.getPlannedPresentations().size(); i++)
        {
            PlannedPresentation presentation = student.getPlannedPresentations().get(i);
            timeslotRow.createCell(i).setCellValue(valueOf(presentation.getTimeslot()));
            presentationRow.createCell(i).setCellValue(presentation.getOfferedPresentation().getTitle());
        }
    }

    protected void addPresentation(PlannedPresentation presentation, Sheet sheet) throws IOException
    {
        OfferedPresentation presentationOffer = presentation.getOfferedPresentation();

        int presentationRowNum = sheet.getLastRowNum() + 1;;
        Row presentationDataRow = sheet.createRow(presentationRowNum);
        presentationDataRow.createCell(0).setCellValue(presentationOffer.getCompanyName());
        presentationDataRow.createCell(1).setCellValue("ID: " + presentationOffer.getId());
        presentationDataRow.createCell(2).setCellValue(presentationOffer.getTitle());
        presentationDataRow.createCell(3).setCellValue(presentationOffer.getSpecialty());

        presentationRowNum++;
        for(Student attendee : presentation.getAttendees()) {
            presentationRowNum++;
            Row attendeeRow = sheet.createRow(presentationRowNum);
            attendeeRow.createCell(0).setCellValue(attendee.getId());
            attendeeRow.createCell(1).setCellValue(attendee.getName());
            attendeeRow.createCell(2).setCellValue(attendee.getSurname());
        }
    }

    protected void addRooms(OfferedPresentation presentation, Sheet sheet) throws IOException
    {

        int roomsRowNum = sheet.getLastRowNum() + 1;;
        Row roomDataRow = sheet.createRow(roomsRowNum);
        roomDataRow.createCell(0).setCellValue(presentation.getCompanyName());
        roomDataRow.createCell(1).setCellValue(presentation.getTitle());

        for(PlannedPresentation plannedPresentation : presentation.getPlannedPresentations())
        {
            // subtract char value of 'A' (first time slot) from timeslot to get index starting from 0
            // add 2 to skip to indexes after name/title info
            int index = plannedPresentation.getTimeslot() - 65 + 2;
            roomDataRow.createCell(index).setCellValue(plannedPresentation.getRoom().getRoomNumber());
        }
    }
}
