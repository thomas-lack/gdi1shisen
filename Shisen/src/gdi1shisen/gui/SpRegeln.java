

package gdi1shisen.gui;

/*
 * Brauch eine extra Datei "Spielregeln.txt2
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.*;

import javax.swing.GroupLayout.*;

public class SpRegeln extends JFrame
                           {
    
    
  
    private JScrollPane jScrollPane1;
    
    private JTextArea regeln;
    
  

    
    
    public SpRegeln(String windowTitle) {
    	
        initComponents();
        
        InputStream in = getClass().getResourceAsStream("Spielregeln.txt");
        try {
            regeln.read(new InputStreamReader(in), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
     
        
        
    
        
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */

    private void initComponents() {
    	
        regeln = new JTextArea();
        
     
        //versteckt das fenster sobald mann es ´schließt
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setTitle("Spielregeln");

        //falls die Zeilen zu lang sind werden sie mit Wrap auf geieignte länge geschnitten
        regeln.setLineWrap(true);
        
        regeln.setWrapStyleWord(true);
        regeln.setEditable(false);
        jScrollPane1 = new JScrollPane(regeln);

       

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
     
	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	
	//Create a sequential and a parallel groups
	SequentialGroup h1 = layout.createSequentialGroup();
	ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
	
	//Add a container gap to the sequential group h1
	h1.addContainerGap();
	
	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

	
	//Create a sequential group h3
	SequentialGroup h3 = layout.createSequentialGroup();

	h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);

	//Add the group h3 to the group h2
	h2.addGroup(h3);
	//Add the group h2 to the group h1
	h1.addGroup(h2);

	h1.addContainerGap();
	
	//Add the group h1 to the hGroup
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);
	
       
	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	//Create a sequential group v1
	SequentialGroup v1 = layout.createSequentialGroup();
	//Add a container gap to the sequential group v1
	v1.addContainerGap();
	//Create a parallel group v2
	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

	
	//Add the group v2 tp the group v1
	v1.addGroup(v2);
	v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
	v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
	v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
	
	v1.addContainerGap();
	
	//Add the group v1 to the group vGroup
	vGroup.addGroup(v1);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);
	pack();
	
//	*/
    }

  
    
    
   
}
