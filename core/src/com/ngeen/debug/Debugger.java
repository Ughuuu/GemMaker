package com.ngeen.debug;

public class Debugger {
    static StringBuilder buffer = new StringBuilder();

    public static String emptyBuffer() {
        String line = buffer.toString();
        return line;
    }

    public static void log() {
        System.out.println();
    }

    public static void log(Object line) {
        System.out.println(line);
        try {
            // Files.write(Paths.get("debug.txt"),
            // (line.toString()+'\n').getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void print(Object line) {
        buffer.append(line);
    }

    public static void println() {
        buffer.append("\n");
    }

    public static void println(Object line) {
        buffer.append(line + "\n");
    }
}
