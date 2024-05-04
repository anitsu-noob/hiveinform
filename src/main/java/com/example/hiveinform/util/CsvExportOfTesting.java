package com.example.hiveinform.util;

import com.example.hiveinform.entity.Testing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvExportOfTesting {
    static final private String LOCATION_URL = "E:/var/" ;
    public boolean createAndWriteCSV(String fileName, List<Testing> testings , String writeHead) throws IOException {
        File file = new File(LOCATION_URL+fileName);
        try(FileOutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(writeHead.getBytes(StandardCharsets.UTF_8));
            outputStream.write('\n');
            for (Testing testing : testings) {
                String input = testing.getId()+","+testing.getTitle()+","+testing.getContent()+","+testing.getAnswer()+","+testing.getAttribute()+","+testing.getType();
                outputStream.write(input.getBytes(StandardCharsets.UTF_8));
                outputStream.write('\n');
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
