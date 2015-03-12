package cs213.photoAlbum.GUIView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.Tag;

/**
 * Dialog that uses a JFrame to edit the attributes of a photo
 * @author Brent
 *
 */
public class EditPhotoDialog extends JFrame {

	Tag newTag;
	UserView view;
	Photo pic;
	String fileName;
	String captionText;
	ArrayList<Tag> tags;
	JPanel bottom, top, right, left, caption, tag;
	JButton addTag, removeTag, save, cancel;
	JLabel cap, tg;
	JTable tagTable;
	Tag selectedTag;
	JTextField capText;
	JScrollPane tagTableSP;
	DefaultTableModel tableMod;
	
	/**
	 * Constructor that initializes the contents of the dialog to that of its parameters
	 * @param userView contents of the current UserView
	 * @param filename filename of the current photo
	 * @param copy copy of the tags of the current photo
	 */
	public EditPhotoDialog(UserView userView, String filename, ArrayList<Tag> copy){
		super("Edit " + filename);
		tags = copy;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.view = userView;
		this.fileName = filename;
		
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setSize(400, 300);
		this.setVisible(true);
		
		initialize();
		place();
		assignListeners();
	}
	
	/**
	 * Constructs the actual layout of the dialog 
	 */
	private void initialize(){
		pic = view.control.backend.getPhoto(fileName);
		captionText = pic.caption;
		
		setLayout(new FlowLayout());
		
		capText = new JTextField(captionText);
		capText.setPreferredSize(new Dimension(235, 20));
		
		cap = new JLabel("Caption: ");
		cap.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		tg = new JLabel("Tags: ");
		tg.setBorder(BorderFactory.createEmptyBorder(4,13,145,17));
		
		addTag = new JButton("Add Tag");
		removeTag = new JButton("<html><body>Remove<br>  Tag</body></html>");
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		
		bottom = new JPanel(new FlowLayout());
		top = new JPanel(new BorderLayout());
		right = new JPanel(new FlowLayout());
		right.setBorder(BorderFactory.createEmptyBorder(85,0,0,0));
		left = new JPanel(new FlowLayout());
		caption = new JPanel(new FlowLayout());
		tag = new JPanel(new BorderLayout());
		
		String[] headers = {"Type", "Value"};
		DefaultTableModel tableMod = new DefaultTableModel(headers, tags.size() > 9 ? tags.size() : 9);
		for (int i = 0; i < tags.size(); i++){
			tableMod.setValueAt(tags.get(i).type, i, 0);
			tableMod.setValueAt(tags.get(i).value, i, 1);
		}
		
		tagTable = new JTable();
		tagTableSP = new JScrollPane(tagTable);
		
		tagTable.setModel(tableMod);
		tagTable.getColumnModel().getColumn(0).setResizable(false);
		tagTable.getColumnModel().getColumn(1).setResizable(false);
		tagTable.setRowSelectionAllowed(true);
		tagTable.getTableHeader().setReorderingAllowed(false);
		tagTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tagTableSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
	}
	
	/**
	 * places the contents of the layout onto the JFrame
	 */
	private void place(){
		
		bottom.setPreferredSize(new Dimension(400,50));
		top.setPreferredSize(new Dimension(400, 220));
		right.setPreferredSize(new Dimension(100, 220));
		left.setPreferredSize(new Dimension(300, 220));
		caption.setPreferredSize(new Dimension(300, 50));
		tag.setPreferredSize(new Dimension(300, 150));
		
		right.add(addTag);
		right.add(removeTag);
		
		bottom.add(save);
		bottom.add(cancel);
		caption.add(cap);
		caption.add(capText);
		tag.add(tg, BorderLayout.LINE_START);
		tag.add(tagTableSP, BorderLayout.CENTER);
		
		add(top);
		top.add(left);
		top.add(right, BorderLayout.LINE_END);
		left.add(caption, BorderLayout.PAGE_START);
		left.add(tag);
		add(bottom);
		
		tagTable.getSelectionModel().setSelectionInterval(0,0);
		
	}
	
	/**
	 * updates the tags if their were any changes
	 */
	private void updateTags(){
		String[] headers = {"Type", "Value"};
		tableMod = new DefaultTableModel(headers, tags.size() > 9 ? tags.size() : 9);
		for (int i = 0; i < tags.size(); i++){
			tableMod.setValueAt(tags.get(i).type, i, 0);
			tableMod.setValueAt(tags.get(i).value, i, 1);
		}
		
		tagTable.setModel(tableMod);
		tagTable.getColumnModel().getColumn(0).setResizable(false);
		tagTable.getColumnModel().getColumn(1).setResizable(false);
		tagTable.setRowSelectionAllowed(true);
		tagTable.getTableHeader().setReorderingAllowed(false);
		tagTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tagTableSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tagTable = new JTable();
		tagTableSP = new JScrollPane(tagTable);
		
		
//		invalidate();
//		validate();
		repaint();
		
	}
	
	/**
	 * Assigns actions to the JButtons in the JFrame
	 */
	private void assignListeners(){
//		tagTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//			public void valueChanged(ListSelectionEvent e){
//				selectedTag = new Tag((String) EditPhotoDialog.this.tableMod.getValueAt(tagTable.getSelectedRow(), 0), (String) EditPhotoDialog.this.tableMod.getValueAt(tagTable.getSelectedRow(), 1)); 
//			}
//		});
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				EditPhotoDialog.this.dispose();
			}
		});
		
		removeTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int row = tagTable.getSelectedRow();
				
				String type = (String) tagTable.getModel().getValueAt(row, 0);
				String value = (String) tagTable.getModel().getValueAt(row, 1);
				
				if (value != null) {
					Tag currenttag = new Tag(type, value);
					tags.remove(currenttag);
					EditPhotoDialog nextWindow = new EditPhotoDialog(view, fileName, tags);
					EditPhotoDialog.this.dispose();
				} else {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "No tags available or tag not selected.");
				}
				
//				updateTags();
////				invalidate();
////				validate();
//				EditPhotoDialog.this.repaint();
			}
		});
		
		addTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				AddTagDialog addTag = new AddTagDialog(EditPhotoDialog.this);
				addTag.addWindowListener(new WindowAdapter(){
					public void windowClosed(WindowEvent e){
						if (newTag == null){
							//System.out.println(newTag.toString());
							return;
						} else{
							tags.add(newTag);
							EditPhotoDialog nextWindow = new EditPhotoDialog(view, fileName, tags);
							EditPhotoDialog.this.dispose();
						}
					}
				});
			}
		});
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pic.tagList = tags;
				pic.caption = capText.getText();
				
				EditPhotoDialog.this.dispose();
				
			}
		});
	}
}
