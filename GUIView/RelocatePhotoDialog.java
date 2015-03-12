package cs213.photoAlbum.GUIView;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoAlbum.model.Album;

/**
 * Dialog class that uses a JFrame to determine where a photo is relocated to.
 * @author Brent
 *
 */
public class RelocatePhotoDialog extends JFrame {

	String fileName, oldAlbum;
	UserView userView;
	JLabel errorText;
	JButton acknowledge;
	JPanel main, error;
	JComboBox spinner;

	/**
	 * constructor that initializes the JFrame based on the parameters
	 * @param view the current UserView
	 * @param filename the filename of the current photo
	 * @param old the old album from which the photo is moving from
	 */
	public RelocatePhotoDialog(UserView view, String filename, String old){
		super("Move Photo");
		this.userView = view;
		this.fileName = filename;
		this.oldAlbum = old;

		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(new Dimension(400, 125));

		main = new JPanel(new GridLayout(2,1));
		error = new JPanel(new FlowLayout());
		
		JPanel first, second;
		first = new JPanel(new FlowLayout());
		second = new JPanel(new FlowLayout());

		//setLayout(new FlowLayout());

		Album[] albums = userView.control.listAlbums();
		String[] names = new String[albums.length];

		for (int i = 0; i < albums.length; i++){
			names[i] = albums[i].albumName;
		}

		spinner = new JComboBox(names);
		
		spinner.setSelectedItem(oldAlbum);

		JLabel label = new JLabel("Move photo " + fileName + " from album " + oldAlbum + " to: ");
		JButton move = new JButton("Move");
		JButton cancel = new JButton("Cancel");

		first.add(label);
		first.add(spinner);
		second.add(move);
		second.add(cancel);
		main.add(first);
		main.add(second);
		add(main);

		errorText = new JLabel("The photo is already present in that album.");
		acknowledge = new JButton("Continue");

		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RelocatePhotoDialog.this.dispose();
			}
		});

		//COPIED
		error = new JPanel(new GridLayout(2,1));
		JPanel emess = new JPanel(new FlowLayout());
		emess.add(errorText);
		error.add(emess);
		JPanel ebutton = new JPanel(new FlowLayout());
		ebutton.add(acknowledge);
		//acknowledge.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
		error.add(ebutton);
		error.setBorder(BorderFactory.createEmptyBorder(5, 20, 1 ,20));
		acknowledge.setPreferredSize(new Dimension(150, 30));

		acknowledge.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RelocatePhotoDialog.this.remove(RelocatePhotoDialog.this.error);
				RelocatePhotoDialog.this.add(RelocatePhotoDialog.this.main);
				RelocatePhotoDialog.this.invalidate();
				RelocatePhotoDialog.this.validate();
				repaint();

			}
		});
		//END COPIED
		move.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (((String) spinner.getSelectedItem()).equals(oldAlbum)){
					RelocatePhotoDialog.this.remove(main);
					RelocatePhotoDialog.this.add(error);
					invalidate();
					validate();
					repaint();
				} else{
					if (!RelocatePhotoDialog.this.userView.control.backend.addPhoto(fileName, "", (String)spinner.getSelectedItem())){
						RelocatePhotoDialog.this.remove(main);
						RelocatePhotoDialog.this.add(error);
						invalidate();
						validate();
						repaint();
						return;
					}
					RelocatePhotoDialog.this.userView.control.backend.removePhoto(fileName, oldAlbum);
					RelocatePhotoDialog.this.dispose();
				}
			}
		});


	}
}
