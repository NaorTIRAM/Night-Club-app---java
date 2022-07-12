import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * This class implements the NightClubMgmtApp's main window
 * It holds the Database Path, the selected Entity type for creation
 * List of clubbers and controlButtons
 * It implements {@link java.awt.event.ActionListener} for controlButtons operation,
 * and  {@link java.awt.event.ItemListener} for Entity selection
 */
 
public class NightClubMgmtApp extends JFrame implements ActionListener
{
	/**
     * {@link java.util.ArrayList}  to handle all {@link ClubAbstractEntity} which will be added from user or loaded from {@link  NightClubMgmtApp#loadClubbersDBFromFile}
     */
	private  ArrayList<ClubAbstractEntity> clubbers; 
    /**
     * {@link java.util.ArrayList} to handle buttons for the {@link NightClubMgmtApp} actions
     */
	private final ArrayList<JButton> controlButtons;
	/**
     * holds the option (solider, student, person)
     */
	private final ArrayList<JRadioButton> entityChoices_Radio;
	/**
     * holds the group
     */
	private final ButtonGroup chosen;
	
	/**
     * {@link NightClubMgmtApp} only object (for Singleton implementation)
     */
	private static NightClubMgmtApp club; 
	/**
     * holds the selected Entity type 
     */
	private String newEntityType;
	/**
     * path to the Database
     */
	private final String DBPath = "BKCustomers.dat";
	/**
     * number of visible {@link ClubAbstractEntity} windows
     */
	private int framesCounter;
	//counts open ClubAbstractEntity for creation

     /**
	 * static function which helps to implement the Singleton design pattern
	 * @return the NightClubMgmtApp's only object
	 */
	public static NightClubMgmtApp getClub() 
    { 
        if (club == null) 
        	club = new NightClubMgmtApp(); 
        return club; 
    }
	/**
	 * NightClubMgmtApp Constructor - Initializes the program
	 * and loads the clubbers from the Database file,
	 * initializes the GUI elements
	 * and attaches the required listeners.
	 */
	private NightClubMgmtApp()
	{
		clubbers = new ArrayList<ClubAbstractEntity>();
		loadClubbersDBFromFile();
		
		String[] buttonName = {"Search","Create"};
		controlButtons = new ArrayList<JButton>();
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel(new FlowLayout());
		panel.add(centerPanel, BorderLayout.CENTER);

		panel.setPreferredSize(new Dimension(350,350));
		
		for(int i=0;i<buttonName.length;i++)
		{
			controlButtons.add(new JButton(buttonName[i]));
			controlButtons.get(i).addActionListener(this);
		}
		
		panel.add(controlButtons.get(0), BorderLayout.NORTH);
		panel.add(controlButtons.get(1), BorderLayout.SOUTH);
		
		//panel.add(new JLabel("Club Entity Type: "));
		//radio buttons ??????
		//JComboBox<String> entityChoices = new JComboBox<String>(new String [] {"Person","Soldier","Student"});
		//panel.add(entityChoices, BorderLayout.AFTER_LAST_LINE);
		//entityChoices.addItemListener(this);
		
		entityChoices_Radio = new ArrayList<JRadioButton>();
		chosen = new ButtonGroup();
		String[] entityNames = {"Person","Soldier","Student"};
		
		centerPanel.add(new JLabel("Choose an entity:"));
		for(int i=0; i<entityNames.length; i++)
		{
			entityChoices_Radio.add(new JRadioButton(entityNames[i]));
			entityChoices_Radio.get(i).addActionListener(this);
			chosen.add(entityChoices_Radio.get(i));
			centerPanel.add(entityChoices_Radio.get(i), BorderLayout.AFTER_LINE_ENDS);
		}
		entityChoices_Radio.get(0).setSelected(true);
		
		add(panel, BorderLayout.CENTER);
		
		pack();
		
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				writeClubbersDBToFile();
			}
		} );
		
		setTitle("B.K Club Database Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * this method is responsible for writing the clubbers into the DB
	 * if DB not found, doesn't have permission nor have issues with IO, the method displays an error
	 * using {@link javax.swing.JOptionPane#showMessageDialog}
	 * this method uses {@link java.io.FileOutputStream} and {@link java.io.ObjectOutputStream}
	 * for DB handling using Serializable Objects
	 */
	private void writeClubbersDBToFile()
	{ 
		String err = "Please contact with your system administrator.";
		try (FileOutputStream F_OutStream = new FileOutputStream(DBPath);
		ObjectOutputStream O_OutStream = new ObjectOutputStream(F_OutStream)) 
		{
			O_OutStream.writeObject(clubbers);            
		    O_OutStream.flush(); 
		} catch (FileNotFoundException e) {			
			JOptionPane.showMessageDialog(this,String.format("Database not found, " + err),"\nError #1",JOptionPane.ERROR_MESSAGE);
		} 
		catch(IOException | SecurityException e) {
			JOptionPane.showMessageDialog(this,String.format("Error while writing to database, " + err),"\nError #2",JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * this method is responsible for loading the clubbers from the DB
	 * if DB not found,doesn't have permission nor have issues with IO, the method displays an error
	 * using {@link javax.swing.JOptionPane#showMessageDialog}
	 * this method uses {@link java.io.FileOutputStream} and {@link java.io.ObjectOutputStream}
	 * for DB handling using Serializable Objects
	 */
	private void loadClubbersDBFromFile()
	{
		String err = "Please Contact with your Administrator";
		try(FileInputStream F_InStream = new FileInputStream(DBPath);
			ObjectInputStream O_InStream = new ObjectInputStream(F_InStream)) {
			
			// this assignment to object is only for the case
			//if the file read is not the same format
			Object obj = O_InStream.readObject();
			
			if(!(obj instanceof ArrayList))
				throw new ClassNotFoundException();
			
			ArrayList temp = (ArrayList)obj;
			for(Object objIndx:temp)
			{
				if(!(objIndx instanceof ClubAbstractEntity)) continue;
				
				clubbers.add((ClubAbstractEntity)objIndx);
			}		
		} 
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this,String.format("DB Not Found, " + err),"\nError #3",JOptionPane.ERROR_MESSAGE);
		} 
		catch(IOException | SecurityException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this,String.format("Error while loading from DB, " + err),"\nError #4",JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * This method runs the button's operation, that triggers her
	 * @param e the Action occurs
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// search button
		if(e.getSource() == controlButtons.get(0))
		{
			String input = JOptionPane.showInputDialog(null,"Please Enter The Clubber's Key ","Input",JOptionPane.QUESTION_MESSAGE);
			if(input == null) return;
			
			ClubAbstractEntity clubber = getClubber(input);
			if(clubber != null)
			{
				clubber.setVisible(true);
				clubber.toFront();
				return;
			}
			
			JOptionPane.showMessageDialog(this,String.format("Clubber with key %s does not exist", input),"Error",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		//create button
		if(e.getSource() == controlButtons.get(1))
		{
			//this dialog allow the user to think again before he enter to a window which he can't get out
			if(JOptionPane.showConfirmDialog (this, "Are you sure you want to create a new Clubber?","Warning",JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) 
				return;
			
			
			ClubAbstractEntity entity = createEntity(getClubberType());
			if(!(entity instanceof ClubAbstractEntity))
			{
				JOptionPane.showMessageDialog(this,String.format("Clubber does not exist"),"Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			entity.addComponentListener(new ComponentHandler());
			entity.setVisible(true);
			clubbers.add(entity);
		}
	}
	/**
	 * This method commits a search using the match method
	 * and returns the clubber object if found or null if not
	 * @param key the search the {@link ClubAbstractEntity} which holds the key
	 * @return {@link ClubAbstractEntity} if key belongs to him else null
	 */
	public ClubAbstractEntity getClubber(String key)
	{
		
		for(ClubAbstractEntity clubber : clubbers)
		{
			if(clubber.match(key))
				return clubber;
		}
		return null;
	}
	
	/**
	 * This method is responsible to return what button was choosen
	 * @return the button the was choosen
	 */
	
	private String getClubberType()
	{
		for(JRadioButton b : entityChoices_Radio)
			if (b.isSelected())
				return b.getText();
		
		return null;
	}
	/**
	 * This method is responsible to create a new Entity Frame
	 * according to the type sent to her.
	 * can create {@link Person},{@link Soldier} or {@link Student} frame 
	 * if the type index does not exist - null is returned.
	 * @param type - the Entity type number
	 * @return {@link ClubAbstractEntity} desired entity
	 */
	 
	public ClubAbstractEntity createEntity(String type)
	{		
		switch (type)
		{
			case "Person":
				return new Person();
			case "Soldier":
				return new Soldier();
			case "Student":
				return new Student();
			default:
				return null;
		}
	}
	
	/**
	 * This is a private inner class for NightClubMgmtApp that extends
	 * the {@link java.awt.event.ComponentAdapter} for handling and modifying
	 * ClubAbstractEntity visibility status and a safe exit for the program	 *
	 */
	private class ComponentHandler extends ComponentAdapter{
		/**
		 * This method is triggered when ClubAbstractEntity's visibility is true
		 * it increments the count of open ClubAbstractEntity's Frames for creation
		 * and sets the close operation to DO_NOTHING_ON_CLOSE
		 * @param e unused
		 */
		@Override
		public void componentShown(ComponentEvent e) {
			framesCounter++;
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
		
		/**
		 * This method is triggered when ClubAbstractEntity's visibility is false
		 * it decrements the count of open ClubAbstractEntity's Frames for creation
		 * and if the count is 0 it sets the close operation to EXIT_ON_CLOSE
		 * to enable a safe exit from the program
		 * @param e unused
		 */
		@Override
		public void componentHidden(ComponentEvent e) {
			if(--framesCounter == 0)
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}
	/**
	 * main Method that runs NightClubMgmtApp's main window
	 * @param args unused
	 */
	public static void main(String[] args)
	{
		NightClubMgmtApp.getClub();
	}
}