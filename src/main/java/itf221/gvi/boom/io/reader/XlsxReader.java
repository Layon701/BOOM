package itf221.gvi.boom.io.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader class to read xlsx-files
 */
public class XlsxReader implements Reader {
    /**
     * method to read a xlsx-file and return the data from it.
     * Replaces missing fields with an empty String
     *
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

        Row headerRow = sheet.getRow(0);
        int maxColumns = 0;
        if (headerRow != null) {
            for (int i = 0; ; i++) {
                Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell.getCellType() == CellType.BLANK ||
                        (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
                    break;
                }
                maxColumns++;
            }
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;
            List<String> cellList = new ArrayList<>();
            for (int i = 0; i < maxColumns; i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                switch (cell.getCellType()) {
                    case STRING:
                        cellList.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        cellList.add(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    default:
                        cellList.add("");
                        break;
                }
            }
            excelData.add(cellList);
        }
        workbook.close();
        file.close();
        return excelData;
    }
}
