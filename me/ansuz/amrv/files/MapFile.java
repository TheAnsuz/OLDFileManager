package me.ansuz.amrv.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A file handler provided by the file manager for easier file operations, this
 * one is prefered to be use for configuration files as the file is readed and
 * written as if it was a map
 * 
 * @author Ansuz
 *
 */
public class MapFile extends BaseFile {

	private Map<String, String> values = new HashMap<>();

	/**
	 * Constructor of the map file, you must use
	 * 
	 * <pre>
	 * FileManager.getMapFile()
	 * </pre>
	 * 
	 * instead
	 * 
	 * @param file
	 */
	protected MapFile(File file) {
		super(file);
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
				this.parse(reader.readLine());
			}
			reader.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
	}

	private final void parse(String string) {
		if (values == null)
			values = new HashMap<String,String>();
		if (string.startsWith("#") || string.startsWith("//") || !string.contains("="))
			return;
		string = string.trim();
		final int index = string.indexOf("=");
		values.put(string.substring(0, index), string.substring(index));
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
	 * @return a string containing the value of the map, if the map does not contain
	 *         the field, this will return an empty string
	 * @param field the key of the map
	 */
	public String get(final String field) {
		if (!values.isEmpty() && values.containsKey(field))
			return values.get(field);
		else
			return "";
	}

	/**
	 * Checks if the map contains the specified key
	 * 
	 * @param key to be checked
	 * 
	 * @return true if the map is not empty and contains that exact key
	 */
	public boolean containsKey(String key) {
		if (values.isEmpty()) return false;
		return values.containsKey(key);
	}
	
	/**
	 * Checks if the map contains the specified value inside any of its fields
	 * 
	 * @param value to be checked
	 * 
	 * @return true if the map is not empty and contains that exact value in any key
	 */
	public boolean containsValue(String value) {
		if (values.isEmpty()) return false;
		return values.containsValue(value);
	}
	
	/**
	 * Changes the data from one field of the map, if the map contains it, this will
	 * override it, if it doesnt, it will just add it
	 * 
	 * @param field of the map that holds that information
	 * @param data  inside the field of the map
	 */
	public void put(final String field, final String data) {
		values.put(field.trim(), data);
	}

	/**
	 * @return retrieves the set of Strings as if it was from a map
	 */
	public Set<Entry<String, String>> getEntrySet() {
		return values.entrySet();
	}

	/**
	 * Clears the whole information stored, if you dont save after this, the file
	 * will not be cleared, this changes just the information that is being holded
	 */
	@Override
	public void clear() {
		values.clear();
	}

	@Override
	protected final void buffersave() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (Entry<String, String> e : values.entrySet()) {
			writer.write(e.getKey() + " = " + e.getValue() + "\n");
		}
		writer.close();
	}

	@Override
	protected final void writersave() throws IOException {
		FileWriter writer = new FileWriter(file);
		for (Entry<String, String> e : values.entrySet()) {
			writer.write(e.getKey() + " = " + e.getValue() + "\n");
		}
		writer.close();
	}

}
