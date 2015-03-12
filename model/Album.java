package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * An album is a data structure which holds a number of photographs in an array of photo objects. 
 * There is no upper limit on the number of photographs which can be contained in an album. An album has the
 * functionality to add and remove pictures from itself.
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class Album implements Serializable{
	
	public String albumName;
	private ArrayList<Photo> photoList;
	public Date start;
	public Date end;
	
	/**
	 * Creates a new album with the given name
	 * @param albumName String which contains the desired name of the album
	 */
	public Album(String albumName){	
		this.albumName = albumName;
		photoList = new ArrayList<Photo>();
		photoList.clear();
		start = new Date();
		start.setYear(2200);
		end = new Date();
		end.setYear(1);
	}
	
	/**
	 * Adds a photograph to the album
	 * @param picture A Photo object which represents the photograph to be added
	 * @return Returns false if the photo was not added
	 */
	public boolean addPhoto(Photo picture){
		if (photoList.contains(picture)){
			return false;
		}
		photoList.add(picture);
		if (photoList.contains(picture)){
			if (start.after(picture.cal.getTime())){
				start = picture.cal.getTime();
			}
			if (end.before(picture.cal.getTime())){
				end = picture.cal.getTime();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a photograph from the album
	 * @param picture A Photo object which represents the picture to be removed
	 * @return Returns false if the photo was not removed
	 */
	public boolean removePhoto(Photo picture){
		if (!photoList.contains(picture)){
			return false;
		}
		photoList.remove(picture);
		if (photoList.contains(picture))
			return false;
		return true;
	}
	
	/**
	 * Getter method for the album's photo list
	 * @return an ArrayList of photos containing the album's current photo list
	 */
	public ArrayList<Photo> getPhotoList() {
		return photoList;
	}
	
	public void rename(String newname){
		
		for (int i = 0; i < photoList.size(); i++){
			photoList.get(i).albums.remove(albumName);
			photoList.get(i).albums.add(newname);
		}
		
		albumName = newname;
	}
	
	/**
	 * Compares two albums to determine whether they're equal by album name
	 * @param o Object to compare to another album
	 * @return true if the album names equal, false otherwise
	 */
	public boolean equals(Object o){
		if (o instanceof Album){
			Album other  = (Album)o;
			if (other.albumName == albumName){
				return true;
			}
		}
		return false;
	}
}
