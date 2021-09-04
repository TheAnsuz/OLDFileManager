package me.ansuz.amrv.files;

import java.io.File;

/**
 * This class handles file operations and adds the option to retrieve, write and
 * save data in multiple formats
 * 
 * @author Ansuz
 *
 * @version 2.1
 */
public class FileManager {

	// Default location to create new files
	private static String root = new File("").getAbsolutePath();
	private static boolean debug = true;
	protected static final String WARNING = "[WARNING] ";
	protected static final String ERROR = "[ERROR] ";

	protected enum FileOperationMessage {
		CANT_SAVE, CANT_READ, CANT_EXEC, CANT_DEL, FOLDER_CREATED, FILE_CREATED, OTHER
	}

	protected enum FileErrorMessage {
		SAVING, CREATING, DELETING, READING, EXECUTING, OTHER
	}

	protected static void warn(FileOperationMessage msg, String filename) {
		if (!debug)
			return;
		String message = "";
		switch (msg) {
		case CANT_SAVE:
			message = "File \"" + filename + "\" cant be saved";
			break;
		case CANT_DEL:
			message = "File \"" + filename + "\" cant be deleted";
			break;
		case CANT_EXEC:
			message = "File \"" + filename + "\" cant be executed";
			break;
		case CANT_READ:
			message = "File \"" + filename + "\" cant be readed";
			break;
		case FOLDER_CREATED:
			message = "Needed to create folders to reach the file \"" + filename + "\"";
			break;
		case FILE_CREATED:
			message = "Needed to create \"" + filename + "\" as it didnt exist at least on the given path";
			break;
		default:
			message = "File \"" + filename + "\" cant be manipulated";
			break;
		}
		System.out.println(WARNING + message);
	}

	protected static void error(FileErrorMessage msg, String filename) {
		if (!debug)
			return;
		String message = "";
		switch (msg) {
		case DELETING:
			message = "There was an error deleting file \"" + filename + "\"";
			break;
		case READING:
			message = "There was an error reading file \"" + filename + "\"";
			break;
		case SAVING:
			message = "There was an error saving file \"" + filename + "\"";
			break;
		case EXECUTING:
			message = "There was an error executing file \"" + filename + "\"";
			break;
		case CREATING:
			message = "There was an error creating file \"" + filename + "\"";
		default:
			message = "File \"" + filename + "\" cant be manipulated";
			break;
		}
		System.out.println(ERROR + message);
	}

	/**
	 * Sets the debug mode to be enabled or not, the debug mode just prints the
	 * errors and warnings from the files althrough the manager will still try to
	 * solve those erros, the debug mode is enabled by default.
	 *
	 * <p>
	 * This does not affect warning or errors that are hardcoded to be thrown in
	 * case something really bad happens
	 *
	 * @param enabled if the debug mode should be enabled or not
	 */
	public static void setDebugEnabled(boolean enabled) {
		debug = enabled;
	}

	/**
	 * Obtains the state of the debug mode, wich if enabled will show errors and
	 * warnings from the manager and each file, howver there are severe errors wich
	 * are hardcoded to be thrown anyways.
	 *
	 * <p>
	 * By default debug mode is enabled
	 *
	 * @return true if the debug log is enabled, false otherwise
	 */
	public static boolean isDebugEnabled() {
		return debug;
	}

	/**
	 * Sets the default path from the disk to the next files.
	 * <p>
	 * However, this will only be used if specified at
	 * <code>construct(String fileName, boolean useDefaultLocation)</code> method.
	 *
	 * @param path to be taken as the new root for the files
	 */
	public static void setDefaultLocation(final String path) {
		root = path;
	}

	/**
	 * Gets the location from the disk specified at
	 * <code>setDefaultLocation(String path)</code>, if the method has not yet been
	 * called then it will return the default given, wich is the same as the path
	 * from the disk where this program is located to the program, or at least, it
	 * should be, except something modified it.
	 *
	 * @return the path that is being used as root
	 */
	public static String getDefaultLocation() {
		return root;
	}

	/**
	 * Creates a safe file with the given name or path, this does not guarantee a
	 * error-free environment but makes it easier to manage files with the given
	 * resources, making IO easier and simpler.
	 *
	 * <p>
	 * If you give a name or path pointing to a file that already exists, this will
	 * obtain that file, if the file or the directories to reach the file do not
	 * exists, this will create them with a empty file if possible.
	 *
	 * @param fileName name or path of the file to be used to obtain or create the
	 *                 file
	 *
	 * @return the FileConstructor for the given file
	 */
	public static FileConstructor construct(String fileName) {
		return FileManager.construct(new File(fileName));
	}

	/**
	 * Creates a safe file with the given name or path, but also including the
	 * option to use a specified root path, set by the
	 * <code>setDefaultLocation()</code> that can be modified so the manager will
	 * execute at the given location, however, this safe file does not guarantee a
	 * error-free environment but reduces most of the errors caused by IO files,
	 * using the default location can cause some errors if you gave a wrong or
	 * system-dependent path
	 *
	 * If you give a name or path pointing to a file that already exists, this will
	 * obtain that file, if the file or the directories to reach the file do not
	 * exists, this will create them with a empty file if possible
	 *
	 * @param fileName name or path of the file to be used to obtain or create the
	 *                 file
	 *
	 * @return the FileConstructor for the given file
	 */
	public static FileConstructor construct(String fileName, boolean useDefaultLocation) {
		if (useDefaultLocation)
			return FileManager.construct(new File(root + File.separator + fileName));
		else
			return FileManager.construct(new File(fileName));
	}

	/**
	 * Creates a safe file with the given file, this does not guarantee a error-free
	 * environment but makes it easier to manage files with the given resources,
	 * making IO easier and simpler
	 *
	 * If you give a file that already exists, this will obtain that file, if the
	 * file or the directories to reach the file do not exists, this will create
	 * them with a empty file if possible
	 *
	 * @param fileName name or path of the file to be used to obtain or create the
	 *                 file
	 *
	 * @return the FileConstructor for the given file
	 */
	public static FileConstructor construct(File file) {
		FileConstructor constructor = new FileConstructor(file);
		return constructor;
	}

	/*
	 * TODO: create a method to create files on directory create a method to create
	 * directories on directory
	 */

}