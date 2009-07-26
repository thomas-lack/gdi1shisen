package gdi1shisen.gui;

import gdi1shisen.gamecontroller.UserMove;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * Anzeigeklasse für die Iconleiste am oberen Spielfeldrand.
 * Sendet entsprechende Befehle an den UserMove Controller.
 * @author Thomas, Benjamin
 */
public class ShisenFrameIconBar 
extends JToolBar
implements ActionListener
{
	private static final long serialVersionUID = -6703370599306276007L;

	private UserMove userMoveController;
	
	private JButton undoButton = new JButton();
	private JButton redoButton = new JButton();
	private JButton hintButton = new JButton();
	private JButton shuffleButton = new JButton();
	private JButton solveButton = new JButton();
	
	
	public ShisenFrameIconBar(ShisenFrameBoard gamePanel)
	{		
		//User soll Toolbar nicht verschieben können
		this.setFloatable(false);
		
		//Register Images
		ImageIcon leftArrow = new ImageIcon("Images/left-arrow.png");
		ImageIcon rightArrow = new ImageIcon("Images/right-arrow.png");
		ImageIcon lightbulb = new ImageIcon("Images/lightbulb.png");
		ImageIcon wrench = new ImageIcon("Images/wrench.png");
		ImageIcon shuffle = new ImageIcon("Images/shuffle.png");
		
		//Buttons belegen
		undoButton.setIcon(leftArrow);
		undoButton.setText("Undo");
		undoButton.setToolTipText("Zug rückgängig machen");
		undoButton.addActionListener(this);
				
		redoButton.setIcon(rightArrow);
		redoButton.setText("Redo");
		redoButton.setToolTipText("Zug wiederherstellen");
		redoButton.addActionListener(this);
		
		shuffleButton.setIcon(shuffle);
		shuffleButton.setText("Shuffle");
		shuffleButton.setToolTipText("Ordnet die verbliebenen Steine auf dem Spielfeld neu an");
		shuffleButton.addActionListener(this);
		
		hintButton.setIcon(lightbulb);
		hintButton.setText("Zughilfe");
		hintButton.setToolTipText("Möglichen Spielzug angezeigen");
		hintButton.addActionListener(this);
		
		solveButton.setIcon(wrench);
		solveButton.setText("Autopilot");
		solveButton.setToolTipText("Das Level wird selbstständig vom PC gelöst");
		solveButton.addActionListener(this);
		
		//Buttons zur Toolbar hinzufügen
		add(undoButton);
		add(redoButton);
		addSeparator();
		add(hintButton);
		addSeparator();
		add(shuffleButton);
		addSeparator();
		add(solveButton);
	}

	/**
	 * Aktionssteuerung für gedrückte Buttons
	 * @param e ActionEvent ausgelöst von Buttons der IconBar
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(this.userMoveController.reloadInput())
		{
			//Aktion bei UNDO Button
			if (e.getSource().equals(undoButton))
			{
				userMoveController.undo();
			}
			//Aktion bei REDO Button
			if (e.getSource().equals(redoButton))
			{
				userMoveController.redo();
			}
			//Aktion bei ZUGHILFE Button
			if (e.getSource().equals(hintButton))
			{
				userMoveController.showNextPossibleMove();
			}
			//Aktion bei SHUFFLE Button
			if (e.getSource().equals(shuffleButton))
			{
				userMoveController.shuffle();
			}
			//Aktion bei AUTOPILOT Button
			if (e.getSource().equals(solveButton))
			{
				userMoveController.solveLevel();
			}
		}
		
		// setze Eingabe Fokus zurück auf ShisenFrame
		this.userMoveController.setFocus();
	}
	
	/**
	 * Setter für das UserMove Objekt. Anbindung an den Controller.
	 * @param userMoveController UserMove Objekt was die Nuterzeingaben verarbeitet
	 */
	public void setMoveController(UserMove userMoveController)
	{
		this.userMoveController = userMoveController;
	}
}
