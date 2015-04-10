package gae.editorpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import gae.model.Receiver;

import javax.swing.JButton;
import javax.swing.JDialog;

public class ComponentsDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> myOptions;
	private String myReturnElement;
	private Receiver model; //no idea how to access this model yet. 

	public ComponentsDialog(String type) {
		myReturnElement = null;
		myOptions = model.getElements(type);
		for (String option : myOptions) {
			JButton b = new JButton(option);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myReturnElement = option;
					dispose();
				}
			});
		}
	}
	
	public String getElement(){
		return myReturnElement;
	}
}
