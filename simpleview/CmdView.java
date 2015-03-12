package cs213.photoAlbum.simpleview;

import cs213.photoAlbum.control.DefaultControl;
import cs213.photoAlbum.model.Album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * The default user interface for the photo album application, runs on the command line.
 * Photos are not displayed, but can be named and manipulated. Can run several commands by 
 * accepting arguments through the command line on calling the program. <p>
 * 
 * - CmdView.exe listusers // Displays a list of user names<p>
 * - CmdView.exe adduser {user id} "{user name}" // Adds a user with {user id} and {user name}<p>
 * - CmdView.exe deleteuser {user id} // Deletes a user with {user id}<p>
 * - CmdView.exe login {user id} // Enters interactive mode, and loads the albums of user with {user id}<p>
 * 
 * Once a user has been logged in, the program enters interactive mode, which supports the following
 * commands:<p>
 * 
 * createAlbum "{name}" <p>
 * deleteAlbum "{name}" <p>
 * listAlbums<p>
 * listPhotos "{name}" <p>
 * addPhoto "{fileName}" "{caption}" "{albumName}" <p>
 * movePhoto "{fileName}" "{oldAlbumName}" "{newAlbumName}"<p>
 * removePhoto "{fileName}" "{albumName}"<p>
 * addTag "{fileName}" {tagType}:"{tagValue}"<p>
 * deleteTag "{fileNme}" {tagType}:"{tagValue}"<p>
 * listPhotoInfo "{fileNme}"<p>
 * getPhotosByDate {start date} {end date} <p>
 * getPhotosByTag [{tagType}:]{tagValue} [,[{tagType}:]{tagValue}]... <p>
 * logout <p>
 * 
 * @author Brent Engler, Jonathan Alvarez
 */
public class CmdView {

	public static DefaultControl control;
	public static String curUserID;
	
	private CmdView(){}
	
	/**
	 * Main method run by the virtual machine. Accepts the user input from the command line and parses it.
	 * @param args Command that should be run; List Users, Add User, Delete User, and Log In.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0){
			System.out.println("Available Commands:\n\tLog In-\n\tlogin <username> //Log in as <username>" +
					"\n\n\tAdd User-\n\tadduser <userID> <userName> //Add a user with <userID>, <userName>\n\n\t" +
					"Remove User-\n\tdeleteuser <userID> //Delete a user with <userID>\n\n\tList Users-\n\tlistusers" +
					" //List all currently registered users");
			System.exit(0);
		}
		try{
			parseInput(args);
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		
	}
	/**
	 * Determines what command to execute based on the user input, and runs that command
	 * @param input String array read from the command line
	 * @throws IOException 
	 * 
	 */
	private static void parseInput(String[] args) throws IOException{
		control = new DefaultControl();
		if (args[0].equalsIgnoreCase("listusers")){
			if (args.length != 1){
				System.out.println("listuser takes no other input");
			}
			listuser();
		} else if (args[0].equalsIgnoreCase("login")){
			if (args.length != 2){
				System.out.println("Wrong number of inputs");
				System.exit(0);
			}
			login(args[1]);
		} else if (args[0].equalsIgnoreCase("adduser")){
			if (args.length != 3){
				System.out.println("Wrong number of inputs");
				System.exit(0);
			}
			adduser(args[1], args[2]);
		} else if (args[0].equalsIgnoreCase("deleteuser")){
			if (args.length != 2){
				System.out.println("Wrong number of inputs");
				System.exit(0);
			}
			deleteuser(args[1]);
		} else
			throw new IOException(args[0] + " is not a recognized command");
		
	}
	
	/**
	 * Logs in given the userID and starts interactive mode 
	 * @param userID user that is currently being logged in
	 */
	private static void login(String userID){
		try{
			control = new DefaultControl(userID);
			curUserID = userID;
			interactivemode();
		} catch (IOException e){
			System.out.println(e.getMessage());
			//System.exit(0);
		}
	}
	
	/**
	 * Adds a new user to the database
	 * @param userID ID of the new user
	 * @param userName name of the new user
	 */
	private static void adduser(String userID, String userName){
		if (control.addUser(userID, userName)){
			System.out.println("created user " + userID + " with name " + userName);
		}
	}
	
	/**
	 * Deletes a user from the database
	 * @param userID ID of the user to be deleted
	 */
	private static void deleteuser(String userID){
		if (control.removeUser(userID)){
			System.out.println("deleted user " + userID);
		}
	}
	
	/**
	 * Prints out a list of currently registered users from the database.
	 */
	private static void listuser(){
		String[] users = control.listUsers();
		if (users == null) {
			System.out.println("no users exist");
		} else {
			for (int i = 0; i < users.length; i++){
				System.out.println(users[i]);
			}
		}
	}
	
	/**
	 * Loop that enables the currently login user to modify its attributes
	 * Logging off will save the changes the user made, otherwise they're discarded
	 */
	private static void interactivemode(){
		System.out.println("Logged In. Please enter a command, or type 'help' for options");
		Scanner sc = new Scanner(System.in);
		
		while (true){
			String line = sc.nextLine();
			String[] tokens = line.split("\"");
			ArrayList<String> tokensal = new ArrayList<String>();
			for (int i = 0; i < tokens.length; i++){
				if (!tokens[i].equals(" ")){
					tokensal.add(tokens[i]);
				}
			}
			tokens = new String[tokensal.size()];
			for (int i = 0; i < tokensal.size(); i++){
				tokens[i] = tokensal.get(i).trim();
			}
			
			//System.out.println(tokens.length);
			
			if (tokens[0].equals("createAlbum")) {
				if (tokens.length == 2)
					createAlbum(tokens[1]);
			} else if (tokens[0].equals("deleteAlbum")) {
				if (tokens.length == 2)
					deleteAlbum(tokens[1]);
			} else if (tokens[0].equals("listAlbums")) {
				listAlbums();
			} else if (tokens[0].equals("listPhotos")) {
				if (tokens.length == 2)
					listPhotos(tokens[1]);
			} else if (tokens[0].equals("addPhoto")) {
				if (tokens.length == 3 || tokens.length == 4)
					addPhoto(tokens);
			} else if (tokens[0].equals("movePhoto")) {
				if (tokens.length == 4)
					movePhoto(tokens[1], tokens[2], tokens[3]);
			} else if (tokens[0].equals("removePhoto")) {
				if (tokens.length == 3)
					removePhoto(tokens[1], tokens[2]);
			} else if (tokens[0].equals("addTag")) {
				if (tokens.length == 4 || tokens.length ==3)
					addTag(tokens);
			} else if (tokens[0].equals("deleteTag")) {
				if (tokens.length == 4 || tokens.length ==3)
					deleteTag(tokens);
			} else if (tokens[0].equals("listPhotoInfo")) {
				if (tokens.length == 2)
					listPhotoInfo(tokens[1]);
			} else if (tokens[0].equals("getPhotosByDate")) {
				if (tokens.length == 3)
					getPhotosByDate(tokens[1], tokens[2]);
			} else if (tokens[0].contains("getPhotosByTag")) {
				getPhotosByTag(line);
			} else if (tokens[0].equals("help")) {
				help();
			} else if (tokens[0].equals("logout")) 
				logout();
			else 
				System.out.println("Invalid Input. Type help for options.");
		}
	}
	
	/**
	 * Creates a new album for the current user
	 * @param albumName name of the new album
	 */
	private static void createAlbum(String albumName){
		boolean b = control.addAlbum(albumName);
		if (b) 
			System.out.println("created album for user " + curUserID + ":\n" + albumName);
		else
			System.out.println("album exists for user " + curUserID + ":\n" + albumName);
	}

	/**
	 * Deletes the user's specified album
	 * @param albumName name of the album to be deleted
	 */
	private static void deleteAlbum(String albumName){
		boolean b = control.removeAlbum(albumName);
		if (b)
			System.out.println("deleted album from user " + curUserID + ":\n" + albumName);
		else
			System.out.println("album does not exist for user " + curUserID + ":\n" + albumName);
	}
	
	/**
	 * Prints a list of the user's current albums
	 */
	private static void listAlbums(){
		Album[] albums = control.listAlbums();
		if (albums != null){
			System.out.println("Albums for user " + curUserID + ":");
			for (int i = 0; i < albums.length; i++){
				if (albums[i].getPhotoList().size() == 0){
					System.out.print(albums[i].albumName + " number of photos: 0\n");
				} else {
					System.out.print(albums[i].albumName + " number of photos: " + albums[i].getPhotoList().size() + ", " 
						+ albums[i].start.toString() + " - " + albums[i].end.toString() + "\n");
				}
			}
		} else{
			System.out.println("No albums exist for user " + curUserID);
		}
			//<name> number of photos: <numberOfPhotos>, <start date> - <end date>
	}
	
	/**
	 * Prints a list of the album's photos
	 * @param albumName string of the album to be called
	 */
	private static void listPhotos(String albumName){
		String[] photos = control.listPhotos(albumName);
		if (photos != null){
			System.out.println("Photos for album " + albumName + ":");
			for (int i = 0; i < photos.length; i++){
				System.out.print(photos[i] + ", ");
			}
		}
		System.out.println("\n");
	}
	
	/**
	 * Adds a photo to the specified album
	 * @param data information to be added which contain the filename, caption(if given), and albumname
	 */
	private static void addPhoto(String[] data){
		boolean b;
		if (data.length == 3){// no caption given
			b = control.addPhoto(data[1], "", data[2]);
		}
		else
			b = control.addPhoto(data[1], data[2], data[3]);
		
		if (b){
			System.out.println("Added photo " + data[1] + ":");
			String[] photoinfo = control.listPhotoInfo(data[1]);
			System.out.println(photoinfo[1] + " - Album: " + data[data.length - 1]);
		}
	}
	
	/**
	 * Moves a photo from one album to another
	 * @param filename file name of the photo
	 * @param oldAlbum album the photo was contained in
	 * @param newAlbum album the photo will be moved to
	 */
	private static void movePhoto(String filename, String oldAlbum, String newAlbum){
		if (control.movePhoto(filename, oldAlbum, newAlbum)){
			System.out.println("Moved photo " + filename + ":");
			System.out.println(filename + " - From album " + oldAlbum + " to album " + newAlbum);
		}
	}
	
	/**
	 * Deletes a photo from an album
	 * @param filename file name of the photo
	 * @param albumName name of the album where the photo is stored
	 */
	private static void removePhoto(String filename, String albumName){
		if (control.removePhoto(filename, albumName)){
			System.out.println("Removed photo:");
			System.out.println(filename + " - From album " + albumName);
		}
	}
	
	/**
	 * Adds a tag to the specified photo
	 * @param data contains the name of the photo and the tag properties
	 */
	private static void addTag(String[] data){
		boolean b;
		String type;
		String value;
		if (data.length == 3){//typless tag
			b = control.addTag(data[1], "", data[2]);
			type = "";
			value = data[2];
		}
		else {
			if (data[2].charAt(data[2].length() - 1) == ':')
				data[2] = data[2].substring(0, data[2].length() - 1);
			b = control.addTag(data[1], data[2], data[3]);
			type = data[2];
			value = data[3];
		}
		if (b) {
			System.out.println("Added tag\n" + data[1] + " " + type + ":" +value);
		}
	}
	
	/**
	 * deletes a tag from the specified photo
	 * @param data contains the name of the photo and tag properties
	 */
	private static void deleteTag(String[] data){
		if (data.length == 3){//typless tag
			control.removeTag(data[1], "", data[2]);
			System.out.println("Removed tag:\n" + data[1] + " :" + data[2]);
		}
		else{
			control.removeTag(data[1], data[2], data[3]);
			System.out.println("Removed tag:\n"+ data[1] + " " + data[2] + ":" + data[3]);
		}
		
	}
	
	/**
	 * lists the information of the specified photo
	 * @param filename file name of the photo
	 */
	private static void listPhotoInfo(String filename){
		String[] info = control.listPhotoInfo(filename);
		if (info == null){
			System.out.println("Photo does not exist");
		}
		else
			for (int i = 0; i < info.length; i++){
				System.out.println(info[i]);
			}
	}
	
	/**
	 * Retrieves all photos taken within a given range of dates, in chronological order
	 * @param start beginning date
	 * @param end ending date
	 */
	private static void getPhotosByDate(String start, String end){
		String[] bydate = control.listByDate(start, end);
		System.out.println("Photos for user " + curUserID + " in range " + start + " - " + end);
		if (bydate != null)
			for (int i = 0; i < bydate.length; i++){
				System.out.println(bydate[i]);
			}
	}
	
	/**
	 * To retrieve all photos that have all the given tags, in chronological order
	 * Tags can be specified with or without their types
	 * @param data tag properties that are used to determine which photos to get
	 */
	private static void getPhotosByTag(String line){
		line = line.substring(line.trim().indexOf(' '));
		line = line.trim();
		
		String[] tagterms = line.split(",");

		ArrayList<String> tagdata = new ArrayList<String>();
		try{
			for (int i = 0; i < tagterms.length; i++){
				if (tagterms[i].indexOf(':') != -1){//contains a colon, and therefore has a type and value
					int index = tagterms[i].indexOf(':');
					tagdata.add(tagterms[i].substring(0, index).trim());
					tagdata.add(tagterms[i].substring(index + 1).trim());
				}
				else {
					tagdata.add("");
					tagdata.add(tagterms[i].trim());
				}
			}
			
			String[] tagdataarr = new String[tagdata.size()];
			for (int i = 0; i < tagdata.size(); i++){
				tagdataarr[i] = tagdata.get(i);
			}
			
			String[] list = control.listByTag(tagdataarr);
			System.out.println("Photos for user " + curUserID + " with search string " +line);
			for (int i = 0; i< list.length; i++){
				System.out.println(list[i]);
			}
			
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Error: Improperly formatted search string");
		}
	}

	/**
	 * Saves the changes made by the user and terminates the program
	 */
	private static void logout(){
		control.saveData();
		System.exit(0);
	}
	
	/**
	 * Lists the functions that can be used when logged into interactive mode
	 */
	private static void help(){
		System.out.println("Available Commands: \n" +
				"createAlbum \"{name}\"\n" +
				"deleteAlbum \"{name}\"\n" +
				"listAlbums\n" +
				"listPhotos \"{name}\"\n" + 
				"addPhoto \"{fileName}\" \"{caption}\" \"{albumName}\"\n" + 
				"movePhoto \"{fileName}\" \"{oldAlbumName}\" \"{newAlbumName}\"\n" +
				"removePhoto \"{fileName}\" \"{albumName}\"\n" +
				"addTag \"{fileName}\" {tagType}:\"{tagValue}\"\n" +
				"deleteTag \"{fileName}\" {tagType}:\"{tagValue}\"\n" +
				"listPhotoInfo \"{fileName}\"\n" +
				"getPhotosByDate {start date} {end date}\n" + 
				"getPhotosByTag [{tagType}:]{tagValue} [,[{tagType}:]{tagValue}]...\n" + 
				"logout");
	}
	
}
