package cn.chdany.clean.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * readLine tool
 * @author chdany
 *
 */
public abstract class LineReader{
	protected abstract void resolveLine(String line);
	
	private boolean close;
	
	public void execute(File file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null && !close){
				if(!TextUtils.isEmpty(line)){
					resolveLine(line);
				}
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void close(){
		close = true;
	}
	
	
}
