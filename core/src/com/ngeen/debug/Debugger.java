package com.ngeen.debug;

public class Debugger {
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
		System.out.print(line);
	}

	public static void println() {
		System.out.println();
	}

	public static void println(Object line) {
		System.out.println(line);
	}
}
