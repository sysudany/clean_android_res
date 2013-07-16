package cn.chdany.clean.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * to log the program. 
 * @author chdany
 *
 */
public class Logger {
	
	private static PrintStream outputFile ;
	
	static {
		try {
			outputFile = new PrintStream(new FileOutputStream(new File("unused_resources")));
		} catch (FileNotFoundException e) {}
	}
	
	public static void log(Object msg){
		System.out.println(msg);
		if(outputFile != null){
			outputFile.println(msg);
		}
	}
	
	public static <T> void log(Iterable<T> Ts){
		for(T t : Ts){
			System.out.println(t.toString());
			if(outputFile != null){
				outputFile.println(t.toString());
			}
		}
	}
	
	public static void closeFile(){
		if(outputFile != null){
			outputFile.close();
		}
	}
}
