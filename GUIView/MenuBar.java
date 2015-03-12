package cs213.photoAlbum.GUIView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.Tag;

/**
 * MenuBar class that deals with all of the functions pertinent to the JMenuBar.
 * @author Brent
 *
 */
public class MenuBar extends JMenuBar {

	public JMenu albumJM, photoJM, searchJM, slideshowJM, logoutJM, albumDeleteJM, albumRenameJM, albumViewJM,
	photoDeleteJM, photoEditJM, photoMoveJM, photoViewJM;
	public JMenuItem albumCreateJMI, photoAddJMI, 
	dateJMI, tagJMI, 
	playJMI,
	saveJMI;
	
	int i; // counter
	String[] albumnames;
	Album currentAlbum;
	final UserView view;

	/**
	 * Constructor that initializes the contents of the JMenuBar based on the current UserView
	 * @param userview contents used for initialization
	 */
	public MenuBar(UserView userview){
		this.view = userview;
		refreshMenu();
		
		photoJM.setEnabled(false);
		slideshowJM.setEnabled(false);
		
	}
	
	/**
	 * Refreshes the JMenuItems in the album JMenu
	 */
	public void refreshAlbumList(){
		remove(albumJM);
		remove(photoJM);
		remove(searchJM);
		remove(slideshowJM);
		remove(logoutJM);
		
		refreshMenu();
		
		photoJM.setEnabled(false);
		slideshowJM.setEnabled(false);
	}
	
	/**
	 * Refreshes the JMenuItems in the photo JMenu
	 * @param currentAlbum
	 */
	public void refreshPhotoList(Album currentAlbum){
		this.currentAlbum = currentAlbum;
		
		remove(albumJM);
		remove(photoJM);
		remove(searchJM);
		remove(slideshowJM);
		remove(logoutJM);
		
		refreshMenu();
		
		photoJM.setEnabled(true);
		slideshowJM.setEnabled(true);
		
		ArrayList<Photo> photos = currentAlbum.getPhotoList();
		
		for (i = 0; i < photos.size(); i++){
			JMenuItem photoitem = new JMenuItem(photos.get(i).filename);
			photoDeleteJM.add(photoitem);
			photoitem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					view.deletePhoto(extractName(arg0.paramString()));
				}
				
			});
		}
		
		for (i = 0; i < photos.size(); i++){
			JMenuItem photoitem = new JMenuItem(photos.get(i).filename);
			photoEditJM.add(photoitem);
			photoitem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					EditPhotoDialog editPhoto = new EditPhotoDialog(view, extractName(arg0.paramString()),(ArrayList<Tag>)view.control.backend.getPhoto(extractName(arg0.paramString())).tagList.clone());
				}
			});
		}
		
		for (i = 0; i < photos.size(); i++){
			JMenuItem photoitem = new JMenuItem(photos.get(i).filename);
			photoMoveJM.add(photoitem);
			photoitem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					RelocatePhotoDialog move = new RelocatePhotoDialog(view, extractName(e.paramString()), MenuBar.this.currentAlbum.albumName);
					move.addWindowListener(new WindowAdapter(){
						public void windowClosed(WindowEvent E){
							try {
								MenuBar.this.view.photoMode(MenuBar.this.view.currentAlbum);
								invalidate();
								validate();
								repaint();
							} catch (IOException ee) {
								// TODO Auto-generated catch block
								ee.printStackTrace();
							}
						}
					});
				}
				
			});
		}
		for (i = 0; i < photos.size(); i++){
			JMenuItem photoitem = new JMenuItem(photos.get(i).filename);
			photoViewJM.add(photoitem);
			photoitem.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					view.curPhoto = view.control.listPhotoInfo(extractName(arg0.paramString()));//viewPhoto(MenuBar.this.currentAlbum.getPhotoList().get(i));//currentphoto
					try {
						view.photoDisplayMode(view.currentAlbum);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	/**
	 * Constructs a new JMenu based on any changes that were made to an album or photo.
	 */
	public void refreshMenu(){
		albumJM = new JMenu("Album");
		photoJM = new JMenu("Photo");
		searchJM = new JMenu("Search");
		slideshowJM = new JMenu("Slideshow");
		logoutJM = new JMenu("Logout");

		albumCreateJMI = new JMenuItem("Create");
		albumDeleteJM = new JMenu("Delete");
		albumRenameJM = new JMenu("Rename");
		albumViewJM = new JMenu("View");

		photoAddJMI = new JMenuItem("Add");
		photoDeleteJM = new JMenu("Delete");
		photoEditJM = new JMenu("Edit");
		photoMoveJM = new JMenu("Move");
		photoViewJM = new JMenu("View");

		//AlbumFromSearchJMI = new JMenuItem("Create Album from Search Results");
		dateJMI = new JMenuItem("Search by Date");
		dateJMI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				view.searchByDate();
			}
		});
		tagJMI = new JMenuItem("Search by Tag");
		tagJMI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				view.searchByTag();
			}
		});

		playJMI = new JMenuItem("Play");
		saveJMI = new JMenuItem("Logout & Save");

		add(albumJM);
		add(photoJM);
		add(searchJM);
		add(slideshowJM);
		add(logoutJM);
		
		photoJM.setEnabled(false);

		albumJM.add(albumCreateJMI);
		albumCreateJMI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				view.createAlbum();
			}
			
		});
		
		albumJM.add(albumDeleteJM);
		albumJM.add(albumRenameJM);
		albumJM.add(albumViewJM);
		
		Album[] albums = UserView.control.listAlbums();
		albumnames = new String[albums.length];
		
		for (i = 0; i < albums.length; i++){
			albumnames[i] = albums[i].albumName;
		}
		
		for (i = 0; i < albums.length; i++){
			JMenuItem albumJMI = new JMenuItem(albumnames[i]);
			albumDeleteJM.add(albumJMI);
			albumJMI.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){

					view.deleteAlbum(extractName(e.paramString()));
				}
			});
		}
		for (i = 0; i < albums.length; i++){
			JMenuItem albumJMI = new JMenuItem(albumnames[i]);
			albumRenameJM.add(albumJMI);
			albumJMI.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					view.renameAlbum(extractName(e.paramString()));
				}
			});
		}
		for (i = 0; i < albums.length; i++){
			JMenuItem albumJMI = new JMenuItem(albumnames[i]);
			albumViewJM.add(albumJMI);
			albumJMI.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					view.viewAlbum(extractName(e.paramString()));
				}
			});
		}
		

		photoJM.add(photoAddJMI);
		photoAddJMI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				view.addPhoto();
			}
		});
		
		photoJM.add(photoDeleteJM);
		photoJM.add(photoEditJM);
		photoJM.add(photoMoveJM);
		photoJM.add(photoViewJM);

		searchJM.add(dateJMI);
		dateJMI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//view.dateSearch();
			}
		});
		
		searchJM.add(tagJMI);
		tagJMI.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//view.tagSearch();
			}
			
		});

		//searchJM.add(AlbumFromSearchJMI);
		//AlbumFromSearchJMI.setEnabled(false);
		slideshowJM.add(playJMI);
		playJMI.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				view.slideshow();	
			}
			
		});
		
		logoutJM.add(saveJMI);
		saveJMI.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.logout();
			}
		});
	}
	
	/**
	 * Used for extracting photo contents
	 * @param s the photo contents
	 * @return a string containing a certain part of the photo contents
	 */
	private String extractName(String s){
		return s.substring(21).substring(0, s.substring(21).indexOf(','));
	}
	
	public void disableAlbum(){
		albumJM.setEnabled(false);
	}
}
