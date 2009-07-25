package gdi1shisen.gui;



import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class About extends JFrame{
	
	//----------------------------------------------------------------//
	private static final long serialVersionUID = -2344L;
	//----------------------------------------------------------------//
	
	public About(String windowTitle){
		super(windowTitle);
		getContentPane().setLayout(new BorderLayout(10,10));
		setSize(300, 250);
		//Setzt Label mit den Namen fest
		JLabel nameLabel = new JLabel();
		//Hier kann auch die ganze Geschichte und der ganze schmog rein!
		nameLabel.setText("Thomas, Eugen, Chandra und Benjamin");
		//Setzt den text mittig
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.SOUTH, nameLabel);
		//A-Team
		JLabel buddhaGuido = new JLabel();
		buddhaGuido.setText("by A-Team");
		buddhaGuido.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.NORTH, buddhaGuido);
		//bild buddhaGuido

		JLabel buddhaGuidoPic = new JLabel(new ImageIcon("Images/smile.png"));
		buddhaGuidoPic.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.CENTER, buddhaGuidoPic);
		
		}

	}	

