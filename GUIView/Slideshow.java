package cs213.photoAlbum.GUIView;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoAlbum.model.Album;

/**
 * JFrame class used to display the images in a dialog window in slideshow form.
 * @author Brent
 *
 */
public class Slideshow extends JFrame {

	String[] photos;
	JPanel viewer;
	JPanel buttons;
	JButton next, previous;
	Album album;
	JLabel[] icons;
	int currentPic;

	/**
	 * Constructor that initializes the JFrame with the parameters
	 * @param currentAlbumObject the current album 
	 * @param currentpic the current picture index
	 */
	public Slideshow(Album currentAlbumObject, int currentpic) {
		super("Slideshow");
		currentPic = currentpic;
		album = currentAlbumObject;
		setSize(600, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		viewer = new JPanel();
		buttons = new JPanel(new GridLayout(1, 2, 200, 200));
		next = new JButton("Next");
		previous = new JButton("Previous");
		buttons.add(previous);
		buttons.add(next);
		buttons.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));

		setLayout(new BorderLayout());

		this.add(viewer, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.SOUTH);
		//setLayout(new FlowLayout());
		//		this.add(next);
		//		this.add(previous);
		setVisible(true);
		
		try {
			makePictures();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//currentPic = 1;
		
		if(icons.length == 0){
			viewer.add(new JLabel("No pictures selected."));
		} else{
			viewer.add(icons[currentPic]);
		}
		
		previous.addActionListener(new listener());
		next.addActionListener(new listener());
		
		if (currentPic == 0){
			previous.setEnabled(false);
		} if(currentPic == icons.length - 1){
			next.setEnabled(false);
		}
		
	}

	private void makePictures() throws IOException{
		icons = new JLabel[album.getPhotoList().size()];

		for (int i = 0; i < icons.length; i++){

			//JLabel picLabel;
			//String photoInfo = curPhoto[1] + "  " + curPhoto[2] + "  " + curPhoto[3] + "  " + curPhoto[4];
			BufferedImage myPicture;
			
			try{
				if (new File("data" + File.separator + album.getPhotoList().get(i).filename).exists())
					myPicture = ImageIO.read(new File("data" + File.separator + album.getPhotoList().get(i).filename));
				else
					myPicture = ImageIO.read(new File("data\\default.png"));
			} catch(FileNotFoundException e){
				myPicture = ImageIO.read(new File("data\\default.png"));
			} catch(IOException e){
				myPicture = ImageIO.read(new File("data\\default.png"));
			}

			ImageIcon icon = new ImageIcon(myPicture);
			ImageIcon resizedImage = new ImageIcon(getScaledImage(icon.getImage(), 512, 512));
			icons[i] = new JLabel("", resizedImage, JLabel.LEFT);

		}

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
	
	/**
	 * listener class that deals with the actions of previous and next jbuttons
	 * @author Brent
	 *
	 */
	public class listener implements ActionListener{
	
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == previous){
				Slideshow sldsh = new Slideshow(album, currentPic - 1);
				Slideshow.this.dispose();
//				currentPic--;
//				viewer = new JPanel();
//				viewer.add(icons[currentPic]);
//				viewer.repaint();
//				
//				previous.setEnabled(true);
//				next.setEnabled(true);
//				if (currentPic == 0){
//					previous.setEnabled(false);
//				} if(currentPic == icons.length - 1){
//					next.setEnabled(false);
//				}
			} else{
				Slideshow sldsh = new Slideshow(album, currentPic + 1);
				Slideshow.this.dispose();
			}
			
		}
	}
}
