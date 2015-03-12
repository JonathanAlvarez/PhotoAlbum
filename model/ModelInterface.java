package cs213.photoAlbum.model;

import java.io.IOException;

/**
 * The ModelInterface is used to obtain information pertaining to users from files.
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public interface ModelInterface {
	
	/**
	 * reads in a user from file
	 * @param userID User to be loaded into the program
	 */
	public void readUser(String userID) throws IOException;
	
	/**
	 * creates a new user, as long as the name is not taken, and saves the user to a file
	 * @param userID ID of the user to be created
	 * @param Username Name of the user to be added
	 * @return true if successful
	 */
	public boolean createUser(String userID, String Username);
	
	/**
	 * deletes a user from database if the user is registered, removes from the list of users
	 * @param userID ID of the user to be deleted
	 */
	public boolean deleteUser(String userID);
	
	/**
	 * reads in the current user list from the database.
	 */
	public void readUserList() throws IOException;
	
	/**
	 * returns user names
	 * @return String[] containing all user IDs
	 */
	public String[] userList();
	
	/**
	 * Saves the current state of the program, including user list, and current user to file.
	 */
	public void saveState();
	
	/**
	 * determines if the user has such a picture in any album
	 * @param filename name of the picture
	 * @return true if picture exists
	 */
	public boolean userHasPicture(String filename);
	
	/**
	 * determines if the user has such an album
	 * @param albumname name of the album
	 * @return true if album exists
	 */
	public boolean userHasAlbum(String albumname);
	
	/**
	 * returns the picture object
	 * @param filename name of the picture
	 * @return Picture object with 'filename'
	 */
	public Photo getPhoto(String filename);
	
	/**
	 * returns the album with the given name, if it exists. 
	 * @param albumname name of the album
	 * @return returns the album if it exists, null otherwise.
	 */
	public Album getAlbum(String albumname);
	
	/**
	 * adds a photo to an album
	 * @param filename name of the photo
	 * @param albumname name of the album
	 * @param caption the text associated with the photo
	 * @return true if successful
	 */
	public boolean addPhoto(String filename, String caption, String albumname);
	
	/**
	 * removes a photo from the album
	 * @param filename name of the photo
	 * @param albumname name of the album
	 * @return true if successful
	 */
	public boolean removePhoto(String filename, String albumname);
		
	/**
	 * returns all photos belonging to the user
	 * @return An array of photos belonging the the user
	 */
	public Photo[] getAllPhotos(); 
	
	/**
	 * Adds an album for the current user
	 * @param albumName Name of the new album
	 * @return true if successful
	 */
	public boolean addAlbum(String albumName);
	
	/**
	 * Removes an album from the current user
	 * @param albumName Name of the album to be deleted
	 * @return true if successful
	 */
	public boolean removeAlbum(String albumName);
	
	/**
	 * returns the names of all albums belonging to the user
	 * @return array of album names
	 */
	public Album[] getAllAlbums();
	
}
