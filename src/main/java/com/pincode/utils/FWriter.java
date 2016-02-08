package com.pincode.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FWriter {

	private static final Logger LOG = Logger.getLogger(FWriter.class);

	private String path;

	private FileWriter fw;

	public FWriter(String path) {
		super();
		this.path = path;
	}

	/**
	 * Write to file
	 * 
	 * @param append
	 *            - append mode if "true"
	 * @throws IOException
	 */
	public void write(boolean append, String string) {
		try {
			fw = new FileWriter(new File(path), append);
			fw.write(string+"\r\n");

		} catch (IOException e) {
			LOG.error("Write error -> " + e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				LOG.error("Close file error -> " + e);
			}
		}

	}
}
