package cs213.photoAlbum.GUIView;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoAlbum.model.Photo;

/**
 * JFrame class used to search for photos based on its date.
 * @author Brent
 *
 */
public class SearchByDate extends JFrame {
	
	UserView view;
	JTextField hour, min, sec, month, day, year, ehour, emin, esec, emonth, eday, eyear;
	JButton search, cancel;
	
	/**
	 * Constructor used to initialize the contents of the JFrame
	 * @param userview contents used in initialization
	 */
	public SearchByDate(UserView userview){
		super("SearchByDate");
		view = userview;
		
		hour = new JTextField();
		min = new JTextField();
		sec = new JTextField();
		month = new JTextField();
		day = new JTextField();
		year = new JTextField();
		ehour = new JTextField();
		emin = new JTextField();
		esec = new JTextField();
		emonth = new JTextField();
		eday = new JTextField();
		eyear = new JTextField();
		
		hour.setPreferredSize(new Dimension(20,20));
		min.setPreferredSize(new Dimension(20,20));
		sec.setPreferredSize(new Dimension(20,20));
		month.setPreferredSize(new Dimension(20,20));
		day.setPreferredSize(new Dimension(20,20));
		year.setPreferredSize(new Dimension(35,20));
		ehour.setPreferredSize(new Dimension(20,20));
		emin.setPreferredSize(new Dimension(20,20));
		esec.setPreferredSize(new Dimension(20,20));
		emonth.setPreferredSize(new Dimension(20,20));
		eday.setPreferredSize(new Dimension(20,20));
		eyear.setPreferredSize(new Dimension(35,20));
		
		this.setSize(300, 150);
		this.setLocationRelativeTo(view);
		this.setResizable(false);
		this.setVisible(true);
		
		setLayout(new GridLayout(3, 1));
		
		JPanel first = new JPanel(new FlowLayout());
		first.add(new JLabel("Starting date: "));
		first.add(month);
		first.add(new JLabel("/"));
		first.add(day);
		first.add(new JLabel("/"));
		first.add(year);
		first.add(new JLabel("-"));
		first.add(hour);
		first.add(new JLabel(":"));
		first.add(min);
		first.add(new JLabel(":"));
		first.add(sec);
		
		JPanel second = new JPanel(new FlowLayout());
		second.add(new JLabel("Starting date: "));
		second.add(emonth);
		second.add(new JLabel("/"));
		second.add(eday);
		second.add(new JLabel("/"));
		second.add(eyear);
		second.add(new JLabel("-"));
		second.add(ehour);
		second.add(new JLabel(":"));
		second.add(emin);
		second.add(new JLabel(":"));
		second.add(esec);
		
		add(first);
		add(second);
		
		JPanel third = new JPanel(new FlowLayout());
		
		search = new JButton("SEARCH");
		cancel = new JButton("CANCEL");
		
		third.add(search);
		third.add(cancel);
		
		add(third);
		
		
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SearchByDate.this.dispose();
			}
		});
		
		search.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int hour, min, sec, month, day, year, ehour, emin, esec, emonth, eday, eyear;
				
				try{
					hour = Integer.parseInt(SearchByDate.this.hour.getText());
					min = Integer.parseInt(SearchByDate.this.min.getText());
					sec = Integer.parseInt(SearchByDate.this.sec.getText());
					month = Integer.parseInt(SearchByDate.this.month.getText());
					day = Integer.parseInt(SearchByDate.this.day.getText());
					year = Integer.parseInt(SearchByDate.this.year.getText());
					
					ehour = Integer.parseInt(SearchByDate.this.ehour.getText());
					emin = Integer.parseInt(SearchByDate.this.emin.getText());
					esec = Integer.parseInt(SearchByDate.this.esec.getText());
					emonth = Integer.parseInt(SearchByDate.this.emonth.getText());
					eday = Integer.parseInt(SearchByDate.this.eday.getText());
					eyear = Integer.parseInt(SearchByDate.this.eyear.getText());
					
				}catch (NumberFormatException E){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "All boxes must contain a number.");
					return;
				}
				
				if (hour > 24 || min > 60 || sec > 60 || ehour > 24 || emin > 60 || esec > 60){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				}
				
				if(month > 12 || day > 31 || emonth > 12 || eday > 31){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				}
				
				if ((month == 4 && day > 30 )|| (emonth == 4 && eday > 30 )){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				} else if((month == 2 && day > 28 && year % 4 != 0) ||(emonth == 2 && eday > 28 && eyear % 4 != 0)){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				} else if ((month == 6 && day > 30 )|| (emonth == 6 && eday > 30 )){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				}else if ((month == 9 && day > 30 )|| (emonth == 9 && eday > 30 )){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				}else if ((month == 11 && day > 30 )|| (emonth == 11 && eday > 30 )){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "Not a valid time.");
					return;
				}
				
				Calendar end = Calendar.getInstance();
				Calendar start = Calendar.getInstance();
				
				start.set(year, month, day, hour, min, sec);
				end.set(eyear, emonth, eday, ehour, emin, esec);
				
				ArrayList<Photo> results = SearchByDate.this.view.getByDate(start, end);
				if (results.size() == 0){
					JFrame frame1 = new JFrame("Error");
					JOptionPane.showMessageDialog(frame1, "No results available");
					return;
				}
				
				try {
					SearchByDate.this.view.displayResults(results);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				SearchByDate.this.dispose();				
			}
		});
		
	}

}
