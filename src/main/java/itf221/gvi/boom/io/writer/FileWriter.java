package itf221.gvi.boom.io.writer;

import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileWriter {
    void writeStudentPlan(Path path, List<Student> students) throws IOException;

    void writePresentationAttendance(Path path, List<PlannedPresentation> presentations) throws IOException;

    void writeRoomTimetable(Path path, List<PlannedPresentation> presentations) throws IOException;
}
