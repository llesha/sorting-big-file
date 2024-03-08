package org.llesha;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalSorter {
    private static final int CHUNK_SIZE = 1 << 2;
    private static final String PREFIX = "sortTemp";
    private static final String FORMAT = ".csv";

    public void sort(File file) {
        int chunkIndex = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<LineWithKey> chunkToSort = new ArrayList<>();
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                if (i == CHUNK_SIZE) {
                    sortAndWrite(chunkToSort, chunkIndex++);
                    i = 0;
                }
                chunkToSort.add(new LineWithKey(line));
                i++;
            }
            sortAndWrite(chunkToSort, chunkIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        merge(0, chunkIndex, file);
        deleteTempFiles(chunkIndex);
    }

    private void sortAndWrite(List<LineWithKey> chunkToSort, int chunkIndex) throws IOException {
        // this also can be written by hand, but it's not the point of the task
        Collections.sort(chunkToSort);
        File file = new File(getFileName(chunkIndex));
        if (file.exists()) {
            throw new RuntimeException("Please move file " + file.getName() + " to another directory for program to run.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int i = 0;
            while (i < chunkToSort.size()) {
                writer.write(chunkToSort.get(i).toString());
                writer.newLine();
                i++;
            }
        }
        chunkToSort.clear();
    }

    private int merge(int start, int end, File topLevel) {
        if (start == end) {
            return start;
        }
        if (end - start != 1) {
            start = merge(start, (start + end) / 2, null);
            end = merge((start + end) / 2 + 1, end, null);
        }
        List<LineWithKey> firstInMemory = readFile(start);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(topLevel == null ? new File(getFileName(start)) : topLevel))) {
            mergeSortTwoFiles(end, firstInMemory, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return start;
    }

    private void mergeSortTwoFiles(int second, List<LineWithKey> firstInMemory, BufferedWriter writer) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(getFileName(second)))) {
            int i = 0;
            String line;
            while ((line = br.readLine()) != null) {
                LineWithKey fromSecond = new LineWithKey(line);
                while (i < firstInMemory.size() && firstInMemory.get(i).compareTo(fromSecond) < 0) {
                    writer.write(firstInMemory.get(i).toString());
                    writer.newLine();
                    i++;
                }
                writer.write(fromSecond.toString());
                writer.newLine();
            }
            while (i < firstInMemory.size()) {
                writer.write(firstInMemory.get(i++).toString());
                writer.newLine();
            }
        }
    }

    private List<LineWithKey> readFile(int number) {
        File file = new File(getFileName(number));
        List<LineWithKey> chunkToSort = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                chunkToSort.add(new LineWithKey(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(chunkToSort);
        return chunkToSort;
    }

    private void deleteTempFiles(int end) {
        for (int i = 0; i <= end; i++) {
            new File(getFileName(i)).delete();
        }
    }

    private static String getFileName(int number) {
        return PREFIX + number + FORMAT;
    }
}
