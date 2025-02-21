package itf221.gvi.boom.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@AllArgsConstructor
@Getter
public class IOManager {
    private Path studentsFilePath;
    private Path companyFilePath;
    private Path roomFilePath;
    private Path outputFolderPath;
}
