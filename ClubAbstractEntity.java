import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This class responsible for the UI of a Club Entity
 * it extends {@link javax.swing.JFrame} and implements {@link java.io.Serializable}
 * it uses buttons to commit and rollback data
 * and has a center panel for displaying required fields.
 *
 */
public abstract class ClubAbstractEntity extends JFrame
{
	/**
     * {@link java.util.ArrayList} to handle buttons for the {@link ClubAbstractEntity}
     */
	private ArrayList<JButton> controlButtons;
	/**
     * {@link javax.swing.JPanel} hold the UI elements to display data
     */
	JPanel centerPanel;
	
    /**
	* ClubAbstractEntity constructor - creates the ClubAbstractEntity's window. 
 	* the user cannot click the window resize button and cannot resize 
 	* the window by dragging its edges with the mouse.
	* See method: {@link  java.awt.Frame#setResizable}.
	* user cannot exit by pressing the X button
	* See method: {@link  javax.swing.JFrame#setDefaultCloseOperation}
	* Initialize GUI elements and attach the buttons with {@link ButtonsHandler}
	* set title using {@link java.lang.Class#getName}
	*/
	public ClubAbstractEntity()
	{
		setTitle(this.getClass().getName() + " Clubber's Data");
		ButtonsHandler handler = new ButtonsHandler();
		String[] buttonName = {"Ok","Cancel"};
		
		controlButtons = new ArrayList<JButton>();
		JPanel southPanel = new JPanel(new FlowLayout());
		
		for(int i=0;i<buttonName.length;i++)
		{
			controlButtons.add(new JButton(buttonName[i]));
			controlButtons.get(i).addActionListener(handler);
			southPanel.add(controlButtons.get(i));
		}
		
		centerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(centerPanel);
		add(southPanel,BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
	}
	/**
	* This method enables/disables the usage of the cancelButton.
	* It is disabled on default and becomes
	* enabled when an entity is created.
	* @param isEnable value of the cancelButton to be set.
	*/
	protected void enableCancel(boolean isEnable)
	{
		controlButtons.get(1).setEnabled(isEnable);
	}
	/**
	* This method responsible for adding {@link java.awt.Component} into the center panel.
	* if the Component is null it will return without adding.
	* @param guiComponent component to add.
	*/
	public void addToCenter(Component guiComponent) 
	{
		if(guiComponent == null) return;
		centerPanel.add(guiComponent);
	}
	
	/**
	 * This is a private inner class for ClubAbstractEntity that implements
	 * the {@link java.awt.event.ActionListener} for GUI buttons
	 *
	 */
	private class ButtonsHandler implements	ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == controlButtons.get(0))
			{
				if(!ifDataIsValid()) return;
				
				commit();
				enableCancel(true);
				setVisible(false);
			}
			
			if(e.getSource() == controlButtons.get(1))
			{
				rollback();
				setVisible(false);
			}
		}
	}
	/**
     * Abstract method to check if a given key has 
     * a match in the database. 
     * Must be implemented by subclasses.
     * @param key - the String to match.
     * @return true if key is matched 
     */
	public abstract boolean match(String key);
	/**
     * Abstract method to validate class's data. 
     * Must be implemented by subclasses.
     * @return true if class's data is valid
     */
	protected abstract boolean ifDataIsValid();
	/**
     * Abstract method that activates the commit process. 
     * Must be implemented by subclasses.
     */
	protected abstract void commit();
	/**
     * Abstract method the activates the rollBack process. 
     * Must be implemented by subclasses.
     */
	protected abstract void rollback();	
}	