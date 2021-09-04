package me.ansuz.amrv.files;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import me.ansuz.amrv.files.FileManager.FileErrorMessage;
import me.ansuz.amrv.files.FileManager.FileOperationMessage;

public final class FileConstructor {

	private final File file;

	/**
	 * Creates a file in a way to guarantee that most of the errors that IO will
	 * throw are never invoked, even if there can be errors anyway, this will make
	 * manipulating files much more easy and faster.
	 *
	 * <p>
	 * If the file starts with a directory separator it will be created at that
	 * absolute location, recomended to use
	 * <code>FileManager.setDefaultLocation</code> to specify a root location and
	 * then <code>FileManager.construct(filename, true)</code> to use that location
	 *
	 * @param file the file to be used for the constructor
	 */
	protected FileConstructor(File file) {
		// Warns if the file is being created at the root folder of the disk instead of
		// the project folder
		if (file.getPath().startsWith(File.separator) && FileManager.isDebugEnabled()) {
			System.out.println(FileManager.WARNING + "File started with " + File.separator
					+ ", file will be located on: \n" + file.getAbsolutePath() + "\nRemove " + File.separator
					+ " to put the file inside the project folder");
		}

		// Creates every directory to reach the file in case that the path to the file
		// does not exists
		String root = file.getAbsolutePath().replace(file.getName(), "");
		File temp = new File(root);
		if (!temp.isDirectory()) {
			temp.mkdirs();
			FileManager.warn(FileOperationMessage.FOLDER_CREATED, file.getName());
		}

		// This section checks if the file exists and is indeed a file, if is not, then
		// will check for the creator to create it
		if (!file.isFile() || !file.exists())
			try {
				file.createNewFile();
				FileManager.warn(FileOperationMessage.FILE_CREATED, file.getName());

			} catch (IOException e) {
				FileManager.error(FileErrorMessage.CREATING, file.getName());
				e.printStackTrace();
			}

		// Last check for the file if exists or not in case something weird happened
		if (!file.exists())
			System.err.println(FileManager.ERROR + "\"" + file.getName() + "\""
					+ " does not exist and the creator is not operative, "
					+ " the process will continue but there is no guarantee it will work propertly ");
		this.file = file;
	}

	/**
	 * Instantiates the object as a new raw java File.
	 *
	 * @see File
	 *
	 * @return a new instance of a file already constructed
	 */
	public File toFile() {
		return file;
	}

	/**
	 * Instantiates the object as a new PlainFile.
	 *
	 * <p>
	 * Plain files are good for very small files of text as the whole file is
	 * representated as a String, do not use for large files, consider using it only
	 * for files smaller than 5000 characters as it may cause performance issues
	 *
	 * @see String
	 *
	 * @return a new instance of a file already constructed
	 */
	public PlainFile toPlainFile() {
		return new PlainFile(file);
	}

	/**
	 * Instantiates the object as a new ListFile.
	 *
	 * <p>
	 * List files are just like multiple plain files, obtaining the data from the
	 * file in multiple strings, useful for large files.
	 *
	 * @see List
	 * @see ArrayList
	 *
	 * @return a new instance of a file already constructed
	 */
	public ListFile toListFile() {
		return new ListFile(file);
	}

	/**
	 * Instantiates the object as a new MapFile.
	 *
	 * <p>
	 * Map files are a built in file data format for configuration and variable
	 * saving but the data <b>should not</b> have whitespaces as this can cause
	 * erros, so a namespace format is recommended, this file is managed as a Map of
	 * strings.
	 *
	 * @see Map
	 * @see Entry
	 *
	 * @return a new instance of a file already constructed
	 */
	public MapFile toMapFile() {
		return new MapFile(file);
	}

	/**
	 * Instantiates the object as a new ImageFile.
	 *
	 * <p>
	 * Image files are used exclusively for image files, recommended to use
	 * <b>JPG</b> or <b>PNG</b> file formats.
	 *
	 * <p>
	 * <i>WARNING</i> This file format is guaranteed to be able to save and read any
	 * file provided by this same source as if the file format is not valid, it will
	 * save the image to that file anyways, being able to read it whenever it needs
	 * but other programs may not be able to operate those files
	 *
	 * @see Image
	 * @see ImageIO
	 * @see BufferedImage
	 *
	 * @return a new instance of a file already constructed
	 */
	public ImageFile toImageFile() {
		return new ImageFile(file);
	}
	
	/**
	 * Instantiates the object as a new AudioFile
	 * 
	 * <p>
	 * Audio files can hold audio, even if the AudioFile itself does not provide options
	 * to modify the audio itself, it grants you the Audio file format and stream to 
	 * modify at your will
	 * 
	 * <p>
	 * <i>WARNING</i> Audio files must be valid types, they cant be any file with any
	 * extension and just manipulate it
	 * 
	 * @see AudioFileFormat
	 * @see AudioInputStream
	 * @return
	 */
/*	public AudioFile toAudioFile() {
		return new AudioFile(file);
	}
*/
}
