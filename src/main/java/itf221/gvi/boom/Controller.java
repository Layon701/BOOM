package itf221.gvi.boom;

import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.io.IOManager;

import java.nio.file.Path;
import java.util.List;

public class Controller {

    public static void main(String[] args) {
        GUI gui = new GUI();

        // this should be filled by the GUI
        Path studentsFilePath = Path.of("src/test/resources/IMPORT BOT2_Wahl.xlsx");
        Path companyFilePath = Path.of("src/test/resources/IMPORT BOT1_Veranstaltungsliste.xlsx");
        Path roomFilePath = Path.of("src/test/resources/IMPORT BOT0_Raumliste.xlsx");
        Path outputFolderPath = Path.of("src/test/resources/");

        // on GUI run button
        int score = runDistributionAlg(studentsFilePath, companyFilePath, roomFilePath, outputFolderPath);

        // write output
    }

    public static int runDistributionAlg(Path studentsFilePath, Path companyFilePath, Path roomFilePath, Path outputFolderPath) {
        // retrieve data
        IOManager ioManager = new IOManager(studentsFilePath, companyFilePath, roomFilePath, outputFolderPath);
        BoomData boomData = ioManager.readFiles();

        // distribution algorithm
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
        return roomManagementUnit.execute(boomData);
    }
}
