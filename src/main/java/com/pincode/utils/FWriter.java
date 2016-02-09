package com.pincode.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FWriter {

	private static final Logger LOG = Logger.getLogger(FWriter.class);

	private String path;
	private FileWriter fw;
	private File file;

	public FWriter(String path) throws IOException {
		this.path = path;
		file = new File(path);
		if(file.exists() && file.isFile()){
			file.delete();
		}
	}

	/**
	 * Write to file
	 * 
	 * @param append
	 *            - append mode if "true"
	 * @throws IOException
	 */
	public void write(String string) {
		try {
			fw = new FileWriter(file, true);
			fw.write(string+"\r\n");
		} catch (IOException e) {
			LOG.error("Write error -> " + e);
		} finally{
			try {
				fw.close();
			} catch (IOException e1) {
			}
		}
	}
}
