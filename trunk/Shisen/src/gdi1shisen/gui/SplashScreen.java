package gdi1shisen.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SplashScreen extends JPanel
{
	private static final long serialVersionUID = 6956537905146976012L;

	public SplashScreen()
	{
		super();
		ImageIcon image = new ImageIcon("Images/splashscreen.png");
		this.setLayout(new BorderLayout());
		this.add(new JLabel(image, JLabel.CENTER), BorderLayout.CENTER);
	}
}
