package db;

import entity.Image;

import java.util.List;

public interface DBConnection {
	/**
	 * Close connection
	 */
	public void close();
	
	/**
	 * Save Image into db
	 * 
	 * @param Image
	 */
	public boolean saveImage(Image image);
	
	/**
	 * Search images after the time that owner set
	 * starting watch
	 * 
	 * @param startTime
	 * @return list of images
	 */
	public List<Image> searchImages(String camera_id, String startTime);
	
}
