package itf221.gvi.boom.io.writer;

import itf221.gvi.boom.data.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XlsxWriterTest {

    /**
     * Creates dummy data for testing purposes, including rooms, companies, students, and presentations.
     *
     * @return BoomData object containing the dummy data.
     */
    protected BoomData createDummyData(){
        Room tmpRoom1 = new Room("1", 20);
        Room tmpRoom2 = new Room("20", 20);
        Room tmpRoom3 = new Room("Aula", 40);

        List<Room> roomData = new ArrayList<>();
        roomData.add(tmpRoom1);
        roomData.add(tmpRoom2);
        roomData.add(tmpRoom3);

        // initializer list by scheme: id, minCapacity, maxCapacity, specialty, maxPresentations, earliestTimeslot, name
        Company tmpCompany1 = new Company("Company1", Arrays.asList(new OfferedPresentation(1, 0, 20, "specialty1", 'A', "Company1")));
        Company tmpCompany2 = new Company("Company2", Arrays.asList(new OfferedPresentation(2, 0, 20, "specialty2", 'A', "Company2")));
        Company tmpCompany3 = new Company("Company3", Arrays.asList(new OfferedPresentation(3, 0, 30, "specialty3", 'A', "Company3")));
        Company tmpCompany4 = new Company("Company4", Arrays.asList(new OfferedPresentation(4, 0, 5, "specialty4", 'C', "Company4")));
        Company tmpCompany5 = new Company("Company5", Arrays.asList(new OfferedPresentation(5, 0, 5, "specialty5", 'C', "Company5")));

        Map<String, Company> tmpCommap = Map.of("Company1", tmpCompany1, "Company2", tmpCompany2, "Company3", tmpCompany3, "Company4", tmpCompany4, "Company5", tmpCompany5);

        List<Company> companyData = new ArrayList<Company>(tmpCommap.values());

        List<OfferedPresentation> allOffered = new ArrayList<OfferedPresentation>();
        allOffered.addAll(tmpCompany1.getOfferedPresentations());
        allOffered.addAll(tmpCompany2.getOfferedPresentations());
        allOffered.addAll(tmpCompany3.getOfferedPresentations());
        allOffered.addAll(tmpCompany4.getOfferedPresentations());
        allOffered.addAll(tmpCompany5.getOfferedPresentations());

        PlannedPresentation tmpPlannedPresentation1 = new PlannedPresentation('A', tmpRoom1, tmpCompany1.getOfferedPresentations().getFirst(), new ArrayList<>());
        tmpCompany1.getOfferedPresentations().getFirst().getPlannedPresentations().add(tmpPlannedPresentation1);
        PlannedPresentation tmpPlannedPresentation2 = new PlannedPresentation('B', tmpRoom2, tmpCompany2.getOfferedPresentations().getFirst(), new ArrayList<>());
        tmpCompany2.getOfferedPresentations().getFirst().getPlannedPresentations().add(tmpPlannedPresentation2);

        Student tmpStudent1 = new Student(allOffered, "surname1", "name1", "Class1", 1);
        tmpStudent1.getPlannedPresentations().add(tmpPlannedPresentation1);
        Student tmpStudent2 = new Student(allOffered, "surname2", "name2", "Class2", 2);
        tmpStudent2.getPlannedPresentations().add(tmpPlannedPresentation1);
        tmpStudent2.getPlannedPresentations().add(tmpPlannedPresentation2);
        Student tmpStudent3 = new Student(allOffered, "surname3", "name3", "Class3", 3);
        tmpStudent3.getPlannedPresentations().add(tmpPlannedPresentation1);
        tmpStudent3.getPlannedPresentations().add(tmpPlannedPresentation2);
        Student tmpStudent4 = new Student(allOffered, "surname4", "name4", "Class4", 4);
        tmpStudent4.getPlannedPresentations().add(tmpPlannedPresentation2);

        List<Student> studentData = Arrays.asList(tmpStudent1, tmpStudent2, tmpStudent3, tmpStudent4);

        for(Student s:studentData)
        {
            for(PlannedPresentation p : s.getPlannedPresentations())
            {
                p.getAttendees().add(s);
            }
        }
        return new BoomData(roomData, companyData, studentData);
    }

    /**
     * Tests the interpret() method from the CompanyInterpreter class.
     */
    @Test
    void writeStudentPlanTest() {
        BoomData data = createDummyData();
        XlsxWriter testWriter = new XlsxWriter();
        Path outputFile = Path.of("src/test/results/Student_out.xlsx");
        try {
            testWriter.writeStudentPlan(outputFile, data.getStudents());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            XSSFWorkbook expectedWorkbook = new XSSFWorkbook();
            Sheet expectedSheet = expectedWorkbook.createSheet();

            // write title
            Row titleRow = expectedSheet.createRow(0);
            Cell titleCell = titleRow.createCell(0, CellType.STRING);
            titleCell.setCellValue("Schüler");

            // empty row
            expectedSheet.createRow(1);

            Row row = expectedSheet.createRow(3);
            row.createCell(1).setCellValue("Class1");
            row = expectedSheet.createRow(4);
            row.createCell(1).setCellValue("1");
            row.createCell(2).setCellValue("name1");
            row.createCell(3).setCellValue("surname1");
            row = expectedSheet.createRow(5);
            row.createCell(1).setCellValue("A");
            row = expectedSheet.createRow(6);
            row.createCell(1).setCellValue("Company1: specialty1");

            row = expectedSheet.createRow(8);
            row.createCell(1).setCellValue("Class2");
            row = expectedSheet.createRow(9);
            row.createCell(1).setCellValue("2");
            row.createCell(2).setCellValue("name2");
            row.createCell(3).setCellValue("surname2");
            row = expectedSheet.createRow(10);
            row.createCell(1).setCellValue("A");
            row.createCell(2).setCellValue("B");
            row = expectedSheet.createRow(11);
            row.createCell(1).setCellValue("Company1: specialty1");
            row.createCell(2).setCellValue("Company2: specialty2");

            row = expectedSheet.createRow(13);
            row.createCell(1).setCellValue("Class3");
            row = expectedSheet.createRow(14);
            row.createCell(1).setCellValue("3");
            row.createCell(2).setCellValue("name3");
            row.createCell(3).setCellValue("surname3");
            row = expectedSheet.createRow(15);
            row.createCell(1).setCellValue("A");
            row.createCell(2).setCellValue("B");
            row = expectedSheet.createRow(16);
            row.createCell(1).setCellValue("Company1: specialty1");
            row.createCell(2).setCellValue("Company2: specialty2");

            row = expectedSheet.createRow(18);
            row.createCell(1).setCellValue("Class4");
            row = expectedSheet.createRow(19);
            row.createCell(1).setCellValue("4");
            row.createCell(2).setCellValue("name4");
            row.createCell(3).setCellValue("surname4");
            row = expectedSheet.createRow(20);
            row.createCell(1).setCellValue("B");
            row = expectedSheet.createRow(21);
            row.createCell(1).setCellValue("Company2: specialty2");


            FileInputStream file = new FileInputStream(outputFile.toFile());
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            String workbookText = new XSSFExcelExtractor(workbook).getText();
            String expectedWorkbookText = new XSSFExcelExtractor(expectedWorkbook).getText();
            assertEquals(workbookText,expectedWorkbookText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the writePresentationAttendance() method, ensuring that it correctly writes the
     * presentation attendance sheet to an Excel file.
     */
    @Test
    void writePresentationAttendanceTest(){
        BoomData data = this.createDummyData();
        List<PlannedPresentation> plannedPresentations = new ArrayList<>();
        List<OfferedPresentation> offeredPresentations = new ArrayList<>();
        for(Company company : data.getCompanies())
        {
            offeredPresentations.addAll(company.getOfferedPresentations());
        }
        for(OfferedPresentation presentation : offeredPresentations)
        {
            plannedPresentations.addAll(presentation.getPlannedPresentations());
        }

        XlsxWriter testWriter = new XlsxWriter();
        Path outputFile = Path.of("src/test/results/Attendance_out.xlsx");
        try {
            testWriter.writePresentationAttendance(outputFile, plannedPresentations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            XSSFWorkbook expectedWorkbook = new XSSFWorkbook();
            Sheet expectedSheet = expectedWorkbook.createSheet();


            Row row = expectedSheet.createRow(0);
            row.createCell(0).setCellValue("Company1");
            row.createCell(1).setCellValue("Company1: specialty1");
            row.createCell(2).setCellValue("1");
            row.createCell(3).setCellValue("A");
            row = expectedSheet.createRow(2);
            row.createCell(0).setCellValue("1");
            row.createCell(1).setCellValue("name1");
            row.createCell(2).setCellValue("surname1");
            row = expectedSheet.createRow(3);
            row.createCell(0).setCellValue("2");
            row.createCell(1).setCellValue("name2");
            row.createCell(2).setCellValue("surname2");
            row = expectedSheet.createRow(4);
            row.createCell(0).setCellValue("3");
            row.createCell(1).setCellValue("name3");
            row.createCell(2).setCellValue("surname3");


            row = expectedSheet.createRow(5);
            row.createCell(0).setCellValue("Company2");
            row.createCell(1).setCellValue("Company2: specialty2");
            row.createCell(1).setCellValue("20");
            row.createCell(1).setCellValue("A");
            row = expectedSheet.createRow(7);
            row.createCell(0).setCellValue("2");
            row.createCell(1).setCellValue("name2");
            row.createCell(2).setCellValue("surname2");
            row = expectedSheet.createRow(8);
            row.createCell(0).setCellValue("3");
            row.createCell(1).setCellValue("name3");
            row.createCell(2).setCellValue("surname3");
            row = expectedSheet.createRow(9);
            row.createCell(0).setCellValue("4");
            row.createCell(1).setCellValue("name4");
            row.createCell(2).setCellValue("surname4");

            FileInputStream file = new FileInputStream(outputFile.toFile());
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            String workbookText = new XSSFExcelExtractor(workbook).getText();
            String expectedWorkbookText = new XSSFExcelExtractor(expectedWorkbook).getText();
            assertEquals(workbookText,expectedWorkbookText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the writeRoomTimetable() method, ensuring that it correctly writes the room timetable
     * to an Excel file.
     */
    @Test
    void writeRoomTimetableTest(){
        BoomData data = createDummyData();
        List<OfferedPresentation> offeredPresentations = new ArrayList<>();
        for(Company company : data.getCompanies())
        {
            offeredPresentations.addAll(company.getOfferedPresentations());
        }
        XlsxWriter testWriter = new XlsxWriter();
        Path outputFile = Path.of("src/test/results/Timetable_out.xlsx");
        try {
            testWriter.writeRoomTimetable(outputFile, offeredPresentations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            XSSFWorkbook expectedWorkbook = new XSSFWorkbook();
            Sheet expectedSheet = expectedWorkbook.createSheet();

            // write title
            Row titleRow = expectedSheet.createRow(0);
            Cell titleCell = titleRow.createCell(0, CellType.STRING);
            titleCell.setCellValue("Raumplan");

            // empty row
            expectedSheet.createRow(1);

            Row row = expectedSheet.createRow(2);
            row.createCell(1).setCellValue("Company1");
            row.createCell(2).setCellValue("Company1: specialty1");
            row.createCell(3).setCellValue("1");
            row = expectedSheet.createRow(3);
            row.createCell(1).setCellValue("Company2");
            row.createCell(2).setCellValue("Company2: specialty2");
            row.createCell(4).setCellValue("20");
            row = expectedSheet.createRow(4);
            row.createCell(1).setCellValue("Company3");
            row.createCell(2).setCellValue("Company3: specialty3");
            row = expectedSheet.createRow(5);
            row.createCell(1).setCellValue("Company4");
            row.createCell(2).setCellValue("Company4: specialty4");
            row = expectedSheet.createRow(6);
            row.createCell(1).setCellValue("Company5");
            row.createCell(2).setCellValue("Company5: specialty5");

            FileInputStream file = new FileInputStream(outputFile.toFile());
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            String workbookText = new XSSFExcelExtractor(workbook).getText();
            String expectedWorkbookText = new XSSFExcelExtractor(expectedWorkbook).getText();
            assertEquals(workbookText,expectedWorkbookText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
