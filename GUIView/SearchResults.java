package cs213.photoAlbum.GUIView;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cs213.photoAlbum.model.Photo;

/**
 * JFrame class that displays the results of a search by date or search by tag
 * into a dialog box with the option to create an album from the results.
 * @author Brent
 *
 */
public class SearchResults extends JFrame {

	UserView view;
	ArrayList<Photo> results;
	
	/**
	 * Initializes the JFrame dialog with the contents of the parameters
	 * @param userview the current user view
	 * @param results an array list of the photos that were obtained from the search
	 * @throws IOException
	 */
	public SearchResults(UserView userview, ArrayList<Photo> results) throws IOException{
		view = userview;
		this.results = results;
		
		this.setVisible(true);
		this.setSize(500,500);
		this.setResizable(true);
		this.setLocationRelativeTo(view);
		setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel(new FlowLayout());
		JPanel viewer = new JPanel(new FlowLayout());
		
		for(int i = 0; i < results.size(); i++) {
			//String[] info = control.listPhotoInfo(photos[i]); 
			//0 = filename, 1 = caption, 2 = albums, 3 = date, 4 = tags
			BufferedImage myPicture;

			try {
				myPicture = ImageIO.read(new File("data" + File.separator + results.get(i).filename));
			} catch (IIOException iioe) {
				myPicture = ImageIO.read(new File("data\\default.png"));
				//invalidImage = true;
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "One or more images are missing.");
			}

			ImageIcon icon = new ImageIcon(myPicture, results.get(i).filename);
			ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 64, 64));
			JLabel picLabel = new JLabel(results.get(i).filename, thumbnailIcon, JLabel.CENTER);
//			picLabel.addMouseListener(new MouseAdapter() {
//				public void mouseClicked(MouseEvent me) {
//					JLabel curLabel = (JLabel) me.getSource();
//					curPhoto = control.listPhotoInfo(curLabel.getText());
//					for(int i = 0; i < curPhoto.length; i++) {
//						System.out.println(curPhoto[i]);
//					}
//					updatePhotoLabelPanel();
//				}
//			});

			viewer.add(picLabel);
			//photoPanel.setPreferredSize(new Dimension(250, 500/12*photos.length));
		}
		add(viewer);
		
		JButton makealbum = new JButton("Create album from results");
		JButton cancel = new JButton("Back");
		
		buttons.add(makealbum);
		buttons.add(cancel);
		add(buttons, BorderLayout.PAGE_END);
		
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//SearchResults.this.view.albumMode();
				SearchResults.this.dispose();
			}
		});
		
		makealbum.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//long time = System.currentTimeMillis();
				String albumName = System.currentTimeMillis() + " search results";
				System.out.println(albumName);
				SearchResults.this.view.control.addAlbum(albumName);
				
				for (int i = 0; i < SearchResults.this.results.size(); i++){
					SearchResults.this.view.control.addPhoto(SearchResults.this.results.get(i).filename, SearchResults.this.results.get(i).caption, albumName);
					if (!SearchResults.this.view.inAlbumListMode){
						SearchResults.this.view.albumMode();
				}
					SearchResults.this.dispose();
				}
			}
		});
		
		
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
	
}
