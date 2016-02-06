package com.ngeen.debug;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Debugger {
	public static void println(Object line) {
		System.out.println(line);
	}

	public static void print(Object line) {
		System.out.print(line);
	}

	public static void println() {
		System.out.println();
	}

	public static void log() {
		System.out.println();
	}

	public static void log(Object line) {
		System.out.println(line);
		try {
			//Files.write(Paths.get("debug.txt"), (line.toString()+'\n').getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
