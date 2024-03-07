package org.llesha;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExternalSorterTest {
    @Test
    void testExternalSorter() {
        File example = new File("example.csv");

        new FileGenerator().generate(example, 20);
        new ExternalSorter().sort(example);

        validateSortedFile(example);
        assertTrue(example.delete(), "test file not deleted");
    }

    private void validateSortedFile(File file) {
        long prev = Long.MIN_VALUE;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                LineWithKey lineWithKey = new LineWithKey(line);
                assertTrue(prev < lineWithKey.getKey());
                prev = lineWithKey.getKey();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
