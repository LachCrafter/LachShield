package de.lachcrafter.lachshield.util;

import de.lachcrafter.lachshield.LachShield;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {

    /**
     * Gets the content of a file as a list of strings.
     * @param file file to get the content from
     * @return A list of strings, each string contains a line from the file.
     */
    @Nullable
    public static List<String> getFileContentAsList(@NotNull File file) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            List<String> lineList = new ArrayList<>();

            bufferedReader.lines().forEach(lineList::add);

            return lineList;
        } catch (IOException e) {
            LachShield.LOGGER.error("An unexpected error occurred while reading file: {}", file.getName());
            LachShield.LOGGER.error("Stack trace: ", e);
            return null;
        }
    }

    /**
     * Writes a stream of strings into a file.
     * @param contentStream the stream of strings
     * @param file file to write the stream to
     * @return if it ran successfully
     */
    public static boolean writeContentToFile(Stream<String> contentStream, File file) {
        String[] contentArray = contentStream.toArray(String[]::new);

        try (FileWriter fileWriter = new FileWriter(file)) {

            for (String line : contentArray) {
                fileWriter.write(line + "\n");
            }
            return true;
        } catch (IOException e) {
            LachShield.LOGGER.error("An unexpected error occurred while writing to file: {}", file.getName());
            LachShield.LOGGER.error("Stack trace: ", e);
            return false;
        }
    }
}
