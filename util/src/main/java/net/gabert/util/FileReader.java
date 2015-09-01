package net.gabert.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Robert Gallas
 */
public final class FileReader {
    public static String readFile(String aFileName) throws IOException, URISyntaxException {
        return aFileName.startsWith("classpath:") ? FileReader.readClasspathFile(aFileName)
                                                  : FileReader.readUrlFile(aFileName);

    }

    private static String readClasspathFile(final String fileName) throws IOException, URISyntaxException {
        String normalizedFileName = fileName.replace("classpath:", "");
        URI fileNameUri =  FileReader.class.getClassLoader().getResource(normalizedFileName).toURI();
        byte[] fileBytes = Files.readAllBytes(Paths.get(fileNameUri));
        return new String(fileBytes);
    }

    private static String readUrlFile(String aFileName) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(aFileName));
        return new String(fileBytes);
    }
}
