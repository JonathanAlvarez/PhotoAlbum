package cs213.photoAlbum.control;

import cs213.photoAlbum.model.Album;

/**
 * Interface for DefaultControl.java
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public interface ControlInterface {

	/**
	 * Lists the users
	 * @return An array of user names
	 */
	String[] listUsers();
	
	/**
	 * Adds an album for the current user
	 * @param albumName Name of the new album
	 * @return true if successful
	 */
	boolean addAlbum(String albumName);
	
	/**
	 * Removes an album for the current user
	 * @param albumName Name of the album to be removed
	 * @return true if successful
	 */
	boolean removeAlbum(String albumName);
	
	/**
	* Lists the albums
	* @return An array of album names
	*/
	public Album[] listAlbums();
	
	/**
	 * adds a user to the register
	 * @param userID
	 * @param userName
	 * @return Returns false if the user is already registered
	 */
	boolean addUser(String userID, String userName);

	/**
	 * removes a user from the register
	 * @param userID
	 * @return Returns false if the user is not registered
	 */
	boolean removeUser(String userID);
	
	/**
	 * Sets the user for the session
	 * @param userID
	 */
	void setUser(String userID);
	
	/**
	 * Gets an array of all the photo names in an album belonging to the user
	 * @param albumName String containing the name of the album
	 * @return Returns a String[] containing the names of photographs
	 */
	String[] listPhotos(String albumName);
	
	/**
	 * Adds a photograph to an album belonging to the user
	 * @param filename Location of the photo on disk
	 * @param caption Caption which should be attributed to the photo
	 * @param albumName Album to which the photo should be added
	 * @return Returns false if the filename is invalid
	 */
	boolean addPhoto(String filename, String caption, String albumName);
	/**
	 * Moves a photograph from one album to another
	 * @param filename Location of the photo on disk
	 * @param oldAlbum Album where the photo currently exists
	 * @param newAlbum Album to where the photo should be moved
	 * @return Returns false if the photograph does not exist in the album
	 */
	boolean movePhoto(String filename, String oldAlbum, String newAlbum);
	
	/**
	 * Removes a photograph from an album
	 * @param filename Location of the photo on disk
	 * @param albumName Album where the photo resides
	 * @return Returns false if the photo does not exist in the album
	 */
	boolean removePhoto(String filename, String albumName);
	
	/**
	 * Adds an identifying tag to the specified photo
	 * @param filename Location of the photo on disk
	 * @param tagType Type of the tag. 'Location', or 'Person', etc.
	 * @param TagValue Value of the tag. 'Vermont', or 'Bob', etc.
	 * @return Returns false if the photo does not exist
	 */
	boolean addTag(String filename, String tagType, String TagValue);
	
	/**
	 * Removes a tag with {tagType} and {tagValue} from the photograph
	 * @param filename Location of the photo on disk
	 * @param tagType Type of the tag. 'Location', or 'Person', etc.
	 * @param TagValue TagValue Value of the tag. 'Vermont', or 'Bob', etc.
	 * @return Returns false if the tag does not exist on the photo
	 */
	boolean removeTag(String filename, String tagType, String TagValue);
	
	/**
	 * Returns an array containing data about the photograph including name, album[s], date and tag[s]
	 * @param filename Location of the photo on disk
	 * @return Returns false if the photograph does not exist
	 */
	String[] listPhotoInfo(String filename);
	
	/**
	 * Lists all photographs taken between the start and end dates, sorted chronologically
	 * @param startDate Beginning of the range of dates
	 * @param endDate End of the range of dates
	 * @return A String array of the names of photographs
	 */
	String[] listByDate(String startDate, String endDate);
	
	/**
	 * Lists all photographs with the listed tags, sorted chronologically
	 * @param tagArray Array of strings in the format of "tagType:tagValue", or ":tagValue" if tagType is not specified
	 * @return A String array of the names of photographs
	 */
	String[] listByTag(String[] tagArray);
	
	/**
	 * Saves the current state of the program
	 */
	public void saveData();
}
