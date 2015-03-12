package cs213.photoAlbum.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class serves to keep track of the users in the database by storing them into
 * an ArrayList for easy retrieval.
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class UserList implements Serializable{

	ArrayList<String> userIDs;

	private UserList(){
		userIDs = new ArrayList<String>();
	}

	/**
	 * saves the user list to the file userlist.bin located in the data folder
	 */
	public void writeUserList(){
		File f = new File("data/userlist.bin");
		if (f.exists())
			f.delete();
		try{
			OutputStream file = new FileOutputStream("data/userlist.bin");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try{
				output.writeObject(this);
			} finally { output.close(); }
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}
	
	/**
	 * Reads a user list from the file userlist.bin, if one exists, or creates a new list
	 * @return Returns user list read from file, or new user list
	 * @throws IOException
	 */
	public static UserList loadUserList() throws IOException{

		File f = new File("data/userlist.bin");
		
		if (!f.exists()){
			return new UserList();
		}
		
		ObjectInputStream objectIn = null;
		
		try {
			objectIn = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream("data/userlist.bin")));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to read in userlist.bin");
		}
		try {
			return (UserList) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to read object from userlist*.bin");
		} catch (IOException e) {
			System.out.println("Failed to read object from userlist**.bin");
		}

		objectIn.close();
		
		return new UserList();
	}
	
	/**
	 * adds a new user ID to the list of user IDs
	 * @param userID ID of the user to be added
	 * @return true if successful
	 */
	public boolean addUser(String userID){
		if (userIDs.contains(userID)){
			return false;
		}
		userIDs.add(userID);
		return true;
	}

	/**
	 * For getting an array of all user IDs
	 * @return String[] of userIDs, null if empty
	 */
	public String[] getIDs(){
		String[] ret = new String[userIDs.size()];
		for (int i = 0; i < ret.length; i++){
			ret[i] = userIDs.get(i);
		}
		
		return ret;
	}
	
	/**
	 * Checks whether a user is registered in the user list
	 * @param userID User to be checked against the list
	 * @return true if the user is present
	 */
	public boolean userPresent(String userID){
		if (userIDs.contains(userID)){
			return true;
		}
		else
			return false;
	}
	
	/**
	 * removes a user from the list
	 * @param userID ID of the user to be removed
	 * @return true if successful
	 */
	public boolean removeUser(String userID){
		if(!userIDs.contains(userID)){
			return false;
		}
		else{
			userIDs.remove(userID);
			return true;
		}
	}

}
