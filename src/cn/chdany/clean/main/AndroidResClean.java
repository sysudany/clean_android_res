package cn.chdany.clean.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;

import cn.chdany.clean.bean.AndroidRes;

/**
 * Tools to clean up the unused resource of android project. 
 * based on text search.
 * "-console" without UI interface
 * "-f param" the folderpath of androidProjectRootDir
 * @author chdany
 *
 */
public class AndroidResClean {
	
	public static void main(String[] args) throws Exception{
		if(args != null && args.length > 0 && "-console".equals(args[0])){
			String androidProjectRootDir = System.getProperty("user.dir");
			if(args.length == 3){
				if("-f".equals(args[1]) && args[2] != null && !"".equals(args[2])){
					androidProjectRootDir = args[2];
				}
			}
			new AndroidResClean.Console(androidProjectRootDir);
		}else{
			new AndroidResClean.UI();
		}
	}
	
	public AndroidResClean() {
	
	}
	
	public static class UI extends JFrame{
		private static final long serialVersionUID = 1L;

		public UI() {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setSize(screenSize.width * 2 / 3 , screenSize.height * 3 / 4);
			setResizable(false);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
		}
	}
	
	public static class Console {
		public Console(String projectRootPath){
			ICleanLogic cleanLogic = new CleanLogic(new File(projectRootPath));
			List<AndroidRes> allResources = cleanLogic.getAndroidResources(null);
			List<AndroidRes> unusedResources = cleanLogic.searchUnusedResources(null, allResources);
			List<File> unusedResourcesFiles = cleanLogic.findUnusedResourcesFiles(null, unusedResources);
//			cleanLogic.doTheClean(null, unusedResourcesFiles);
		}
	}
	
}
