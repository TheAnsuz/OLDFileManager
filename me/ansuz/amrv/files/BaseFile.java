package me.ansuz.amrv.files;

import java.io.File;
import java.io.IOException;

import me.ansuz.amrv.files.FileManager.FileErrorMessage;
import me.ansuz.amrv.files.FileManager.FileOperationMessage;

public abstract class BaseFile {

	public enum FileOperationResult {
		NOTHING, SAVED, CLEARED, IOERROR, BLOCKED, DONE
	}

	protected FileOperationResult result;
	protected final File file;
	protected Long seed = null;

	/**
	 * Creates a base for the file.
	 *
	 * <p>
	 * This must be extended for the file type.
	 *
	 * @param file to be created with
	 */
	protected BaseFile(File file) {
		this.file = file;
		this.result = FileOperationResult.NOTHING;
		this.reload();
	}

	/**
	 * Get the file as if it was a simple java file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Gets the full name of the file with the extension.
	 *
	 * @return the name of the file
	 */
	public String getName() {
		return file.getName();
	}

	/**
	 * Gets the short version of the file name, without the extension.
	 *
	 * @return the prefix of the file
	 */
	public String getShortName() {
		final String name = file.getName();
		if (name.contains("."))
			return name.substring(0, name.indexOf(".")).toLowerCase();
		else
			return file.getName();
	}

	/**
	 * Gets the extension or type of the file, if the extension is not found returns
	 * a empty string.
	 *
	 * @return the extension as lower case or a empty string if cant find the
	 *         extension
	 */
	public String getExtension() {
		final String name = file.getName();
		if (name.contains("."))
			return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
		else
			return "";
	}

	/**
	 * Changes the seed to encrypt the file, set it to null to remove the
	 * encriptation from the file
	 *
	 * @param seed to be used as encript method, or <code>null</code> to disable
	 */
	public final void setEncriptationSeed(final Long seed) {
		this.seed = seed;
	}

	/**
	 * Obtains the
	 * 
	 * @return
	 */
	public final Long getEncriptationSeed() {
		return seed;
	}

	/**
	 * Gets the absolute path from the file.
	 *
	 * @return a string containing the path
	 */
	public String getPath() {
		return file.getAbsolutePath();
	}

	/**
	 * Gets the relative path from the app root to the file (if is in a subfolder).
	 *
	 * @return the path relative to the app location
	 */
	public String getRelativePath() {
		return file.getPath();
	}

	/**
	 * Returns the size of the file.
	 *
	 * @return the length of the file in bits or 0L if the file does not exists ir
	 *         if the system cant get it
	 */
	public long size() {
		return file.length();
	}
	
	/**
	 * Checks if the file can be readed.
	 *
	 * @return true if read operations can be done
	 */
	public boolean canRead() {
		return file.canRead();
	}

	public void setReadable(final boolean readable) {
		file.setReadable(readable);
	}
	
	/**
	 * Checks if the file can be writen.
	 *
	 * @return true if write operations can be done
	 */
	public boolean canWrite() {
		return file.canWrite();
	}

	public void setWritable(final boolean writable) {
		file.setWritable(writable);
	}
	
	/**
	 * Checks if the file can be executed.
	 *
	 * @return true if the file can be executed by the program
	 */
	public boolean canExecute() {
		return file.canExecute();
	}

	public void setExecutable(final boolean executable) {
		file.setExecutable(executable);
	}
	
	/**
	 * Checks if the file is given with a absolute path, the marker is defined by
	 * the Operative system if a file is absolute, the starting point of the file
	 * will be the disk.
	 *
	 * @return true if the file path from <code>getPath()</code> is the same as
	 *         <code>getAbsolutePath()</code> meaning the file roots extend from the
	 *         disk
	 */
	public boolean isAbsolute() {
		return file.isAbsolute();
	}

	/**
	 * Checks if the file is indeed a file and not a directory and other
	 * system-based criteria.
	 *
	 * @return true if passes all the criteria to be a file
	 */
	public boolean isFile() {
		return file.isFile();
	}

	/**
	 * Checks if the file is a directory and not a file or other type of storage
	 * content.
	 *
	 * @return if the file is a folder
	 */
	public boolean isDirectory() {
		return file.isDirectory();
	}

	/**
	 * Checks if the file is hidden to the user, if the file cant be found or the
	 * security manager denies the access to this information then it will count as
	 * hidden.
	 *
	 * @return only returns false when there is access to the file so even the user
	 *         can manipulate, even if its not recommended
	 */
	public boolean isHidden() {
		try {
			return file.isHidden();
		} catch (SecurityException e) {
			return true;
		}
	}
	
	/**
	 * Checks for the last operation result of the file or NOTHING if no operation
	 * is done.
	 *
	 * <p>
	 * Althrough this returns the result of the last operation of the file its
	 * recommended to check the return value of those methods as they will grant a
	 * true value if completed or a false if not and will include the reason why it
	 * was unable to complete the operation.
	 *
	 * <p>
	 * However, you can use this just fine but its not the standard way to go.
	 *
	 * @return the result as a <code>FileOperationResult</code>
	 *
	 */
	@Deprecated
	public FileOperationResult getResult() {
		return result;
	}

	/**
	 * Reloads the contents of the file so they are loaded to the file memory in
	 * order to operate with them.
	 *
	 * <p>
	 * If the file is updated via external process you must call this method to be
	 * able to obtain the new contents of the file.
	 *
	 * @return true if the file was reloaded without erros, a false if something
	 *         went wrong
	 */
	public boolean reload() {
		if (this.canRead())
			return reloadProcess();
		else {
			FileManager.warn(FileOperationMessage.CANT_READ, file.getName());
			return false;
		}
	}

	/**
	 * Starts the sub load process for the file, althrough this is only for
	 * convenience, use <code>reload()</code> instead as will check for permissions
	 * to operate the file and will also use logs
	 * 
	 * @return
	 */
	protected abstract boolean reloadProcess();

	/**
	 * Clears the contents of the file that are allocated on the memory, but if the
	 * file is never written after this call, it will never override its contents.
	 *
	 * <p>
	 * If you want to remove the file with the contents, use <code>delete()</code>
	 */
	public abstract void clear();

	/**
	 * Saves the data from the filetype to the file, how the data is saved,
	 * independently of the buffer capability and whenever to use it or not depends
	 * on the subclass that inherits the saving with buffers.
	 *
	 * <p>
	 * This method involves the use of the buffer.
	 *
	 * <p>
	 * Some files may not have such a big difference and some other will execute a
	 * save with or without buffer even if you choose not just because its the
	 * recomended operation in both cases.
	 *
	 * @return true if the file was saved, false otherwise
	 */
	public final boolean save() {
		return this.save(true);
	}

	/**
	 * Saves the data from the filetype to the file, how the data is saved,
	 * independently of the buffer capability and whenever to use it or not depends
	 * on the subclass that inherits the saving with and without buffers.
	 *
	 * @param buffered if the save method should use buffers
	 *
	 * @return true if the file was saved, false otherwise
	 */
	public final boolean save(final boolean buffered) {
		if (!this.canWrite()) {
			this.result = FileOperationResult.BLOCKED;
			FileManager.warn(FileOperationMessage.CANT_SAVE, file.getName());
			return false;
		}
		try {
			if (buffered)
				this.buffersave();
			else
				this.writersave();
			this.result = FileOperationResult.SAVED;
			return true;
		} catch (IOException e) {
			this.result = FileOperationResult.IOERROR;
			FileManager.error(FileErrorMessage.SAVING, file.getName());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Triggers the delete process for the file right now, however, you will still
	 * be able to operate with the data that was already loaded but using the
	 * <code>reload()</code> or <code>save()</code> will result in an error.
	 *
	 * <p>
	 * This method assumes that you want to delete the file in this exact moment.
	 *
	 * <p>
	 * Its not recomenended to manage files that you wish to remove except you know
	 * what you are doing but you might also know that you can create temporal files
	 * with
	 * 
	 * <pre>
	 * File.createTemporalFile()
	 * </pre>
	 *
	 * @return true if the file was able to be deleted
	 */
	public final boolean delete() {
		return this.delete(false);
	}

	/**
	 * Triggers the delete process for the file right now or when the program ends,
	 * however, you will still be able to operate with the data that was already
	 * loaded but using the <code>reload()</code> or <code>save()</code> will result
	 * in an error.
	 *
	 * <p>
	 * Its not recomenended to manage files that you wish to remove except you know
	 * what you are doing but you might also know that you can create temporal files
	 * with
	 * 
	 * <pre>
	 * File.createTemporalFile()
	 * </pre>
	 *
	 * @param onExit if the delete process should be triggered after the virtual
	 *               machine ends all the process
	 *
	 * @return true if the file was deleted or was succesfully queued to be deleted
	 *         on the exit
	 */
	public final boolean delete(final boolean onExit) {
		try {
			if (onExit)
				file.deleteOnExit();
			else
				file.delete();
			this.result = FileOperationResult.DONE;
			return true;
		} catch (Exception e) {
			this.result = FileOperationResult.IOERROR;
			FileManager.error(FileErrorMessage.DELETING, file.getName());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Writing method that will be called automatically when a save without buffer
	 * is triggered
	 *
	 * This method sould not be called directly as using <code>save(true)</code>
	 * will grant the option to use this save
	 *
	 * @throws IOException if any problem occurs during the writing of the file
	 */
	protected abstract void buffersave() throws IOException;

	/**
	 * Writing method that will be called automatically when a save without buffer
	 * is triggered
	 *
	 * This method sould not be called directly as using <code>save(true)</code>
	 * will grant the option to use this save
	 *
	 * @throws IOException if any problem occurs during the writing of the file
	 */
	protected abstract void writersave() throws IOException;

}
