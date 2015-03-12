package cs213.photoAlbum.GUIView;

import javax.swing.*;

import cs213.photoAlbum.control.DefaultControl;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * LoginView is a class that gets gets input from a  user. They can login 
 * by typing a valid username bringing them to the User View 
 * or "admin" which brings them to the Administrator View.
 * @author Jonathan
 *
 */
public class LoginView extends JFrame implements ActionListener{
	
	public static void main(String[] args){
		try {
			JFrame lgv = new LoginView("Login");
			lgv.setSize(300, 90);
			lgv.setResizable(false);
			lgv.setLocationRelativeTo(null);
			lgv.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JLabel usernameJL, errorJL;
	private JTextField usernameTF;
	private JButton loginJB;
	public static DefaultControl control;
	
	/**
	 * Constructor that initializes the contents and displays them
	 * in a JFrame
	 * @param title title of the JFrame
	 * @throws IOException
	 */
	public LoginView(String title) throws IOException {
		super(title);
		control = new DefaultControl();
		usernameJL = new JLabel("Username: ");
		errorJL = new JLabel("");
		usernameTF = new JTextField(10);
		loginJB = new JButton("Login");
		
		setLayout(new FlowLayout());
		add(usernameJL);
		add(usernameTF);
		add(loginJB);
		add(errorJL);
		
		errorJL.setVisible(false);
		loginJB.addActionListener(this);
		
		//shortcut for logging in (pressing enter key)
		usernameTF.addKeyListener( new KeyAdapter() {
             public void keyReleased( final KeyEvent e ) {
                     if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                             login();
                     }
             }
     } );

	}
	
	/**
	 * Performs login method when the login JButton is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginJB) {
			login();
		}
	}
	
	/**
	 * Launches the Administrator View or User View depending user input.
	 */
	public void login() {
		errorJL.setVisible(false);
		String username = usernameTF.getText();
		if (username.equalsIgnoreCase("admin")) {
			try {
				JFrame av = new AdminView("Administrator View");
				this.dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (username.equals("")) {
			errorJL.setText("No username entered. Please try again.");
			errorJL.setVisible(true);
		} else {
			try{
				JFrame uv = new UserView("User View", username);
				this.dispose();
			} catch (IOException ioe){
				System.out.println(ioe.getMessage());
				errorJL.setText("Not a valid username. Please try again.");
				errorJL.setVisible(true);
			}
		}
	}
}

