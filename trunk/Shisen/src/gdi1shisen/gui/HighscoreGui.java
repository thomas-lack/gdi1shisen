package gdi1shisen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HighscoreGui extends JFrame
{
public JLabel[] Best = new JLabel[5];
public JLabel[] Rang = new JLabel[5];
public HighscoreGui()
{

setLocation(600, 250);
setSize(600, 400);
setResizable(false);
setVisible(false);

//Erzeugen eines Objektes der Klasse WindowAdapter,
//implementieren der Schnittstelle WindowListener
addWindowListener(new WindowAdapter()
{

public void windowClosing (WindowEvent e)
{
setVisible(false);

}
});



Container highscore = getContentPane();
JPanel westPanel =new JPanel();
JPanel eastPanel =new JPanel();
JPanel northPanel =new JPanel();
JPanel southPanel = new JPanel();
JPanel centerPanel = new JPanel();


//in diesem Panel wird das GridLayout verwendet
northPanel.setLayout(new GridLayout(2,1));

//Erzeugung von 2 Label, die dem Panel zugefügt werden
JLabel label1, label2;
label1 =new JLabel("Highscore");
label2 =new JLabel(" Name: Zeit:");
label1.setFont (new Font("SansSerif", Font.BOLD, 18));
label2.setFont (new Font("SansSerif", Font.PLAIN, 13));
northPanel.add(label1,BorderLayout.NORTH);
northPanel.add(label2,BorderLayout.NORTH);
northPanel.setBackground (Color.blue);

//das Panel wird dem Container im Norden zugefügt
highscore.add(northPanel,BorderLayout.NORTH);



southPanel.setBackground(Color.blue);
highscore.add(southPanel, BorderLayout.SOUTH);


//im westPanel wird das GridLayout verwendet
westPanel.setLayout(new GridLayout(5,1));

//Erzeugung von 5 Label, die dem Panel zugefügt werden
JLabel platz1, platz2, platz3, platz4, platz5;
platz1 =new JLabel("1.");
platz2 =new JLabel("2.");
platz3 =new JLabel("3.");
platz4 =new JLabel("4.");
platz5 =new JLabel("5.");
platz1.setFont (new Font("SansSerif", Font.PLAIN, 15));
platz2.setFont (new Font("SansSerif", Font.PLAIN, 15));
platz3.setFont (new Font("SansSerif", Font.PLAIN, 15));
platz4.setFont (new Font("SansSerif", Font.PLAIN, 15));
platz5.setFont (new Font("SansSerif", Font.PLAIN, 15));

westPanel.add(platz1,BorderLayout.WEST);
westPanel.add(platz2,BorderLayout.WEST);
westPanel.add(platz3,BorderLayout.WEST);
westPanel.add(platz4,BorderLayout.WEST);
westPanel.add(platz5,BorderLayout.WEST);
westPanel.setBackground (Color.yellow);

highscore.add(westPanel,BorderLayout.WEST);


centerPanel.setLayout(new GridLayout(9,4));


for (int g=0; g<6; g++){
Rang[g] = new JLabel("");
Rang[g].setSize(350,150);
Rang[g].setFont (new Font("SansSerif", Font.PLAIN, 15));
centerPanel.add(Rang[g]);

}
centerPanel.setBackground(Color.white);

highscore.add(centerPanel);




//das Panel wird dem Container im Süden zugefügt

highscore.add(southPanel,BorderLayout.SOUTH);

//Platzhalter Panel
eastPanel.setBackground (Color.blue);
highscore.add(eastPanel,BorderLayout.EAST);
Best[0]=new JLabel( "Eugen", 0 );
Best[1]=new JLabel( "Chandra ", 1 );
Best[2]=new JLabel( "Thomas", 2 );
Best[3]=new JLabel( "Benjamin", 3 );
Best[4]=new JLabel( "Martina", 4 );
int spielzeit=0;
Best[5]=new JLabel( "Neuer Spieler", spielzeit );

;
}
}