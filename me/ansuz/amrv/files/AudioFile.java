package me.ansuz.amrv.files;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFile extends BaseFile{

	private AudioFileFormat audio;
	private AudioInputStream stream;
	private AudioFileFormat.Type type;
	@SuppressWarnings("unused")
	private AudioFormat format;
	
	protected AudioFile(File file) {
		super(file);
		
	}

	@Override
	protected boolean reloadProcess() {
		try {
			audio = AudioSystem.getAudioFileFormat(file);
			type = audio.getType();
			stream = AudioSystem.getAudioInputStream(file);
			return true;
		} catch (UnsupportedAudioFileException | IOException x) {
			if (FileManager.isDebugEnabled())
				System.err.println(
						FileManager.WARNING + "file " + file.getName() + " got an error while reading the file");
			x.printStackTrace();
			return false;
		}
		
	}

	/**
	 * Obtains the number of frames of the audio stream
	 * 
	 * @return the length in frames of the file
	 */
	public Long getLength() {
		return stream.getFrameLength();
	}
	
	/**
	 * Obtains the type of the file
	 * 
	 * @see AudioFileFormat.Type
	 * 
	 * @return the type of the file
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Sets the type of the file
	 * 
	 * @see AudioFileFormat.Type
	 * 
	 * @param type to as replacement for the old type
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Obtains the format of the audio input stream from the file
	 * the format cant be changed from the stream unless you create a 
	 * new one with that format
	 * 
	 * @return the AudioFormat of the file
	 */
	public AudioFormat getFormat() {
		return this.stream.getFormat();
	}
	
	/**
	 * Replaces the audio that will be written onto the file when you save it
	 * 
	 * @param stream the AudioInputStream to be replaced with
	 */
	public void setAudio(AudioInputStream stream) {
		this.stream = stream;
		this.format = stream.getFormat();
	}
	
	/**
	 * Obtains the AudioInputStream that is currently holding the file, if
	 * there was an error obtaining it this will return a null value
	 * 
	 * @return the audio as an AudioInputStream
	 */
	public AudioInputStream getAudio() {
		return stream;
	}
	
	@Override
	protected void buffersave() throws IOException {
		AudioSystem.write(stream, type, file);
	}

	@Override
	protected void writersave() throws IOException {
		AudioSystem.write(stream, AudioFileFormat.Type.AU, file);
	}

	@Override
	public void clear() {
		audio.properties().clear();
		try {
			stream.close();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

}
