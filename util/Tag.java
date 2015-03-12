package cs213.photoAlbum.util;

import java.io.Serializable;

/**
 * A tag is a combination of tag type and tag value, and each tag in a photo is unique. 
 * For instance, the people who appear in a photo will appear in multiple tags, one tag per 
 * person. Their are two types for a tag, person and location. (more types added later if needed)
 * @author Brent Engler, Jonathan Alvarez
 *
 */

public class Tag implements Serializable {

	public String type;
	public String value;
	
	public Tag(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * toString method showing a neat representation of a Tag.
	 * @return string representation of a tag
	 */
	public String toString() {
		return type + ": " + value;
	}
	
	public boolean equals(Object o){
		if (o instanceof Tag){
			Tag other = (Tag)o;
			if (other.type.equals(type) && other.value.equals(value)){
				return true;
			}
		}
		return false;
	}
}
