package cs213.photoAlbum.GUIView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.Tag;

/**
 * JFrame class used to search for photos based on their tags.
 * @author Brent
 *
 */
public class SearchByTag extends JFrame {

	ArrayList<Tag> tags;
	JTable table;
	DefaultTableModel tableMod;
	JScrollPane tagPane;
	JButton search, cancel, add, remove;
	JLabel tagl, valuel;
	JPanel bottom, top, right, left;
	protected Tag newTag;
	UserView view;
	
	/**
	 * Initializes the JFrame based on the contents of the parameters
	 * @param userview contents of the current userview
	 * @param prev contents of the tags
	 */
	public SearchByTag(UserView userview, ArrayList<Tag> prev){
		super("Search by tag");
		
		view = userview;
		
		if (prev == null || prev.size() == 0){
			tags = new ArrayList<Tag>();
		} else{
			tags = prev;
		}
		
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setSize(350, 220);
		this.setVisible(true);
		this.setLayout(new FlowLayout());
		
		initialize();
		setListeners();
	}
	
	/**
	 * Constructs and places the contents of the JFrame into a layout
	 */
	private void initialize(){
//		Border line = BorderFactory.createLineBorder(Color.black,3);		
		bottom = new JPanel(new FlowLayout());
		right = new JPanel(new FlowLayout());
		left = new JPanel(new BorderLayout());
		top = new JPanel(new BorderLayout());
		top.setPreferredSize(new Dimension(350, 150));
		bottom.setPreferredSize(new Dimension(300, 50));
		right.setPreferredSize(new Dimension(100, 150));
		left.setPreferredSize(new Dimension(235, 150));
		
		top.add(left, BorderLayout.LINE_START);
		top.add(right, BorderLayout.LINE_END);
		
		add(top);
		add(bottom);
		
//		top.setBorder(line);
//		bottom.setBorder(line);
//		left.setBorder(line);
//		right.setBorder(line);
		
		search = new JButton("Search");
		cancel = new JButton("Cancel");
		
		bottom.add(search);
		bottom.add(cancel);
		
		add = new JButton("Add Tag");
		remove = new JButton("<html><body>Remove<br>  Tag</body></html>");
		
		right.add(add);
		right.add(remove);
		tagl = new JLabel("Search tags: ");
		tagl.setBorder(BorderFactory.createEmptyBorder(0,3,105,0));
		left.add(tagl, BorderLayout.LINE_START);
		
		String[] headers = {"Type", "Value"};
		DefaultTableModel tableMod = new DefaultTableModel(headers, tags.size() > 9 ? tags.size() : 9);
		for (int i = 0; i < tags.size(); i++){
			tableMod.setValueAt(tags.get(i).type, i, 0);
			tableMod.setValueAt(tags.get(i).value, i, 1);
		}
		
		table = new JTable();
		tagPane = new JScrollPane(table);
		
		table.setModel(tableMod);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.setRowSelectionAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		left.add(tagPane);
		
		
		
	}
	
	/**
	 * method that gives each of the JButtons appropriate actions.
	 */
	private void setListeners(){
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				AddTagDialog2 addtg = new AddTagDialog2(SearchByTag.this);
				addtg.addWindowListener(new WindowAdapter(){
					public void windowClosed(WindowEvent e){
						if (SearchByTag.this.newTag != null){
							tags.add(newTag);
							new SearchByTag(view, tags);
							SearchByTag.this.dispose();
						}
					}
				});
				
				
			}
		});
		
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SearchByTag.this.dispose();
			}
		});
		
		remove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int row = table.getSelectedRow();
				
				String type = (String) table.getModel().getValueAt(row, 0);
				String value = (String) table.getModel().getValueAt(row, 1);
//				
				Tag currenttag = new Tag(type, value);
				
				tags.remove(currenttag);
				SearchByTag nextWindow = new SearchByTag(view, tags);
				SearchByTag.this.dispose();
//				updateTags();
////				invalidate();
////				validate();
//				EditPhotoDialog.this.repaint();
			}
		});
		
		search.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (tags.size() == 0){
					SearchByTag.this.dispose();
				}
				
				ArrayList<Photo> results = SearchByTag.this.view.getByTags(tags);
				
				if (results.size() == 0){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "No results available");
					return;
				}
				
				try {
					SearchByTag.this.view.displayResults(results);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				SearchByTag.this.dispose();
			}
		});
	}
}
/*
		
	public EditPhotoDialog(UserView userView, String filename, ArrayList<Tag> copy){
				
		initialize();
		place();
		assignListeners();
	}
	
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
		save = new JButton("SAVE");
		cancel = new JButton("CANCEL");
		
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
//				
				Tag currenttag = new Tag(type, value);
				
				tags.remove(currenttag);
				EditPhotoDialog nextWindow = new EditPhotoDialog(view, fileName, tags);
				EditPhotoDialog.this.dispose();
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
*/
