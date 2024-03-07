package org.llesha;

public class Config {
    private static String delimiter = ";";

    public static void setDelimiter(String delimiter) {
        Config.delimiter = delimiter;
    }

    public static String getDelimiter() {
        return delimiter;
    }
}
