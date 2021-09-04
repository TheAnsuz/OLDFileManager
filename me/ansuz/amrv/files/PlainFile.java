package me.ansuz.amrv.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A file handler provided by the FileManager, this class adds the option for an
 * easy reading and writing of files without consuming so much resources,
 * however this class is dedicated to operate with plain texts as if the whole
 * file was a string, not recomended to use for big files
 * 
 * @author Ansuz
 *
 */
public class PlainFile extends BaseFile {

	private String data = "";

	/**
	 * Creator for the plain file, use
	 * 
	 * <pre>
	 * FileManager.getPlainFile()
	 * </pre>
	 * 
	 * instead
	 * 
	 * @param file
	 */
	protected PlainFile(File file) {
		super(file);
		this.reloadProcess();
	}

	@Override
	protected boolean reloadProcess() {
		if (!file.canRead()) {
			System.err.println("Cant read file " + file.getName() + " access blocked");
			return false;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.ready()) {
				data = data + reader.readLine();
			}
			reader.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * Gets the holded data from the file, if the file has been changed manually or
	 * using other methods, this will not take again, this will recover the data
	 * from the instance that was already obtained using the <code>reload()</code>
	 * method
	 * 
	 * @return a string containing the whole file, not recomended for large files
	 */
	public String getData() {
		return data;
	}

	/**
	 * Overrides the whole data from the file using the given string
	 * 
	 * @param data - information to be set as the data
	 */
	public void setData(final String data) {
		this.data = data;
	}

	/**
	 * Adds the given character to the end of the file
	 * 
	 * @param character to add to the file
	 */
	public void addData(final char character) {
		this.addData(character + "");
	}
	
	/**
	 * Adds the given character array to the end of the file
	 * 
	 * @param characters to append to the file
	 */
	public void addData(final char[] characters) {
		this.addData(characters.toString());
	}
	/**
	 * Adds the given data to the data already on the file
	 * 
	 * @param data - a string containing the data to be added
	 */
	public void addData(final String data) {
		if (data.length() < 65536)
			this.data = this.data + data;
		else {
			StringBuilder b = new StringBuilder(this.data);
			b.append(data);
			this.data = b.toString();
		}
	}

	/**
	 * Clears the whole information stored, if you dont save after this, the file
	 * will not be cleared, this changes just the information that is being holded,
	 * the data after this operation will be just a empty string
	 */
	@Override
	public void clear() {
		data = "";
	}

	@Override
	protected final void buffersave() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(data);
		writer.close();
	}

	@Override
	protected final void writersave() throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(data);
		writer.close();
	}

}
