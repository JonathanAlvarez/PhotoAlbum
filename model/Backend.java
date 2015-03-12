package cs213.photoAlbum.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The backend refers to functionality that will allow for the storage and 
 * retrieval from the directories data and photos. It's functions mainly deal 
 * with user manipulation.
 * 
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class Backend implements ModelInterface {
	
	public User user;
	UserList userList;
	
	public Backend() throws IOException{
		readUserList();
	}
	
	public Backend(String userID) throws IOException{
		readUserList();
		if (!userList.userPresent(userID)){
			throw new IOException("user " + userID + " does not exist");
		}
		readUser(userID);
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#readUser(java.lang.String)
	 */
	@Override
	public void readUser(String userID) throws IOException {
		ObjectInputStream objectIn = null;
		user = null; 
		
	    try {
			objectIn = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream("data/" + userID + ".bin")));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to read in user.bin");
			System.exit(0);
		}
    	try {
			user = (User) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to read object from user.bin*");
		} catch (IOException e) {
			System.out.println("Failed to read object from user.bin**");
		}
    
	    objectIn.close();
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#createUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createUser(String userID, String Username) {
		if (userList.userPresent(userID)){
			System.out.println("user " + userID +" already exists with name " + Username);
			return false;
		}
		user = new User(userID, Username);
		user.writeUser();
		userList.addUser(userID);
		userList.writeUserList();
		return true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#deleteUser(java.lang.String)
	 */
	@Override
	public boolean deleteUser(String userID) {
		if (!userList.userPresent(userID)){
			System.out.println("user " + userID + " does not exist");
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "User " + userID + " does not exist.");
			return false;
		}
		File f = new File("data/" + userID + ".bin");
		f.delete();
		userList.removeUser(userID);
		userList.writeUserList();
		return true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#readUserList()
	 */
	@Override
	public void readUserList() throws IOException {
		userList = UserList.loadUserList();
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#userList()
	 */
	@Override
	public String[] userList() {
		String[] ret = userList.getIDs();
		if (ret.length == 0)
			return null;
		else
			return ret;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#saveState()
	 */
	@Override
	public void saveState() {
		user.writeUser();
		System.out.println("user saved");
		userList.writeUserList();

	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#userHasPicture(java.lang.String)
	 */
	@Override
	public boolean userHasPicture(String filename) {
		HashMap<String, Photo> allpics = user.allPhotos;
		if (allpics.containsKey(filename))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#userHasAlbum(java.lang.String)
	 */
	@Override
	public boolean userHasAlbum(String albumname) {
		Album[] albums = user.albumList();
		for(int i = 0; i < albums.length; i++){
			if (albums[i].albumName.equalsIgnoreCase(albumname)){
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#getPhoto(java.lang.String)
	 */
	@Override
	public Photo getPhoto(String filename) {
		HashMap<String, Photo> allpics = user.allPhotos;
		if (!userHasPicture(filename)){
			System.out.println("The Photo: " + filename + " does not belong to " + user.uniqueID);
			return null;
		}
		else
			//System.out.println(filename);
			return allpics.get(filename);
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#getAlbum(java.lang.String)
	 */
	@Override
	public Album getAlbum(String albumname) {
		if (!userHasAlbum(albumname)){
			//System.out.println("The Album: " + albumname + " does not belong to " + user.uniqueID);
			return null;
		}
		
		for (int i = 0; i < user.userAlbum.size(); i++){
			if (user.userAlbum.get(i).albumName.equalsIgnoreCase(albumname))
				return user.userAlbum.get(i);
		}
		System.out.println("Album " + albumname + "not found.");
		return null;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#addPhoto(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addPhoto(String filename, String caption, String albumname) {
		if (!userHasAlbum(albumname)){
			//System.out.println("The album: " + albumname + " does not exist.");
			return false;
		}
		
		if (userHasPicture(filename) && !getPhoto(filename).caption.equals(caption)){
			//System.out.println("A photo can have only one caption.\nThe caption associated with this " +
			//		"photograph is: " + getPhoto(filename).caption);
			Photo p = getPhoto(filename);
				if (p.albums.contains(albumname)){ //already in the album
					//System.out.println("Photo " + filename + " already exists in album " + albumname);
					return false;
				}
			p.addToAlbum(albumname);
			Album a = getAlbum(albumname);
			a.addPhoto(p);
			return true;
		} else if(userHasPicture(filename)){
			Photo p = getPhoto(filename);
			if (p.albums.contains(albumname)){ //already in the album
				//System.out.println("Photo " + filename + " already exists in album " + albumname);
				return false;
			}
			p.addToAlbum(albumname);
			Album a = getAlbum(albumname);
			a.addPhoto(p);
			return true;
		} else {
			Photo p = new Photo(filename, caption);
			if (p.albums.contains(albumname)){ //already in the album
				//System.out.println("Photo " + filename + " already exists in album " + albumname);
				return false;
			}
			p.addToAlbum(albumname);
			Album a = getAlbum(albumname);
			a.addPhoto(p);
			user.addToAllPhotos(p);
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#removePhoto(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean removePhoto(String filename, String albumname) {
		if (!userHasPicture(filename)){//picture not registered to user
			//System.out.println("That photograph does not exist");
			return false;
		}
		if (!userHasAlbum(albumname)){//album not registered to user
			//System.out.println("That album does not exist");
			return false;
		}
		if (!getAlbum(albumname).getPhotoList().contains(new Photo(filename, ""))){//picture not in album
			//System.out.println("Photo " + filename + "is not in " + albumname);
			return false;
		}
		Photo p = getPhoto(filename);
		Album a = getAlbum(albumname);
		
		p.removeFromAlbum(albumname);
		a.removePhoto(p);
		
		if (p.albums.isEmpty()){ //if the picture now belongs to no albums
			user.allPhotos.remove(filename);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#getAllPhotos()
	 */
	@Override
	public Photo[] getAllPhotos() {
		int len = user.allPhotos.size();
		
		if (len == 0){
			return null;
		}
		
		Photo[] ret = new Photo[len];
		Iterator<String> keys = user.allPhotos.keySet().iterator();
		
		int i = 0;
		while (keys.hasNext()){
			ret[i] = user.allPhotos.get(keys.next());
			i++;
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#getAllAlbums()
	 */
	@Override
	public Album[] getAllAlbums() {
		return user.albumList();
		
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#addAlbum()
	 */
	@Override
	public boolean addAlbum(String albumName) {
		if (userHasAlbum(albumName))
			return false;
		else
			user.addAlbum(new Album(albumName));
		return true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.ModelInterface#removeAlbum()
	 */
	@Override
	public boolean removeAlbum(String albumName) {
		return user.deleteAlbum(getAlbum(albumName));
	}

}
