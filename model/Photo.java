package cs213.photoAlbum.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import cs213.photoAlbum.util.Tag;

/**
 * A photo is a class that contains a filename, caption, date the photo was taken, and tags.
 * Its only function is the ability to edit its own attributes.
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class Photo implements Serializable, Comparable<Photo> {
	
	public final String filename;
	public String caption;
	public Calendar cal;
	public ArrayList<Tag> tagList;
	ArrayList<String> albums;
	
	public Photo(String filename, String caption){
		this.filename = filename;
		this.caption = caption;
		File f = new File("data" + File.separator + filename);
		
		cal = Calendar.getInstance();
		cal.setTimeInMillis(f.lastModified());
		tagList = new ArrayList<Tag>();
		albums = new ArrayList<String>();
	}
	
	/**
	 * Setter method that changes the Photo's caption.
	 * @param caption string used to change the current caption
	 */
	public void setPhotoCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Getter method for the list of albums a photograph is in
	 * @return String[] containing the names of albums
	 */
	public String[] listAlbums(){
		String[] list = new String[albums.size()];
		for (int i = 0; i < list.length; i++){
			list[i] = albums.get(i);
		}
		
		return list;
	}
	
	/**
	 * Setter method that changes the time the photo was taken.
	 * @param cal new time for the photo
	 */
	public void setCalendarDate(Calendar cal) {
		this.cal = cal;
	}
	
	/**
	 * Marks that a photo is in an album
	 * @param albumname album the photo has been added to
	 * @return true if successful
	 */
	public boolean addToAlbum(String albumname){
		if (albums.contains(albumname)){
			return false;
		}
		albums.add(albumname);
		return true;
	}
	
	/**
	 * Marks that the photo is no longer in an album
	 * @param albumname album the photo is being removed from
	 * @return true if successful
	 */
	public boolean removeFromAlbum(String albumname){
		if (albums.contains(albumname)){
			albums.remove(albumname);
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a tag to the photo's tag list.
	 * @param tg the tag to be added.
	 */
	public void addTagToList(Tag tg) {
		tagList.add(tg);
	}
	
	/**
	 * Deletes a tag from the photo's tag list.
	 * @param tg the tag to be deleted.
	 */
	public void deleteTagFromList(Tag tg) {
		tagList.remove(tg);
	}
	
	/**
	 * Converts the tag list into a readable string array.
	 * @return a string array of tags
	 */
	public String[] listTags() {
		String[] tags = new String[tagList.size()];
		
		for (int i = 0; i < tagList.size(); i++) {
			tags[i] = tagList.get(i).toString();
		}
		return tags;
	}

	/**
	 * Implements the comparable interface. Compares photos based on the calendar object each contains
	 * @param other Photo object that's being compared
	 * @return 1 if this photo came after other. 0 if they're equal. -1 if this photo came before other.
	 */
	public int compareTo(Photo other) {
		if (cal.after(other))
			return 1;
		else if (cal.before(other))
			return -1;
		else 
			return 0;
	}
	
	/**
	 * Checks to see if both photographs have the same filename, and are the same picture
	 * @param o Object to be compared to the photo
	 * @return true if the photo filenames are equal, false otherwise
	 */
	public boolean equals(Object o){
		if (o instanceof Photo){
			Photo other = (Photo)o;
			if (other.filename.equals(this.filename)){
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * toString method for the photo class
	 * @return A string containing the filename of the photo
	 */
	public String toString(){
		return filename;
	}
}