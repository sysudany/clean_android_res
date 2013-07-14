package cn.chdany.clean.tool;

/**
 * to log the program. 
 * @author chdany
 *
 */
public class Logger {
	public static void log(Object msg){
		System.out.println(msg);
	}
	
	public static <T> void log(Iterable<T> Ts){
		for(T t : Ts){
			System.out.println(t.toString());
		}
	}
}
