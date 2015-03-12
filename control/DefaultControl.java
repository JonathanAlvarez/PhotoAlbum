package cs213.photoAlbum.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.PhotoSort;
import cs213.photoAlbum.util.Tag;

/**
 * The control does all manipulation/processing of data within the program 
 * including the creation (but not storage) and deletion of new objects, sorting/filtering data 
 * on various dimensions, and checking data validity that goes beyond syntactic correctness of 
 * input commands. (For instance, checking dates for validity.)
 * 
 * As mentioned earlier, the view can directly interact with the model only for direct access 
 * of data that is not processed in any way. Note that the user specification in the model includes 
 * storing albums in efficient data structures.
 * 
 * The control can know about a single user at a time. Then all operations on the control apply to that user.
 * The control should not be tied to any one particular model implementation, 
 * so it should interact with the model interface(s). 
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class DefaultControl implements ControlInterface {
	
	public Backend backend;
	boolean userSet = false;
	
	public DefaultControl() throws IOException{
		backend = new Backend();
	}
	
	public DefaultControl(String userID) throws IOException{
		backend = new Backend(userID);
		userSet = true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#listUsers()
	 */
	@Override
	public String[] listUsers() {
		String[] list = backend.userList();
		return list;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#addUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUser(String userID, String userName) {
		boolean b = backend.createUser(userID, userName);
		if (b)
			return true;
		else
			return false;
		
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#removeUser(java.lang.String)
	 */
	@Override
	public boolean removeUser(String userID) {
		boolean b = backend.deleteUser(userID);
		if (b)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#setUser(java.lang.String)
	 */
	@Override
	public void setUser(String userID) {
		try {
			backend.readUser(userID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userSet = true;

	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#listPhotos(java.lang.String)
	 */
	@Override
	public String[] listPhotos(String albumName) {
		ArrayList<Photo> photolist;
		if (!backend.userHasAlbum(albumName)){
			System.out.println("Album does not exist");
			return null;
		}
		else {
			photolist = backend.getAlbum(albumName).getPhotoList(); 
			photolist = PhotoSort.dateSort(photolist);
		}
		String[] ret = new String[photolist.size()];
		
		for (int i = 0; i < ret.length; i++){
			ret[i] = photolist.get(i).filename; // + " - " + photolist.get(i).cal.getTime().toString();
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#addPhoto(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addPhoto(String filename, String caption, String albumName) {
		boolean b = backend.addPhoto(filename, caption, albumName);
		if (b)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#movePhoto(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean movePhoto(String filename, String oldAlbum, String newAlbum) {
		boolean b = backend.addPhoto(filename, "", newAlbum);
			if (!b){
				System.out.println("Failed to add photo to new Album");
				return false;
			}
		b = backend.removePhoto(filename, oldAlbum);
		if (!b){
			System.out.println("Failed to remove photo from old album");
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#removePhoto(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean removePhoto(String filename, String albumName) {
		boolean b = backend.removePhoto(filename, albumName);
		if (b)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#addTag(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addTag(String filename, String tagType, String TagValue) {
		Photo p = backend.getPhoto(filename);
		if (p == null){
			System.out.println("The photo does not exist.");
			return false;
		}
		p.addTagToList(new Tag(tagType, TagValue));
		return true;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#removeTag(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean removeTag(String filename, String tagType, String TagValue) {
		Photo p = backend.getPhoto(filename);
		if (p == null){
			System.out.println("Photo does not exist");
		}
		p.deleteTagFromList(new Tag(tagType, TagValue));
		return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#listPhotoInfo(java.lang.String)
	 */
	@Override
	public String[] listPhotoInfo(String filename) {
		Photo p = backend.getPhoto(filename);
		
		if (p == null){
			return null;
		}
		
		int tagsize = 0;
		int numalbums = 0;
		
		if (p.listTags() != null){
			tagsize = p.listTags().length;
		}
		if (p.listAlbums() != null){
			numalbums = p.listAlbums().length;
		}
		
		String[] ret = new String[5];
		
		ret[0] = p.toString();
		ret[1] = p.caption;
		ret[3] = /*"Date: " +*/p.cal.getTime().toString();
		ret[2] = "";//"Album:";
		
		for (int i = 0; i < p.listAlbums().length; i++){
			ret[2] = ret[2] + p.listAlbums()[i] + ", ";
		}
		ret[2] = ret[2].substring(0, ret[2].length() - 2);
		
		ret[4] = "";
		if (p.listTags().length != 0){
			//ret[4] = "Tags:\n ";
			for (int i = 0; i < p.listTags().length; i++){
				ret[4] = ret[4] + p.listTags()[i] + ", ";
			}
		}
		if(ret[4].length() != 0)
			ret[4] = ret[4].substring(0, ret[4].length()-2);
//		int index = 4;
//		for (int i = 0; i < p.listTags().length; i++){
//			ret[index] = p.listTags()[i];
//			index++;
//		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#listByDate(java.lang.String, java.lang.String)
	 */
	@Override
	public String[] listByDate(String startDate, String endDate) {
		Photo[] allphotos = backend.getAllPhotos();
		ArrayList<Photo> inrange = new ArrayList<Photo>();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		// parse date strings
		String[] firstsplit = startDate.split("-");
		String[] startmonthdayyear = firstsplit[0].split("/");
		String[] starthourminsec = firstsplit[1].split(":");
		firstsplit = endDate.split("-");
		String[] endmonthdayyear = firstsplit[0].split("/");
		String[] endhourminsec = firstsplit[1].split(":");
		if (Integer.parseInt(startmonthdayyear[0]) > 12){
			return null;
		}
		if (Integer.parseInt(startmonthdayyear[1]) > 31){
			return null;
		}
		if (Integer.parseInt(endmonthdayyear[0]) > 12){
			return null;
		}
		if (Integer.parseInt(endmonthdayyear[1]) > 31){
			return null;
		}
		start.set(Integer.parseInt(startmonthdayyear[2]), Integer.parseInt(startmonthdayyear[0]) - 1, Integer.parseInt(startmonthdayyear[1]), Integer.parseInt(starthourminsec[0]), Integer.parseInt(starthourminsec[1]), Integer.parseInt(starthourminsec[2]));
		end.set(Integer.parseInt(endmonthdayyear[2]), Integer.parseInt(endmonthdayyear[0]) - 1, Integer.parseInt(endmonthdayyear[1]), Integer.parseInt(endhourminsec[0]), Integer.parseInt(endhourminsec[1]), Integer.parseInt(endhourminsec[2]));

		
		for (int i = 0; i < allphotos.length; i++){
			if (allphotos[i].cal.after(start) && allphotos[i].cal.before(end)){
				inrange.add(allphotos[i]);
			}
		}
		inrange = PhotoSort.dateSort(inrange);
		
		String[] ret = new String[inrange.size()];
		for (int i = 0; i < ret.length; i++){
			Photo p = inrange.get(i);
			ret[i] = p.caption + " - Album: ";
			for (int j = 0; j < p.listAlbums().length; j++){
				ret[i] = ret[i] + p.listAlbums()[j] + ", ";
			}
			ret[i] = ret[i].substring(0, ret[i].length() - 2) + " Date: " + p.cal.getTime().toString();
					
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#listByTag(java.lang.String[])
	 */
	@Override
	public String[] listByTag(String[] tagArray) {
		int numtags = tagArray.length/2;
		Tag[] tags = new Tag[numtags];
		
		Photo[] allphotos = backend.getAllPhotos();
		ArrayList<Photo> withtags = new ArrayList<Photo>();
		
		for (int i = 0; i <numtags; i++){
			tags[i] = new Tag(tagArray[2*i], tagArray[2*i + 1]);
		}
		
		for (int i = 0; i < allphotos.length; i++){
			boolean hastags = true;
			for (int j = 0; j < numtags; j++){
				if (!allphotos[i].tagList.contains(tags[j])){
					hastags = false;
				}
			}
			if (hastags){
				withtags.add(allphotos[i]);
			}
		}
		
		String[] ret = new String[withtags.size()];
		
		for (int i = 0; i < withtags.size(); i++){
			Photo p =withtags.get(i);
			ret[i] = p.caption + " - Album: ";

			for (int j = 0; j < p.listAlbums().length; j++){
				ret[i] = ret[i] + p.listAlbums()[j] + ", ";
			}
			ret[i] = ret[i].substring(0, ret[i].length() - 2) + " Date: " + p.cal.getTime().toString();

		}
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.control.ControlInterface#addAlbum(java.lang.String)
	 */
	@Override
	public boolean addAlbum(String albumName) {
		return backend.addAlbum(albumName);
	}

	/* (non-Javadoc)
	*  @see cs213.photoAlbum.control.ControlInterface#removeAlbum(java.lang.String)
	*/
	@Override
	public boolean removeAlbum(String albumName) {
		return backend.removeAlbum(albumName);
	}
	
	/* (non-Javadoc)
	*  @see cs213.photoAlbum.control.ControlInterface#listAlbums()
	*/
	public Album[] listAlbums(){
		return backend.getAllAlbums();
	}

	/* (non-Javadoc)
	*  @see cs213.photoAlbum.control.ControlInterface#saveData()
	*/
	public void saveData() {
		backend.saveState();
	}
}
