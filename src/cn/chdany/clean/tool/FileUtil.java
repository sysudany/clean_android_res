package cn.chdany.clean.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * for searching with spacific requirement
 * @author chdany
 *
 */
public class FileUtil {
	
	public static final File findFileInFolderWithPrefix(File folder, String prefix){
		File[] files = folder.listFiles();
		for(File file : files){
			String fileName = file.getName();
			int indexOfDot = fileName.indexOf(".");
			if(indexOfDot == -1 && TextUtils.equals(prefix, fileName)){
				return file;
			}else{
				String fileNameWithOutDot = fileName.substring(0, indexOfDot);
				if(TextUtils.equals(fileNameWithOutDot, prefix)){
					return file;
				}
			}
		}
		return null;
	}
	
	// to find the R.java file
	public static final File findUniqueFileByNameInDirectory(String fileName, File dir){
		File root = dir;
		_while : while(root.isDirectory() && root.listFiles().length > 0){
			File[] children = root.listFiles();
			for(File child : children){
				if(child.isDirectory()){
					root = child;
					continue _while;
				}else{
					if(TextUtils.equals(fileName, child.getName())){
						return child;
					}
				}
			}
		}
		return null;
	}
	
	public static final List<File> findSubFoldersWithPreffix(File resRoot, final String prefix){
		File[] subFles = resRoot.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isDirectory() && name.startsWith(prefix);
			}
		});
		return Arrays.asList(subFles);
	}
	
	public static final List<File> findFilesWithSuffix(File dir, String suffix){
		ArrayList<File> originList = new ArrayList<File>();
		addFilesWithSuffix(originList, dir, suffix);
		return originList;
	}
	
	private static void addFilesWithSuffix(List<File> originList, File file, final String suffix) {
		if(file.isDirectory()){
			File[] files = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return dir.isDirectory() || name.endsWith(suffix);
				}
			});
			for(File f : files){
				addFilesWithSuffix(originList, f, suffix);
			}
		}
		if(file.getName().endsWith(suffix)){
			originList.add(file);
		}
	}
	
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        File parent = targetFile.getParentFile();
        while(!parent.exists()){
        	parent.mkdirs();
        }
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
	
}
