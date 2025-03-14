package itf221.gvi.boom.io;

import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.Room;
import itf221.gvi.boom.data.Student;
import itf221.gvi.boom.exceptions.BoomDataCreationException;
import itf221.gvi.boom.exceptions.EmptyFileException;
import itf221.gvi.boom.exceptions.InterpretException;
import itf221.gvi.boom.io.reader.Reader;
import itf221.gvi.boom.io.reader.XlsxReader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The IOManager class encapsulates the file paths used for various input and output operations within the application.
 * This class holds Variables to:
 * The file paths where the students, company and room data files are located.
 * The folder path where output files are saved.
 */
@AllArgsConstructor
@Getter
public class IOManager {

    /**
     * The file path to the students data file.
     */
    private Path studentsFilePath;

    /**
     * The file path to the company data file.
     */
    private Path companyFilePath;

    /**
     * The file path to the room data file.
     */
    private Path roomFilePath;

    /**
     * The folder path where output files are saved.
     */
    private Path outputFolderPath;

    /**
     * Method to fill and return a BoomData object
     *
     * @return BoomDate object with rooms, companies and students
     */
    public BoomData readFiles() {
        Reader reader = new XlsxReader();

        List<Room> rooms;
        List<Company> companies;
        List<Student> students;

        try {
            rooms = RoomInterpreter.interpret(reader.readFile(roomFilePath));
            companies = CompanyInterpreter.interpret(reader.readFile(companyFilePath));
            students = StudentInterpreter.interpret(reader.readFile(studentsFilePath), companies);
        } catch (IOException | InterpretException e) {
            throw new BoomDataCreationException("Error while creating BoomData  \r\n", e);
        }

        if (rooms.isEmpty() || companies.isEmpty() || students.isEmpty()) {
            throw new EmptyFileException("File had no content");
        }
        return new BoomData(rooms, companies, students);
    }

    public void writeFiles() {
    }
}
