package me.ansuz.amrv.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A file handler provided by the file manager for easier file operations, this
 * one is prefered to be use for list files as the file is managed as a list of
 * strings
 * 
 * @author Ansuz
 *
 */
public class ListFile extends BaseFile {

	private List<String> data = new ArrayList<String>();

	/**
	 * Constructor of the list file, you must use
	 * 
	 * <pre>
	 * FileManager.getListFile()
	 * </pre>
	 * 
	 * instead
	 * 
	 * @param file
	 */
	protected ListFile(File file) {
		super(file);
	}

	@Override
	protected boolean reloadProcess() {
		if (data == null)
			data = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			while (reader.ready()) {
				data.add(reader.readLine());
			}

			reader.close();
			return true;
		} catch (Exception e) {
			if (FileManager.isDebugEnabled())
				System.err.println(
						FileManager.WARNING + "file " + file.getName() + " got an error while reading the file");
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * Gets the holded data from the file in a string, if the file has been changed
	 * manually or using other methods this will not get that information, instead
	 * will get the information the file had in the instance the
	 * 
	 * <pre>
	 * reload()
	 * </pre>
	 * 
	 * method was called
	 * 
	 * @return a string containing the value of the list, if the list does not
	 *         contain the field, this will return an empty string
	 * @param field the key of the list
	 */
	public String getData(final int index) {
		if (index < 0 || index > data.size())
			return "";
		else
			return data.get(index);
	}

	/**
	 * Obtain the size of the file, returns 0 if empty and if the value is higher than <code>Integer.MAX_VALUE</code> will return that amount
	 * 
	 * @return the length as a integer
	 */
	public int length() {
		return data.size();
	}
	
	/**
	 * Changes the data from one field of the list, if the list contains it, this
	 * will override it, if it doesnt, it will just add it
	 * 
	 * @param index of the list that holds that information
	 * @param data  inside the field of the list
	 */
	public void setData(final int index, final String data) {
		this.setData(index, data, false);
	}

	/**
	 * Changes the data from one field of the list, if the list contains it, this
	 * will override it, if it doesnt, it will just add it
	 * 
	 * @param index     of the list that holds that information
	 * @param data      inside the field of the list
	 * @param addIfNull add the data at the end of the file if the index is out of
	 *                  bounds
	 */
	public void setData(final int index, final String data, final boolean addIfNull) {
		if (index < 0 || index >= this.data.size())
			if (addIfNull)
				this.addnewLine(data);
			else
				System.err.println(
						"ListFile: " + file.getName() + " \"setData(" + index + ")\" wasnt added, INVALID INDEX");
		else
			this.data.set(index, data);
	}

	/**
	 * Adds a <bold>new string line<bold> at the end of the list, if you wish to
	 * concatenate data at the end of a string at an index
	 * 
	 * @see appendData()
	 * @param data a string containing the data to be added
	 */
	public void addnewLine(final String data) {
		this.data.add(data);
		return;
	}

	/**
	 * joins the given string to the last string of the list
	 * 
	 * @param data to add to the list
	 */
	public void appendData(final String data) {
		int loc = this.data.size() - 1;
		this.data.set(loc, this.data.get(loc) + data);
	}

	/**
	 * Joins the given string to the given index of the list
	 * 
	 * @param index to append the data to
	 * @param data  to append to an already existing string of the list
	 */
	public void appendData(final int index, final String data) {
		if (index > 0 && index < this.data.size())
			this.data.set(index, this.data.get(index) + data);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	protected final void buffersave() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String line : data) {
			writer.write(line + "\n");
		}
		writer.close();
	}

	@Override
	protected final void writersave() throws IOException {
		FileWriter writer = new FileWriter(file);
		for (String line : data) {
			writer.write(line + "\n");
		}
		writer.close();
	}

}
