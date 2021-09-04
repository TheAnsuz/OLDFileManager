package me.ansuz.amrv.files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.ansuz.amrv.files.FileManager.FileErrorMessage;
import me.ansuz.amrv.files.FileManager.FileOperationMessage;

public class ImageFile extends BaseFile {

	private BufferedImage img;
	private String extension;

	protected ImageFile(File file) {
		super(file);
	}

	@Override
	protected boolean reloadProcess() {
		try {
			img = ImageIO.read(file);
			if (this.isValidFormat())
				extension = this.getExtension();
			else
				extension = "png";

			if (img == null) {
				img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
				FileManager.warn(FileOperationMessage.CANT_READ, file.getName());
			}
			return true;
		} catch (IOException x) {
			FileManager.error(FileErrorMessage.READING, file.getName());
			x.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if the extension of the file is calable of handle image files or not,
	 * remember that this file will be able to read and write files that contain
	 * images even if the format is not valid
	 *
	 * @return true if the file format is supported for at least one type of image
	 *         encoding
	 */
	public boolean isValidFormat() {
		if (ImageIO.getImageReadersBySuffix(this.getExtension()).hasNext())
			if (ImageIO.getImageWritersBySuffix(this.getExtension()).hasNext())
				return true;
		return false;
	}

	/**
	 * Changes the encoding to be used as the default for the file, however if is
	 * not valid, it will use a default png image encoding but the program will not
	 * try to check if is valid or not. Remember this has nothing to do with the
	 * file sufix, in fact, the sufix can be empty and you can still select to save
	 * the file as a JPG image
	 *
	 * @param encoding type, allowed formats are given by the <code>ImageIO</code>,
	 *                 at the time this was written, supported encodings by default
	 *                 are <b>TIF JPG TIFF JPEG PNG WBMP BMP GIF</b> (remember more
	 *                 ones can be added with java plugins)
	 *
	 * @see ImageIO
	 */
	public void setSaveEncoding(String encoding) {
		this.extension = encoding.toLowerCase();
	}

	/**
	 * Gets the encoding that was set for the file, by default it will try to use a
	 * valid one but you can change it to the one you want even if it can cause some
	 * encoding/decoding erros, however, if you set as encoding a invalid one, the
	 * manager wont change it but at the time of saving the file, it will use the
	 * default one (PNG)
	 *
	 * @param encoding a string containing what encoding should be used
	 *
	 * @return a string containing the value (will always be on lowercase)
	 */
	public String getSaveEncoding(String encoding) {
		return extension;
	}

	/**
	 * Gets the buffered image that the file contains, if you manipulated the format
	 * of the file or using one that is not handled by ImageIO it can cause some
	 * problems with pixels and alpha values at the time of saving to different
	 * formats
	 *
	 * @return a BufferedImage with the whole image from the file or a blank if the
	 *         file couldnt retrieve the image
	 *
	 * @see ImageIO
	 */
	public BufferedImage getImage() {
		return img;
	}

	/**
	 * Sets the stored image on the file as a new one, this will override completly
	 * the old image
	 *
	 * @param image the buffered image to be set
	 */
	public void setImage(BufferedImage image) {
		img = image;
	}

	/**
	 * Obtains the height of the image in pixels
	 *
	 * @return the height, in pixels
	 */
	public int getHeight() {
		return img.getHeight();
	}

	/**
	 * Obtains the width of the image in pixels
	 *
	 * @return the width, in pixels
	 */
	public int getWidth() {
		return img.getWidth();
	}

	/**
	 * Chacks if the file has an alpha set on the color model
	 *
	 * @return true only if the Color model of the image accepts alpha values in
	 *         pixels
	 */
	public boolean hasAlpha() {
		return img.getColorModel().hasAlpha();
	}

	/**
	 * Will clear the image among all the memory and buffer used, this will make the
	 * image just a 1x1 pixels size image without color
	 *
	 */
	@Override
	public void clear() {
		img.flush();
		img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	protected void buffersave() throws IOException {
		final boolean useCache = ImageIO.getUseCache();
		ImageIO.setUseCache(true);
		if (!ImageIO.write(img, extension, file))
			ImageIO.write(img, "png", file);
		ImageIO.setUseCache(useCache);
	}

	@Override
	protected void writersave() throws IOException {
		final boolean useCache = ImageIO.getUseCache();
		ImageIO.setUseCache(false);
		if (!ImageIO.write(img, extension, file))
			ImageIO.write(img, "png", file);
		ImageIO.setUseCache(useCache);
	}

}
