package bsaRandomizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	
	private static LinkedList<String> allFilePaths;
	
	public static void main(String[] args) {
		System.out.println("");
		System.out.println("***** Randomizing Your Files *****");
		File basefolder = new File(args[0].toString());
				// new File("C:\\Users\\Wulfex\\Desktop\\New folder\\sound");
		File copyfolder = new File(args[1].toString());
				//new File("C:\\Users\\Wulfex\\Desktop\\testfiles");
		
		allFilePaths = getFilePathsFromSubDirs(basefolder.getPath());
		
		try {
		randomizeFiles(basefolder, copyfolder);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("");
		System.out.println("***** Randomization Complete *****");
	}
	
	private static void randomizeFiles(File baseFile, File copyFile) throws IOException {
		File nextFile;
		File nextCopyFile;
		
		for(String currFile : baseFile.list()){
			nextFile = new File(baseFile.getPath() + "\\" + currFile);
			nextCopyFile = new File(copyFile.getPath() + "\\" + currFile);
			
			if(nextFile.isDirectory()) {
				// if copy folder doesn't exist, create it
				if(!nextCopyFile.exists()) {
					nextCopyFile.mkdir();
				}
				// recurse into the current folder to look for files
				randomizeFiles(nextFile, nextCopyFile);
			}
			else if(nextFile.isFile()) {
				// if the file exists, delete it
				if(nextCopyFile.exists()) {
					nextCopyFile.delete();
				}
				
				// copy random file
				copyFile(getRandomFile(), nextCopyFile);
				System.out.print("\r                                     ");
				System.out.print("\rFiles Remaing: " + allFilePaths.size());
			}
		}
	}
	
	private static void copyFile(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	private static File getRandomFile() {
		int randomFileIndex = ThreadLocalRandom.current().nextInt(0, allFilePaths.size());
		return new File(allFilePaths.remove(randomFileIndex));
	}

	private static LinkedList<String> getFilePathsFromSubDirs(String filePath) {
		LinkedList<String> filePaths = new LinkedList<String>();
		File parentDir = new File(filePath);
		File currFile;
		String currPath;
		
		for (String fileName : parentDir.list()) {
			currPath = parentDir.getPath()+ "\\" + fileName;
			currFile = new File(currPath);
			
			// if file add to list
			if(currFile.isFile()) {
				filePaths.add(currPath);
			}
			// else if directory, recurse and add return
			else if(currFile.isDirectory()) {
				filePaths.addAll(getFilePathsFromSubDirs(currPath));
			}
		}
		
		return filePaths;
	}
}