package itf221.gvi.boom.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

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
}
