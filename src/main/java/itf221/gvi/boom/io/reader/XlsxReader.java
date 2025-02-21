package itf221.gvi.boom.io.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XlsxReader implements Reader{
    /**
     * method to read a xlsx-file and return the data from it.
     * Replaces missing fields with an empty String
     * @param path to the xlsx-file
     * @return excelData as List<List<String>>
     * @throws IOException when file is missing
     */
    @Override
    public List<List<String>> readFile(Path path) throws IOException {
        FileInputStream file = new FileInputStream(path.toFile());
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> excelData = new ArrayList<>();

        int maxColumns = sheet.getLastRowNum();

        for (Row row : sheet){
            List<String> cellList = new ArrayList<>();
            for (int i = 0; i < maxColumns; i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                switch (cell.getCellType()){
                    case STRING: cellList.add(cell.getStringCellValue());
                        break;
                    case NUMERIC: cellList.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    default: cellList.add("");
                        break;
                }
            }
            excelData.add(cellList);
        }
        return excelData;
    }
}
