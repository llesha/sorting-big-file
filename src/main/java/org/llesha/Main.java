package org.llesha;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            throw new RuntimeException("Expected file name as an argument");
        }
        ExternalSorter externalSorter = new ExternalSorter();
        externalSorter.sort(new File(args[0]));
    }
}
