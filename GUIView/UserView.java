package cs213.photoAlbum.GUIView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.BorderFactory;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import sun.util.calendar.LocalGregorianCalendar.Date;

import cs213.photoAlbum.control.DefaultControl;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.Tag;

/**
 * User View is the class dedicated to all things that the user is able to do.
 * The gui user interface for the photo album application, runs through a JFrame.
 * Albums and Photos can be displayed, named and manipulated. 
 * @author Jonathan
 *
 */
public class UserView extends JFrame implements ActionListener, ListSelectionListener {

	//User View Variables
	public JMenuBar userJMB;
	public JMenu albumJM, photoJM, searchJM, slideshowJM, logoutJM;
	public JMenuItem albumCreateJMI, albumDeleteJMI, albumRenameJMI, albumViewJMI, 
	photoAddJMI, photoDeleteJMI, photoEditJMI, photoMoveJMI, photoViewJMI, 
	dateJMI, tagJMI,
	playJMI,
	saveJMI;
	private JLabel anameJL, numPhotosJL, startJL, endJL, 
	albumNameJL, albumNumPhotosJL, albumStartJL, albumEndJL;
	private JPanel leftPanel, rightPanel;	
	private String curUser, curAlbum;
	int currentAlbum;
	String[] curPhoto;
	private boolean inPhotoMode = false;
	boolean inAlbumListMode = false;
	private boolean inAlbumDetailMode = false;
	private boolean inSearchViewMode = false;
	public static DefaultControl control;

	//Album Mode Variables
	private Album[] albums;
	private JButton openJB, createJB, deleteJB, renameJB;
	private JPanel albumLabelPanel, albumButtonPanel;
	private JList albumsJL;
	private DefaultListModel listModel;

	//Photo Mode Variables
	Album currentAlbumObject;
	private JLabel photoJL, pnameJL, palbumsJL, captionJL, dateJL, tagsJL,
	photoNameJL, photoAlbumsJL, photoCaptionJL, photoDateJL, photoTagsJL;
	private JButton addJB, delJB, editJB, moveJB, viewJB, backJB;
	private JPanel photoLabelPanel, photoButtonPanel, photoPanel;
	private JScrollPane photoSP;
	private boolean invalidImage = false;
	
	private static final Font titleFont = new Font("Dialog", Font.BOLD, 18);
	private static final Font headerFont = new Font("Dialog", Font.BOLD, 13);
	private static final Font entryFont = new Font("Courier", Font.PLAIN, 13);
	


	/*
	 * 
	 * 
	 * User View Constructor + Methods
	 * 
	 * 
	 */

	/**
	 * Constructs a new JFrame with a JMenuBar. Starts the User View out
	 * in Album Mode.
	 * @param title title used for the JFrame
	 * @param userID ID of the currently logged in User
	 * @throws IOException
	 */
	public UserView(String title, String userID) throws IOException {

		super(title);
		inAlbumListMode = true;
		control = new DefaultControl(userID);
		setSize(800,600);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false); 
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				control.saveData();
				LoginView lgv;
				try {
					lgv = new LoginView("Login");
					lgv.setSize(300,90);
					lgv.setVisible(true);
					lgv.setLocationRelativeTo(null);
					lgv.setResizable(false);
					lgv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			public void windowClosed(WindowEvent e){
				control.saveData();
				LoginView lgv;
				try {
					lgv = new LoginView("Login");
					lgv.setSize(300,90);
					lgv.setVisible(true);
					lgv.setLocationRelativeTo(null);
					lgv.setResizable(false);
					lgv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//logout();
			}
		});
		curUser = userID;
		userJMB = new MenuBar(this);
		((MenuBar) userJMB).refreshAlbumList();
		setJMenuBar(userJMB);
		albumMode();
	}

	/**
	 * Puts the User View window into album viewing mode.
	 * Displays the list of albums and options you have available with
	 * each album that the user has.
	 */
	public void albumMode() {
		if(inPhotoMode == true){
			remove(leftPanel);
			remove(rightPanel);
		}
		
		inPhotoMode = false;
		inSearchViewMode = false;
		inAlbumListMode = true;
		((MenuBar) userJMB).refreshAlbumList();

		createAlbumJList();
		createAlbumLabelPanel(0);
		createAlbumButtonPanel();

		leftPanel = new JPanel(new GridLayout(3, 1, 20, 20));
		leftPanel.add(albumLabelPanel);
		leftPanel.add(new JPanel());
		leftPanel.add(albumButtonPanel);
		
		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(albumsJL);
		Border othertitle = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.BLACK), " Albums: ");
		((TitledBorder)othertitle).setTitleFont(titleFont);
		rightPanel.setBorder(BorderFactory.createCompoundBorder
			(BorderFactory.createEmptyBorder(5,10,3,10),
				BorderFactory.createCompoundBorder(othertitle, 
					BorderFactory.createEmptyBorder(5,5,5,5))));

		setLayout(new GridLayout(1,2));
		add(leftPanel);
		add(rightPanel);
	}

	/**
	 * Puts the User View into photo viewing mode. Displays the list of photos 
	 * that is contained in the album that was selected from album viewing mode 
	 * along with the available options you can perform with each of the photos.
	 * @param albumIndex used for album information and what photos to display
	 * @throws IOException
	 */
	public void photoMode(int albumIndex) throws IOException {
		currentAlbumObject = albums[albumIndex];
		((MenuBar) userJMB).refreshPhotoList(currentAlbumObject);
		
		inPhotoMode = true;
		remove(leftPanel);
		remove(rightPanel);

		createPhotoPanel(albumIndex);
		createAlbumLabelPanel(albumIndex);
		createPhotoLabelPanel();
		createPhotoButtonPanel();

		leftPanel = new JPanel(new GridLayout(3, 1, 20, 20));
		leftPanel.add(albumLabelPanel);
		leftPanel.add(photoLabelPanel);
		leftPanel.add(photoButtonPanel);

		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(BorderLayout.CENTER, photoSP);
		rightPanel.add(BorderLayout.SOUTH, backJB);
		
		Border othertitle = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.BLACK), " Album Contents: ");
		((TitledBorder)othertitle).setTitleFont(titleFont);
		rightPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(5,10,3,10),
				BorderFactory.createCompoundBorder(othertitle, 
					BorderFactory.createEmptyBorder(5,5,5,5))));
		
		add(leftPanel);
		add(rightPanel);

		validate();
		repaint();
	}

	/**
	 * Puts the User View into photo display mode. Displays the photo that was 
	 * selected from photo viewing mode. 
	 * @param albumIndex used for album information
	 * @throws IOException
	 */
	public void photoDisplayMode(int albumIndex) throws IOException {
		inPhotoMode = false;
		remove(leftPanel);
		remove(rightPanel);
		
		
		((MenuBar) userJMB).disableAlbum();
		//userJMB.albumJM.setEnabled(false);

		createPhotoViewPanel();
		createAlbumLabelPanel(albumIndex);
		createPhotoLabelPanel();

		leftPanel = new JPanel(new GridLayout(2, 1));
		leftPanel.add(albumLabelPanel);
		leftPanel.add(photoLabelPanel);

		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(BorderLayout.CENTER, photoSP);
		rightPanel.add(BorderLayout.SOUTH, backJB);

		add(leftPanel);
		add(rightPanel);
	
		invalidate();
		validate();
		repaint();
	}

	/*
	 * 
	 * 
	 * Album View Methods
	 * 
	 * 
	 */

	/**
	 * Constructs the JList with the names of the User's current albums.
	 */
	private void createAlbumJList() {	
		listModel = new DefaultListModel();
		albums = control.listAlbums();

		String[] albumNames = new String[albums.length];
		//System.out.println("Albums for user " + curUser + ":");
		{
			for (int i = 0; i < albums.length; i++){
				listModel.addElement(albums[i].albumName);
				albumNames[i] = albums[i].albumName;

				if (albums[i].getPhotoList().size() == 0){
					//System.out.print(albums[i].albumName + " number of photos: 0\n");
				} else {
					//System.out.print(albums[i].albumName + " number of photos: " + albums[i].getPhotoList().size() + ", " 
					//		+ albums[i].start.toString() + " - " + albums[i].end.toString() + "\n");
				}
			}
		}

		albumsJL = new JList(listModel);
		albumsJL.setModel(listModel);
		albumsJL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		albumsJL.setSelectedIndex(0);
		albumsJL.addListSelectionListener(this);

	}

	/**
	 * updates the JList whenever changes are made to it (add/delete).
	 */
	private void updateAlbumJList() {
		listModel = new DefaultListModel();
		albums = control.listAlbums();
		for (int i = 0; i < albums.length; i++){
			listModel.addElement(albums[i].albumName);
		}
		albumsJL.setModel(listModel);
		albumsJL.setSelectedIndex(0);
	}

	/**
	 * Constructs the appropriate labels for the currently selected album
	 * @param currentAlbum the index of the current album
	 */
	private void createAlbumLabelPanel(int currentAlbum) {
		albumLabelPanel = new JPanel(new GridLayout(5,1));

		anameJL = new JLabel("Name: ");
		anameJL.setFont(headerFont);
		numPhotosJL = new JLabel("Number of Photos: ");
		numPhotosJL.setFont(headerFont);
		startJL = new JLabel("Start: ");
		startJL.setFont(headerFont);
		endJL = new JLabel("End: ");
		endJL.setFont(headerFont);

		currentAlbum = albumsJL.getSelectedIndex();
		
		if (currentAlbum == -1) {
			albumNameJL = new JLabel("...");
			albumNumPhotosJL = new JLabel("...");
			albumStartJL = new JLabel("...");
			albumEndJL = new JLabel("...");
		} else if (albums[currentAlbum].getPhotoList().size() == 0)  {
			albumNameJL = new JLabel(albums[currentAlbum].albumName);
			albumNumPhotosJL = new JLabel(albums[currentAlbum].getPhotoList().size() + "");
			albumStartJL = new JLabel("...");
			albumEndJL = new JLabel("...");
		} else {
			albumNameJL = new JLabel(albums[currentAlbum].albumName);
			albumNumPhotosJL = new JLabel(albums[currentAlbum].getPhotoList().size() + "");
			albumStartJL = new JLabel(albums[currentAlbum].start.toString());
			albumEndJL = new JLabel(albums[currentAlbum].end.toString());
		}

		JPanel nameinfo = new JPanel(new FlowLayout());
		nameinfo.add(anameJL);
		nameinfo.add(albumNameJL);
		albumLabelPanel.add(nameinfo);
		
		JPanel numphotosinfo = new JPanel(new FlowLayout());
		numphotosinfo.add(numPhotosJL);
		numphotosinfo.add(albumNumPhotosJL);
		albumLabelPanel.add(numphotosinfo);
		
		JPanel startinfo = new JPanel(new FlowLayout());
		startinfo.add(startJL);
		startinfo.add(albumStartJL);
		albumLabelPanel.add(startinfo);
		
		JPanel endinfo = new JPanel(new FlowLayout());
		endinfo.add(endJL);
		endinfo.add(albumEndJL);
		albumLabelPanel.add(endinfo);
		
		albumNameJL.setFont(entryFont);
		albumNumPhotosJL.setFont(entryFont);
		albumStartJL.setFont(entryFont);
		albumEndJL.setFont(entryFont);
		
		Border titleborder;
		titleborder = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.BLACK), " Album Detail: ");
		((TitledBorder) titleborder).setTitleJustification(TitledBorder.LEFT);
		((TitledBorder) titleborder).setTitleFont(titleFont);
		Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
		albumLabelPanel.setBorder(BorderFactory.createCompoundBorder(empty, titleborder));
	}

	/**
	 * Updates the album labels if the current album selected changes.
	 *
	 */
	private void updateAlbumLabelPanel() {
		String albumname = (String) albumsJL.getSelectedValue();
		Album album = control.backend.getAlbum(albumname);
		albumNameJL.setText(albumname);

		if(album == null){
			albumNumPhotosJL.setText("");
			albumStartJL.setText("");
			albumEndJL.setText("");
		}else if(album.getPhotoList().size() == 0) {
			albumNumPhotosJL.setText("0");
			albumStartJL.setText("...");
			albumEndJL.setText("...");
		} else {
			albumNumPhotosJL.setText(album.getPhotoList().size() + "");
			albumStartJL.setText(album.start.toString());
			albumEndJL.setText(album.end.toString());
		}
	}

	/**
	 * Constructs the appropriate buttons for use with an album (open, create, delete, rename).
	 */
	private void createAlbumButtonPanel() {
		albumButtonPanel = new JPanel(new GridLayout(2, 2, 20, 20));

		openJB = new JButton("Open");
		createJB = new JButton("Create");
		deleteJB = new JButton("Delete");
		renameJB = new JButton("Rename");

		albumButtonPanel.add(openJB);
		albumButtonPanel.add(createJB);
		albumButtonPanel.add(deleteJB);
		albumButtonPanel.add(renameJB);
		
		openJB.addActionListener(this);
		createJB.addActionListener(this);
		deleteJB.addActionListener(this);
		renameJB.addActionListener(this);
		
		Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
		Border line = BorderFactory.createLineBorder(Color.BLACK);
		albumButtonPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(empty, line),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		
	}


	/*
	 * 
	 * 
	 * Photo View Methods
	 * 
	 * 
	 */
	/**
	 * Constructs the JPanel that'll display the photos and their captions
	 * @param albumIndex the album where the photos come from
	 * @throws IOException
	 */
	private void createPhotoPanel(int albumIndex) throws IOException {
		String[] photos = control.listPhotos(curAlbum);

		if (albums[albumIndex].getPhotoList().size() > 0) {
			photoPanel = new JPanel(new GridLayout(5, albums[albumIndex].getPhotoList().size()));
			
			for(int i = 0; i < photos.length; i++) {
				String[] info = control.listPhotoInfo(photos[i]); 
				//0 = filename, 1 = caption, 2 = albums, 3 = date, 4 = tags
				BufferedImage myPicture;
				try {
					myPicture = ImageIO.read(new File("data" + File.separator + photos[i]));
				} catch (IIOException iioe) {
					myPicture = ImageIO.read(new File("data\\default.png"));
					invalidImage = true;
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "One or more images are missing.");
				}

				ImageIcon icon = new ImageIcon(myPicture, info[1]);
				ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 64, 64));
				JLabel picLabel = new JLabel(info[0], thumbnailIcon, JLabel.CENTER);
				picLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						JLabel curLabel = (JLabel) me.getSource();
						curPhoto = control.listPhotoInfo(curLabel.getText());
						for(int i = 0; i < curPhoto.length; i++) {
							//System.out.println(curPhoto[i]);
						}
						updatePhotoLabelPanel();
					}
				});
				photoPanel.add(picLabel);
			}
			curPhoto = control.listPhotoInfo(photos[0]);
		} else {
			photoPanel = new JPanel(new GridLayout(1,1));
			JLabel none = new JLabel("No photos in Album");
			photoPanel.add(none);
		}

		photoSP = new JScrollPane(photoPanel);
	}

	/**
	 * Constructs the appropriate labels for the currently selected photo
	 */
	private void createPhotoLabelPanel() {
		photoLabelPanel = new JPanel(new GridLayout(5,1));

		pnameJL = new JLabel("Name: ");
		pnameJL.setFont(headerFont);
		palbumsJL = new JLabel("Albums: ");
		palbumsJL.setFont(headerFont);
		captionJL = new JLabel("Caption: ");
		captionJL.setFont(headerFont);
		dateJL = new JLabel("Date: ");
		dateJL.setFont(headerFont);
		tagsJL = new JLabel("Tags: ");
		tagsJL.setFont(headerFont);

		if (curPhoto == null) {
			photoNameJL = new JLabel("...");
			photoAlbumsJL = new JLabel("...");
			photoCaptionJL = new JLabel("...");
			photoDateJL = new JLabel("...");
			photoTagsJL = new JLabel("...");
		} else {
			photoNameJL = new JLabel(curPhoto[0]);
			photoAlbumsJL = new JLabel(curPhoto[2]);
			photoCaptionJL = new JLabel(curPhoto[1]);
			photoDateJL = new JLabel(curPhoto[3]);
			photoTagsJL = new JLabel(curPhoto[4].length() < 40? curPhoto[4] : curPhoto[4].substring(0, 35) + "...");
		}
		
		photoNameJL.setFont(entryFont);
		photoAlbumsJL.setFont(entryFont);
		photoCaptionJL.setFont(entryFont);
		photoDateJL.setFont(entryFont);
		photoTagsJL.setFont(entryFont);
		
		JPanel jp1, jp2, jp3, jp4, jp5;
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();
		
		jp1.add(pnameJL);
		jp1.add(photoNameJL);
		
		jp2.add(palbumsJL);
		jp2.add(photoAlbumsJL);
		
		jp3.add(captionJL);
		jp3.add(photoCaptionJL);
		
		jp4.add(dateJL);
		jp4.add(photoDateJL);
		
		jp5.add(tagsJL);
		jp5.add(photoTagsJL);
		
		photoLabelPanel.add(jp1);
		photoLabelPanel.add(jp2);
		photoLabelPanel.add(jp3);
		photoLabelPanel.add(jp4);
		photoLabelPanel.add(jp5);
		
		Border titleborder;
		titleborder = BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(Color.BLACK), " Photo Detail: ");
		((TitledBorder) titleborder).setTitleJustification(TitledBorder.LEFT);
		((TitledBorder) titleborder).setTitleFont(titleFont);
		Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
		photoLabelPanel.setBorder(BorderFactory.createCompoundBorder(empty, titleborder));

	}

	/**
	 * Updates the photo labels if the current photo selected changes.
	 */
	private void updatePhotoLabelPanel() {
		if (curPhoto == null) {
			photoNameJL.setText("...");
			photoAlbumsJL.setText("...");
			photoCaptionJL.setText("...");
			photoDateJL.setText("...");
			photoTagsJL.setText("...");
		} else {
			photoNameJL.setText(curPhoto[0]);
			photoAlbumsJL.setText(curPhoto[2]);
			photoCaptionJL.setText(curPhoto[1]);
			photoDateJL.setText(curPhoto[3]);
			photoTagsJL.setText(curPhoto[4].length() < 40? curPhoto[4] : curPhoto[4].substring(0, 35) + "...");
		}
	}

	/**
	 * Constructs the JButtons and adds them to the photo button panel
	 * 
	 */
	private void createPhotoButtonPanel() {
		photoButtonPanel = new JPanel(new GridLayout(3,1,10,10));

		addJB = new JButton("Add");
		delJB = new JButton("Delete");
		editJB = new JButton("Edit");
		moveJB = new JButton("Move");
		viewJB = new JButton("View");
		backJB = new JButton("Back");

		JPanel first = new JPanel(new GridLayout(1,2, 20, 20));
		JPanel second = new JPanel(new GridLayout(1,2,20 ,20));
		JPanel third = new JPanel(new GridLayout(1,1));
		
		first.add(addJB);
		first.add(delJB);
		second.add(editJB);
		second.add(moveJB);
		third.add(viewJB);
		
		photoButtonPanel.add(first);
		photoButtonPanel.add(second);
		photoButtonPanel.add(third);
		
		third.setBorder(BorderFactory.createEmptyBorder(0,100, 0, 100));
		
		Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
		Border line = BorderFactory.createLineBorder(Color.BLACK);
		photoButtonPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(empty, line),
				BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		

		addJB.addActionListener(this);
		delJB.addActionListener(this);
		editJB.addActionListener(this);
		moveJB.addActionListener(this);
		viewJB.addActionListener(this);
		backJB.addActionListener(this);
	}

	/*
	 * 
	 * 
	 * Photo Display Methods
	 * 
	 * 
	 */

	/**
	 * Constructs a new JPanel displaying the selected photo.
	 * @throws IOException
	 */
	public void createPhotoViewPanel() throws IOException {
		JLabel picLabel;
		if (curPhoto == null) {
			picLabel = new JLabel("No photo selected.");
		} else {
			BufferedImage myPicture;

			if (invalidImage)
				myPicture = ImageIO.read(new File("data\\default.png"));
			else
				myPicture = ImageIO.read(new File("data\\" + curPhoto[0]));

			ImageIcon icon = new ImageIcon(myPicture, curPhoto[1]);
			ImageIcon resizedImage = new ImageIcon(getScaledImage(icon.getImage(), 512, 512));
			picLabel = new JLabel("", resizedImage, JLabel.LEFT);
		}
		photoPanel = new JPanel(new BorderLayout());
		photoPanel.add(picLabel);
		photoSP = new JScrollPane(photoPanel);
	}

	/*
	 * 
	 * 
	 * Miscellaneous Methods
	 *
	 *
	 */

	/**
	 * Gives all of the JButtons actions to perform when clicked
	 */
	public void actionPerformed(ActionEvent e) {
		//*************************************JMenuItem Actions*****************************************//
		if (e.getSource() == saveJMI) {
			//System.out.println("logout");
			control.saveData();
			dispose();
		}

		//*************************************Album Actions*****************************************//
		if (e.getSource() == createJB) {
			createAlbum();
		}

		if (e.getSource() == openJB) {
			curAlbum = (String) albumsJL.getSelectedValue();
			if (curAlbum != null)
				viewAlbum(curAlbum);
			else {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "No albums to open.");
			}
			
		}

		if (e.getSource() == deleteJB) {
			String albumName = (String) albumsJL.getSelectedValue();
			if (albumName != null)
				deleteAlbum(albumName);
			else {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "No albums to delete.");
			}
		}

		if (e.getSource() == renameJB) {
			JFrame frame = new JFrame();
			if (control.backend.user.userAlbum.size() != 0) {
				String albumName = JOptionPane.showInputDialog(frame, "Please enter new album name:");
				if (albumName != null && (!(albumName.equals("")))) {
					if(control.backend.userHasAlbum(albumName)){
						JFrame frame1 = new JFrame("Error");
						JOptionPane.showMessageDialog(frame1, "Album already exists.");
					} else{ 
						control.backend.user.userAlbum.get(albumsJL.getSelectedIndex()).rename(albumName);
						updateAlbumJList();
						((MenuBar) userJMB).refreshAlbumList();
						albumsJL.setSelectedValue(albumName, true);
					}
				}
				
			} else {
				JOptionPane.showMessageDialog(frame, "No albums to rename.");
			}
		}

		//*************************************Photo Actions*****************************************//
		
		if (e.getSource() == addJB) {
			addPhoto();
		}
		
		if (e.getSource() == delJB) {
			if (curPhoto != null) {
				deletePhoto(UserView.this.curPhoto[0]);
			} else {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "You have no photos to delete.");
			}
		}
		
		if (e.getSource() == editJB) {
			if (curPhoto != null){
				EditPhotoDialog edit = new EditPhotoDialog(UserView.this, curPhoto[0], (ArrayList<Tag>) control.backend.getPhoto(curPhoto[0]).tagList.clone());
				updatePhotoLabelPanel();
				invalidate();
				validate();
				repaint();
			} else {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame,  "You have no photos to edit.");
			}
		}
		
		if (e.getSource() == moveJB) {
			if (curPhoto != null) {
				RelocatePhotoDialog move = new RelocatePhotoDialog(UserView.this, curPhoto[0], currentAlbumObject.albumName);
				move.addWindowListener(new WindowAdapter(){
					public void windowClosed(WindowEvent e){
						try {
							photoMode(currentAlbum);
							invalidate();
							validate();
							repaint();
						} catch (IOException E) {
							// TODO Auto-generated catch block
							E.printStackTrace();
						}
					}
				});
			} else {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame,  "You have no photos to move.");
			}
		}

		if (e.getSource() == viewJB) {
			try {
				photoDisplayMode(currentAlbum);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			invalidate();
			validate();
			repaint();
		}
		
		if (e.getSource() == backJB) {
			remove(leftPanel);
			remove(rightPanel);
			if (inPhotoMode == true) {
				albumMode();
				curPhoto = null;
				updatePhotoLabelPanel();
			} else {
				try {
					photoMode(currentAlbum);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			invalidate();
			validate();
			repaint();
		}
	}

	/**
	 * Used for determining which album is selected and updates the album labels.
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (albumsJL.getSelectedValue() == null)
			albumsJL.setSelectedIndex(0);
		updateAlbumLabelPanel();
	}

	/**
	 * Method used for image resizing and scaling
	 * @param srcImg source image to be scaled
	 * @param w width of the new scaled image
	 * @param h height of the new scaled image
	 * @return the resized image
	 */
	private Image getScaledImage(Image srcImg, int w, int h){
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	/* menu methods */

	/**
	 * logs off and saved the data, closes the window.
	 */
	public void logout(){
		control.saveData();
		dispose();
	}

	/**
	 * Deletes the user's specified album
	 * @param albumName name of the album to be deleted
	 */
	public void deleteAlbum(String name){
		boolean b = control.removeAlbum(name);
		if (!b){
			JFrame frame = new JFrame("Error");
			JOptionPane.showMessageDialog(frame, "Album could not be deleted.");
		} else{
			updateAlbumJList();
			((MenuBar) userJMB).refreshAlbumList();
		}
	}
	
	/**
	 * Creates a new album for the current user
	 * @param albumName name of the new album
	 */
	public void createAlbum(){
		JFrame frame = new JFrame();
		String albumName = JOptionPane.showInputDialog(frame, "Enter album name:");
		
		if (albumName != null) {
			while (albumName.trim().length() == 0){
				JFrame frame1 = new JFrame("Error");
				JOptionPane.showMessageDialog(frame1, "Album name is empty.");
				albumName = JOptionPane.showInputDialog(frame, "Enter album name:");
			}
	
			boolean b = control.addAlbum(albumName);
			
			if (!b){
				JFrame frame1 = new JFrame("Error");
				JOptionPane.showMessageDialog(frame1, "Album already exists.");
			} else {
				updateAlbumJList();
				albumsJL.setSelectedIndex(albumsJL.getModel().getSize() - 1);
				((MenuBar) userJMB).refreshAlbumList();
				if (inPhotoMode)
					albumMode();
				
			}
		}
	}
	
	/**
	 * Renames the specified album
	 * @param name name of the album to be renamed
	 */
	public void renameAlbum(String name){
		JFrame frame = new JFrame();
		String albumName = JOptionPane.showInputDialog(frame, "Please enter new album name:");
		if(control.backend.userHasAlbum(albumName)){
			JFrame frame1 = new JFrame("Error");
			JOptionPane.showMessageDialog(frame1, "Album already exists.");
		} else{ 
			control.backend.user.userAlbum.get(albumsJL.getSelectedIndex()).rename(albumName);
			albumsJL.setSelectedValue(name, true);
			updateAlbumJList();
			albums = control.listAlbums();
			((MenuBar) userJMB).refreshAlbumList();
			if (inPhotoMode){
				albumMode();
			}
		}
	}
	
	/**
	 * Views the specified album
	 * @param name name of the album to be viewed
	 */
	public void viewAlbum(String name){
		albumsJL.setSelectedValue(name, true);
		currentAlbum = albumsJL.getSelectedIndex();
		curAlbum = (String) albumsJL.getSelectedValue();
		try {
			photoMode(currentAlbum);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Displays AddPhotoDialog window.
	 */
	public void addPhoto(){
		AddPhotoDialog addphoto = new AddPhotoDialog(this);
		addphoto.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				try {
					photoMode(currentAlbum);
					invalidate();
					validate();
					repaint();
				} catch (IOException E) {
					// TODO Auto-generated catch block
					E.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * Displays slideshow window
	 */
	public void slideshow(){
		Slideshow slides = new Slideshow(currentAlbumObject, 0);
	}
	
	/**
	 * Deletes a photo from an album
	 * @param filename file name of the photo
	 */
	public void deletePhoto(String filename){
		if (!control.backend.removePhoto(filename, curAlbum)){
			JFrame frame1 = new JFrame("Error");
			JOptionPane.showMessageDialog(frame1, "Photo could not be removed.");
		} else{
			try {
				photoMode(currentAlbum);
				invalidate();
				validate();
				if (currentAlbumObject.getPhotoList().size() == 0){
					curPhoto = null;
					updatePhotoLabelPanel();
				}
				repaint();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * displays SearchByTag Dialog window
	 */
	public void searchByTag(){
		SearchByTag sbt = new SearchByTag(this, null);
	}
	
	/**
	 * displays SearchByDate Dialog window
	 */
	public void searchByDate(){
		SearchByDate sbd = new SearchByDate(this);
	}
	
	/**
	 * Gets a list of photos based on tags
	 * @param tags the specified tag(s)
	 * @return an arraylist of photos
	 */
	public ArrayList<Photo> getByTags(ArrayList<Tag> tags){
		Photo[] all = control.backend.getAllPhotos();
		ArrayList<Photo> found = new ArrayList<Photo>();
		
		for (int i = 0; i < tags.size(); i++){ //loops over all tags
			if (tags.get(i).type == null || tags.get(i).type.length() == 0){
				//typeless
				for (int j = 0; j < all.length; j++){
					//over all photos
					for (int k = 0; k < all[j].tagList.size(); k++){
						//over all tags in photo
						if(all[j].tagList.get(k).value.equalsIgnoreCase(tags.get(i).value)){
							if (!found.contains(all[j])){
								found.add(all[j]);
							}
						}
					}
				}
			} else{
				//typed
				for (int j = 0; j < all.length; j++){
					//loops over all photos
					if (all[j].tagList.contains(tags.get(i))){
						if (!found.contains(all[j])){
							found.add(all[j]);
						}
					}
				}
			}			
		}
		return found;
	}
	
	/**
	 * Gets a list of photos based on date
	 * @param start start date
	 * @param end end date
	 * @return an arraylist of photos
	 */
	public ArrayList<Photo> getByDate(Calendar start, Calendar end){
		Photo[] all = control.backend.getAllPhotos();
		ArrayList<Photo> found = new ArrayList<Photo>();
		
		for (int i = 0; i < all.length; i++){
			//over all photos
			if (all[i].cal.after(start) && all[i].cal.before(end)){
				found.add(all[i]);
			}
		}
		return found;
	}

	/**
	 * Displays the SearchResults Dialog window
	 * @param results results used for the window
	 * @throws IOException
	 */
	public void displayResults(ArrayList<Photo> results) throws IOException {
		SearchResults res = new SearchResults(this, results);
		this.setVisible(false);
		res.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				UserView.this.setVisible(true);
			}
		});
		
	}

}
