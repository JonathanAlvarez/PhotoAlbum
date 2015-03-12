package cs213.photoAlbum.GUIView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import cs213.photoAlbum.control.DefaultControl;

/**
 * AdminView is a class that has a list of users in the database and displays them in a table.
 * The admin can create or delete any users from the database. 
 * @author Jonathan
 *
 */
public class AdminView extends JFrame implements ActionListener {

	private JLabel usernameJL, nameJL;
	private JTextField usernameTF, nameTF;
	private JButton logoutJB, addJB, deleteJB;
	private JTable userJT;
	private DefaultTableModel tableModel;
	private JScrollPane tableSP;
	private JPanel p1, p2;
	public static DefaultControl control;
	public String[] users;
	
	/**
	 * Constructor that generates the gui for the AdminView window.
	 * @param title String used for the JFrame title
	 * @throws IOException
	 */
	public AdminView(String title) throws IOException {
		super(title);
		
		this.setSize(500,250);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false); 
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				LoginView lgv;
				try {
					lgv = new LoginView("Login");
					lgv.setSize(300,90);
					lgv.setVisible(true);
					lgv.setLocationRelativeTo(null);
					lgv.setResizable(false);
					lgv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			public void windowClosed(WindowEvent e){
				LoginView lgv;
				try {
					lgv = new LoginView("Login");
					lgv.setSize(300,90);
					lgv.setVisible(true);
					lgv.setLocationRelativeTo(null);
					lgv.setResizable(false);
					lgv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		control = new DefaultControl();
		usernameJL = new JLabel("Username: ");
		usernameTF = new JTextField(10);
		nameJL = new JLabel("Name: ");
		nameTF = new JTextField(10);
		logoutJB = new JButton("Logout");
		addJB = new JButton("Add");
		deleteJB = new JButton("Delete");
		userJT = new JTable();
		tableSP = new JScrollPane(userJT);
		
		populateTable();
		BorderLayout brdl = new BorderLayout();
		p1 = new JPanel(brdl);
		p2 = new JPanel(new GridLayout(7, 1, 1, 5));
		
		p1.add(tableSP, BorderLayout.CENTER);
		p1.add(logoutJB, BorderLayout.PAGE_END);
		brdl.setVgap(5);
		
		p2.add(usernameJL);
		p2.add(usernameTF);
		p2.add(nameJL);
		p2.add(nameTF);
		p2.add(new JPanel());
		p2.add(addJB);
		p2.add(deleteJB);
		
		setLayout(new GridLayout(1,2,15,10));
		add(p1);
		add(p2);

		logoutJB.addActionListener(this);
		addJB.addActionListener(this);
		deleteJB.addActionListener(this);
	}
	
	/**
	 * populates the table with the users that are currently in the database.
	 * displays the usernames and user's full name of each user.
	 * @throws IOException
	 */
	public void populateTable() throws IOException {
		users = control.listUsers();
		
		String[] titles = {"User ID", "User Name"};
		if (users == null) {
			tableModel = new DefaultTableModel(titles, 11);
			tableModel.setValueAt("no valid user", 0, 0);
			tableModel.setValueAt("...", 0, 1);
		} else {
			
				tableModel = new DefaultTableModel(titles, users.length > 11 ? users.length : 11);
				
			for (int i = 0; i < users.length; i++){	
				
				control.backend.readUser(users[i]);
				tableModel.setValueAt(users[i], i, 0);
				tableModel.setValueAt(control.backend.user.fullName, i, 1);
				
			}
		}
		
		userJT.setModel(tableModel);
		userJT.getColumnModel().getColumn(0).setResizable(false);
		userJT.getColumnModel().getColumn(1).setResizable(false);
		userJT.setRowSelectionAllowed(true);
		userJT.getTableHeader().setReorderingAllowed(false);
		userJT.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		userJT.getSelectionModel().addListSelectionListener(new tableListener(this));
	}
	
	/**
	 * Class used to listen for changes in the selection of the table
	 * and fire off events based on that.
	 * @author Jonathan
	 *
	 */
	public class tableListener implements ListSelectionListener{

		AdminView av;
		public tableListener(AdminView av){
			this.av = av;
		}
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			// TODO Auto-generated method stub
			try{
			String ID = (String) userJT.getModel().getValueAt(userJT.getSelectedRow(), 0);
			String name = (String) userJT.getModel().getValueAt(userJT.getSelectedRow(), 1);
			
			usernameTF.setText(ID);
			nameTF.setText(name);
			} catch (Exception E){
				
			}
			
		}
		
	}

	/**
	 * action listener method that makes decisions based on what button
	 * was clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		
		//Adds a user to the database
		if (e.getSource() == addJB) {
			String userID = usernameTF.getText();
			String userName = nameTF.getText();
			if (userID.equals("") || userName.equals("")){
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "Username is blank or trying to add already existing user.");
				System.out.println("username or name text field is empty.");
			} else {
				if (control.addUser(userID, userName)){
					System.out.println("created user " + userID + " with name " + userName);
					usernameTF.setText("");
					nameTF.setText("");
				}
			}
			
			try {
				populateTable();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//Deletes a user from the database.
		if (e.getSource() == deleteJB) {
			String userID = usernameTF.getText();
			usernameTF.setText("");
			nameTF.setText("");
			if (control.removeUser(userID)){
				System.out.println("deleted user " + userID);
			}
			
			try {
				populateTable();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//Not really needed since admin changes are always saved as they're made
		//but it was on the storyboard so... yeah. Closes the Admin View JFrame.
		if (e.getSource() == logoutJB) {
			dispose();
		}
	
		
	}
}

