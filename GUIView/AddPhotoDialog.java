package cs213.photoAlbum.GUIView;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.model.Album;

/**
 * Dialog frame used for adding a photo to an album.
 * @author Brent
 *
 */
public class AddPhotoDialog extends JFrame {

	UserView view;
	JTextField filenameTF, captionTF;
	Album a;
	JPanel error, main;
	JLabel errorText;
	
	/**
	 * Constructor that takes in the current UserView and initializes
	 * the frame with the contents within that view
	 * @param view the UserView to be passed into the Dialog
	 */
	public AddPhotoDialog(UserView view){
		super("Add photo to album: " + view.currentAlbumObject.albumName);
		this.view = view;
		this.a = view.currentAlbumObject;
		
		main = new JPanel();
		
		main.setLayout(new GridLayout(3,1));
		
		JPanel first = new JPanel();
		JPanel second = new JPanel();
		JPanel third = new JPanel();
		
		JLabel filename = new JLabel("Filename: ");
		JLabel caption = new JLabel("Caption:   ");
		
		filenameTF = new JTextField();
		filenameTF.setPreferredSize(new Dimension(200, 20));
		captionTF = new JTextField();
		captionTF.setPreferredSize(new Dimension(200, 20));
		
		JButton add = new JButton("Add");
		JButton cancel = new JButton("Cancel");
		
		main.add(first);
		main.add(second);
		main.add(third);
		
		first.add(filename);
		first.add(filenameTF);
		
		second.add(caption);
		second.add(captionTF);
		
		third.add(add);
		third.add(cancel);
		
		this.setSize(350, 150);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		add(main);
		JButton acknowledge = new JButton("Continue");
		error = new JPanel(new GridLayout(5,2));
		errorText = new JLabel();
		for (int i = 0; i < 9; i++){
			if (i == 4){
				error.add(errorText);
			} else if (i == 5){
				error.add(acknowledge);
			} else{
				error.add(new JLabel());
			}
		}
		error.setBorder(BorderFactory.createEmptyBorder(10, 20, 10 ,20));
		acknowledge.setPreferredSize(new Dimension(150, 30));
		
		acknowledge.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				AddPhotoDialog.this.remove(AddPhotoDialog.this.error);
				AddPhotoDialog.this.add(AddPhotoDialog.this.main);
				AddPhotoDialog.this.invalidate();
				AddPhotoDialog.this.validate();
				repaint();
				
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				AddPhotoDialog.this.dispose();
			}
		});
		
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String filename = AddPhotoDialog.this.filenameTF.getText();
				String caption = AddPhotoDialog.this.captionTF.getText();
				if (filename.length() == 0){
					return;
				}
				
				if (new File("data"+ File.separator + filename).exists()){
					if(AddPhotoDialog.this.view.control.backend.addPhoto(filename, caption, a.albumName)){
						AddPhotoDialog.this.dispose();
					} else{
						AddPhotoDialog.this.remove(AddPhotoDialog.this.main);
						AddPhotoDialog.this.add(AddPhotoDialog.this.error);
						AddPhotoDialog.this.errorText.setText("That file cannot be added.");
						
						invalidate();
						validate();
						repaint();
					}
					
				} else {
					AddPhotoDialog.this.remove(AddPhotoDialog.this.main);
					AddPhotoDialog.this.add(AddPhotoDialog.this.error);
					AddPhotoDialog.this.errorText.setText("That file cannot be found.");
					
					invalidate();
					validate();
					repaint();
					
				}
				
			}
		});
		
	}
}
