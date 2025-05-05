package com.gamestore.grt.service.strategy;

import com.gamestore.grt.dto.GameDto;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.capitalize;

public class ExportService {

    public void exportToExcel(HttpServletResponse response, Map<String, List<GameDto>> gamesByStore) throws IOException{
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            for (Map.Entry<String, List<GameDto>> entry : gamesByStore.entrySet()) {
                boolean includeReleaseDate = false;
                String store = entry.getKey();
                if(store.equals("nintendo-jp")){
                    includeReleaseDate = true;
                }
                String sheetName = capitalize(entry.getKey());
                writeSheet(workbook, sheetName, entry.getValue(), includeReleaseDate);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String filename = "games_" + java.time.LocalDate.now() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            workbook.write(response.getOutputStream());
        }
        catch (Exception e){
            throw new RuntimeException("Cannot create excel sheets");
        }

    }


    public void writeSheet(Workbook workbook, String sheetName, List<GameDto> games, boolean includeReleaseDate){
        Sheet sheet = workbook.createSheet(sheetName);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Game Name");
        header.createCell(1).setCellValue("Store Link");
        if (includeReleaseDate) {
            header.createCell(2).setCellValue("Tentative Release Date");
        }
        // create if else for release date games


        int rowNum = 1;
        for(GameDto game: games){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(game.getTitle());
            row.createCell(1).setCellValue(game.getStoreUrl());
            if (includeReleaseDate) {
                String releaseDate = game.getDsDate(); // Should be "dd-MM-yyyy"
                row.createCell(2).setCellValue(releaseDate != null ? releaseDate : "");
            }

        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        if (includeReleaseDate) {
            sheet.autoSizeColumn(2);
        }
    }
}
