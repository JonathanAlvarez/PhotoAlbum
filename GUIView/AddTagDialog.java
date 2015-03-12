package cs213.photoAlbum.GUIView;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.util.Tag;

/**
 * Dialog that uses a JFrame to add tags to photos.
 * Exclusive to EditPhotoDialog.
 * @author Brent
 *
 */
public class AddTagDialog extends JFrame {
	
	//create giving editphoto, and on add tag/close, write the new tag to newTag in editphoto
	
	EditPhotoDialog editPhoto;
	JTextField type, value;
	
	/**
	 * Constructor that initializes the JFrame to include the contents
	 * of editPhoto
	 * @param editPhoto the EditPhotoDialog that contains the contents
	 */
	public AddTagDialog(EditPhotoDialog editPhoto){
		this.editPhoto = editPhoto;
		
		this.setSize(300, 150);
		this.setLocationRelativeTo(editPhoto);
		this.setVisible(true);
		this.setResizable(false);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setLayout(new GridLayout(3,1));
		
		JPanel first, second, third;
		
		first = new JPanel(new FlowLayout());
		second = new JPanel(new FlowLayout());
		third = new JPanel(new FlowLayout());
		
		type = new JTextField();
		type.setPreferredSize(new Dimension(150, 20));
		value = new JTextField();
		value.setPreferredSize(new Dimension(150, 20));
		
		first.add(new JLabel("Tag type: "));
		second.add(new JLabel("Tag value: "));
		
		first.add(type);
		second.add(value);
		
		JButton add, cancel;
		
		add = new JButton("Add Tag");
		cancel = new JButton("Cancel");
		
		third.add(add);
		third.add(cancel);
		
		add(first);
		add(second);
		add(third);
		
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				AddTagDialog.this.dispose();
			}
		});
		
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (AddTagDialog.this.value.getText().length() != 0){				
				AddTagDialog.this.editPhoto.newTag = new Tag(AddTagDialog.this.type.getText(), AddTagDialog.this.value.getText());
				AddTagDialog.this.dispose();
				}
				
			}
		});
	}

}
