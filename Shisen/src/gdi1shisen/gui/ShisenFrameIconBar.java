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
		
		//Buttons belegen
		undoButton.setIcon(leftArrow);
		undoButton.setText("Undo");
		undoButton.setToolTipText("Zug rückgängig machen");
		undoButton.addActionListener(this);
				
		redoButton.setIcon(rightArrow);
		redoButton.setText("Redo");
		redoButton.setToolTipText("Zug wiederherstellen");
		redoButton.addActionListener(this);
		
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
		add(solveButton);
	}

	/**
	 * Aktionssteuerung für gedrückte Buttons
	 * @param e ActionEvent ausgelöst von Buttons der IconBar
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
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
		//Aktion bei AUTOPILOT Button
		if (e.getSource().equals(solveButton))
		{
			userMoveController.solveLevel();
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
