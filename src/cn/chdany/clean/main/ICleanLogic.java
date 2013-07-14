package cn.chdany.clean.main;

import java.io.File;
import java.util.List;

import cn.chdany.clean.bean.AndroidRes;

/**
 * interface to declare the todo list
 * @author chdany
 *
 */
public interface ICleanLogic {
	
	// 1. get all res id of android resources
	public List<AndroidRes> getAndroidResources(IProgressCallback callback);
	
	// 2. search all java files and xml files to find out weathe the res is used
	public List<AndroidRes> searchUnusedResources(IProgressCallback callback, List<AndroidRes> allResources) ;
	
	// 3. find the res file of the unused resid
	public List<File> findUnusedResourcesFiles(IProgressCallback callback, List<AndroidRes> allResources);
	
	// 4. delete and save the deleted file 
	public void doTheClean(IProgressCallback callback, List<File> unusedFiles);
	
	public interface IProgressCallback {
		void onProgressChanged(Object extra, int progress);
		void onFinished();
	}
}
