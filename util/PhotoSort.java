package cs213.photoAlbum.util;

import java.util.ArrayList;

/**
 * A Utility class for sorting photo objects
 * @author Brent Engler, Jonathan Alvarez
 *
 */
public class PhotoSort {
	
	
	/**
	 * Sorts An array list (in this case, an array of photo objects by date) using bubble sort, since the 
	 * array is expected to be mostly ordered already.
	 * @param unsortedArray Array to be sorted
	 * @return Returns the sorted array
	 */
	public static <T extends Comparable<T>> ArrayList<T> dateSort(ArrayList<T> unsortedArray){
		int numitems = unsortedArray.size();
		for (int i = 0; i < numitems - 1; i++){// outer loop, iterates through places in list
			for (int j = i; j < numitems - 1; j ++){
				if (unsortedArray.get(j).compareTo(unsortedArray.get(j+1)) == -1 || unsortedArray.get(j).compareTo(unsortedArray.get(j+1)) == 0){
					break;
				}
				else {
					T temp = unsortedArray.get(j);
					unsortedArray.set(j, unsortedArray.get(j+1));
					unsortedArray.set(j + 1, temp);
				}
			}
			
		}
		
		return unsortedArray;
	}

}
