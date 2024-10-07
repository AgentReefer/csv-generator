package com.reportgenerator.csv;

import com.reportgenerator.Utils.ReflectionUtils;
import com.reportgenerator.exceptions.NoCsvColumnsException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CsvGenerator {

    private static final Logger logger = Logger.getLogger(CsvGenerator.class.getName());
    public static  <T> void generateCsvToFile(List<T> objects, String filePath) throws IOException, IllegalArgumentException, NoCsvColumnsException, IllegalAccessException {

        if (objects.isEmpty()) {
           throw new IllegalArgumentException("No data to write to CSV.");
        }
        Class<?> clazz = objects.get(0).getClass();

        if (!ReflectionUtils.hasCsvColumns(clazz)){
            throw new NoCsvColumnsException( "No fields annotated with @CsvColumn found in class: " + clazz.getName());
        }
        if (!filePath.endsWith(".csv")) {
            throw new IllegalArgumentException("File must be .csv ");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            String header = ReflectionUtils.getCsvHeader(objects.get(0).getClass());
            writer.write(header);
            writer.newLine();

            for (T object : objects) {
                String row = ReflectionUtils.getCsvRow(object);
                writer.write(row);
                writer.newLine();
            }
        } catch (NoCsvColumnsException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        logger.log(Level.INFO, "CSV generated successfully: " + filePath);
    }
}
