package cn.chdany.clean.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chdany.clean.bean.AndroidRes;
import cn.chdany.clean.tool.FileUtil;
import cn.chdany.clean.tool.LineReader;
import cn.chdany.clean.tool.Logger;
import cn.chdany.clean.tool.TimeAnalysis;

/**
 * steps to cleans the unused res
 * @author chdany
 *
 */
public class CleanLogic implements ICleanLogic{
	private File mRootDir;

	private File mManifestFile;

	private File mR_file;
	private File mSrcDir;
	private File mGenDir;
	private File mResDir;
	private List<File> mDrawableDirs;
	private List<File> mLayoutDirs;
	private List<File> mXmlFiles;
	private List<File> mJavaFiles;
	
	private File mBackupDir;

	public CleanLogic(File androidProjectRootDir) {
		if(androidProjectRootDir == null || !androidProjectRootDir.exists()
				|| !androidProjectRootDir.isDirectory()){
			throw new RuntimeException("please check if the project root dir exit");
		}
		mRootDir = androidProjectRootDir;
		init();
	}

	private void init() {
		TimeAnalysis.startAnalysis("init()");
		mManifestFile = new File(mRootDir, "AndroidManifest.xml");
		mSrcDir = new File(mRootDir, "src");
		mResDir = new File(mRootDir, "res");
		mGenDir = new File(mRootDir, "gen");
		log("mManifestFile.exists() -- " + mManifestFile.exists());
		log("mSrcDir.exists() --" + mSrcDir.exists());
		log("mResDir.exists() --" + mResDir.exists());
		log("mGenDir.exists() --" + mGenDir.exists());

		mR_file = FileUtil.findUniqueFileByNameInDirectory("R.java", mGenDir);
		log("mR_file.exists() --" + (mR_file != null && mR_file.exists()));

		mDrawableDirs = FileUtil.findSubFoldersWithPreffix(mResDir, "drawable");
		mLayoutDirs = FileUtil.findSubFoldersWithPreffix(mResDir, "layout");
		log(mDrawableDirs);
		log(mLayoutDirs);

		mJavaFiles = FileUtil.findFilesWithSuffix(mSrcDir, ".java");
		mXmlFiles = FileUtil.findFilesWithSuffix(mResDir, ".xml");
		if(mManifestFile.exists()){
			mXmlFiles.add(mManifestFile);
		}
		
		log("mJavaFiles.size() --" + mJavaFiles.size());
		log("mXmlFiles.size() --" + mXmlFiles.size());
		
		
		mBackupDir = new File(mRootDir, "backup");
		TimeAnalysis.endAnalysis("init()");
	}


	@Override
	public List<AndroidRes> getAndroidResources(IProgressCallback callback) {
		TimeAnalysis.startAnalysis("getAndroidResources()");
		final Pattern pattern1 = Pattern.compile("public static final class (.+?) ");
		final Pattern pattern2 = Pattern.compile("public static final int(\\[\\])? (.+?)=");
		final List<AndroidRes> resList = new ArrayList<AndroidRes>();
		new LineReader() {
			String currentLable = null;
			@Override
			protected void resolveLine(String content) {
				Matcher matcher1 = pattern1.matcher(content);
				if(matcher1.find()){
					currentLable = matcher1.group(1);
				}else{
					Matcher matcher2 = pattern2.matcher(content);
					if(matcher2.find()){
						String name = matcher2.group(2);
						resList.add(new AndroidRes(currentLable, name));
					}
				}
			}
		}.execute(mR_file);
		log("resList.size() --" + resList.size());
		TimeAnalysis.endAnalysis("getAndroidResources()");
		return resList;
	}

	@Override
	public List<AndroidRes> searchUnusedResources(IProgressCallback callback, List<AndroidRes> allResources) {
		TimeAnalysis.startAnalysis("searchUnusedResources()");
		res_traversal : for(final AndroidRes res : allResources){
			for(File javaFile : mJavaFiles){
				new LineReader() {
					@Override
					protected void resolveLine(String line) {
						if(line.contains(res.toJavaSearchString())){
							res.setUsed(true);
							close();
						}
					}
				}.execute(javaFile);
				if(res.isUsed()){
					continue res_traversal;
				}
			}
			
			for(File xmlFile : mXmlFiles){
				new LineReader() {
					@Override
					protected void resolveLine(String line) {
						if(line.contains(res.toXmlSearchString())){
							res.setUsed(true);
							close();
						}
					}
				}.execute(xmlFile);
				if(res.isUsed()){
					continue res_traversal;
				}
			}
		}
		ArrayList<AndroidRes> unusedResources = new ArrayList<AndroidRes>();
		for(AndroidRes res : allResources){
			if(!res.isUsed()){
				unusedResources.add(res);
			}
		}
		log("unusedResources.size() --" + unusedResources.size());
		TimeAnalysis.endAnalysis("searchUnusedResources()");
		return unusedResources;
	}

	@Override
	public List<File> findUnusedResourcesFiles(IProgressCallback callback,
			List<AndroidRes> unusedResources) {
		TimeAnalysis.startAnalysis("findUnusedResourcesFiles()");
		ArrayList<File> unusedFiles = new ArrayList<File>();
		for(AndroidRes res : unusedResources){
			if("drawable".equals(res.getType())){
				for(File drawableDir : mDrawableDirs){
					File unusedFile = FileUtil.findFileInFolderWithPrefix(drawableDir, res.getName());
					if(unusedFile != null){
						unusedFiles.add(unusedFile);
					}
				}
			}
			if("layout".equals(res.getType())){
				for(File layoutDir : mLayoutDirs){
					File unusedFile = FileUtil.findFileInFolderWithPrefix(layoutDir, res.getName());
					if(unusedFile != null){
						unusedFiles.add(unusedFile);
					}
				}
			}
		}
		Collections.sort(unusedFiles);
		log("unusedFiles.size() --" + unusedFiles.size());
		log(unusedFiles);
		TimeAnalysis.endAnalysis("findUnusedResourcesFiles()");
		return unusedFiles;
	}

	@Override
	public void doTheClean(IProgressCallback callback, List<File> unusedFiles) {
		TimeAnalysis.startAnalysis("doTheClean()");
		try {
			for(File unusedFile : unusedFiles){
				File backupFile = new File(new File(mBackupDir, unusedFile.getParentFile().getName()),unusedFile.getName());
				FileUtil.copyFile(unusedFile, backupFile);
				unusedFile.delete();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		TimeAnalysis.endAnalysis("doTheClean()");
	}


	private void log(Object msg){
		Logger.log(msg);
	}
	
	private <T> void log(Iterable<T> Ts){
		Logger.log(Ts);
	}

}
