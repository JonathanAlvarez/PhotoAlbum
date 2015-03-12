package cs213.photoAlbum.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A user is a data storage class which contains a unique user ID, the user's name,
 * an arraylist of the user's albums, and a hashmap used for sorting. The user class
 * has the functionality to add, delete, and rename albums.
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class User implements Serializable{

	public String uniqueID;
	public String fullName;
	public ArrayList<Album> userAlbum;
	public HashMap<String, Photo> allPhotos; //all photos in all albums, useful for sort by date and tag
	
	public User(String uniqueID, String fullName){ // Creates a user, or loads user from file
		this.uniqueID = uniqueID;
		this.fullName = fullName;
		userAlbum = new ArrayList<Album>();
		userAlbum.clear();
		allPhotos = new HashMap<String, Photo>();
	}
	
	/**
	 * Adds an album to the user.
	 * @param albumName album object to be stored into a user's album list.
	 * @return false if the album is already present
	 */
	public boolean addAlbum(Album albumName){
		if (userAlbum.contains(albumName))
			return false;
		
		userAlbum.add(albumName);
		return true;
	}
	
	/**
	 * Removes an album from the user.
	 * @param albumName album object to be removed from the user's album list.
	 * @return false if the album was already removed
	 */
	public boolean deleteAlbum(Album albumName){
		if (userAlbum.contains(albumName)) {
			ArrayList<Photo> photolist = albumName.getPhotoList();
			for (int i = 0; i < photolist.size(); i++){
				Photo p = photolist.get(i);
				p.removeFromAlbum(albumName.albumName);
				if (p.albums.isEmpty()){
					allPhotos.remove(p.filename);
				}
			}
			userAlbum.remove(albumName);
			return true;
		}
			return false;
	}
	
	/**
	 * Renames a specified album.
	 * @param newName new name of the album
	 * @param album album to be renamed
	 * @return false if the album name remained unchanged
	 */
	public boolean renameAlbum(String newName, Album album) {
		album.albumName = newName;
		if (album.albumName.equals(newName))
			return true;
		return false;
	}
	
	/**
	 * Adds a picture to the hash table if it's not already there
	 * @param picture picture to be added
	 * @return false if the picture was not added
	 */
	public boolean addToAllPhotos(Photo picture){ //Adds the picture to the hash table if its not already there
		allPhotos.put(picture.filename, picture);
		if(allPhotos.containsValue(picture))
			return true;
		return false;
	}
	
	/**
	 * Returns the list of albums in this user
	 * @return An album array of albums contained in the user
	 */
	public Album[] albumList(){
		Album[] albumList = new Album[userAlbum.size()];
		
		for(int i = 0; i < userAlbum.size(); i++) {
			albumList[i] = userAlbum.get(i);
		}
		
		return albumList;
	}
	
	/**
	 * writes the user to file in data/'userID'.bin
	 */
	public void writeUser(){
		OutputStream file, buffer;
		ObjectOutput output = null;
		try{
			file = new FileOutputStream("data/" + uniqueID + ".bin");
			buffer = new BufferedOutputStream(file);
			output = new ObjectOutputStream(buffer);
		}catch(Exception e){
			System.err.println("Caught IOException: " + e.getMessage());
		}
			try{
				output.writeObject(this);
			} //finally { output.close(); }
			catch (IOException e) {
				System.err.println("Caught IOException: " + e.getMessage());
			}
			
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	/**
	 * Reads a user from the database.
	 * @param userID uniqueID of the user to be read into
	 * @return the user that was read from the database corresponding to the uniqueID.
	 */
	public User readUser(String userID) throws IOException{
		ObjectInputStream objectIn = null;
		User user = null; 
		
	    try {
			objectIn = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream("data/" + userID + ".bin")));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to read in user.bin");
		}
    	try {
			user = (User) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to read object from user.bin");
		} catch (IOException e) {
			System.out.println("Failed to read object from user.bin");
		}
    
	    objectIn.close();
	    return user;
	}
}
