import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.regex.*;


/**
 * This Class extends {@link ClubAbstractEntity} and is responsible to handle a Person Entity's data
 * and it's UI elements
 *
 */
 
public class Person extends ClubAbstractEntity
{
	/**
     * {@link java.util.ArrayList} holds the "inside" data of the Entity
     */
	private ArrayList<String> pplData;
	/**
     * {@link java.util.ArrayList} holds the validation for each data
     */
	private ArrayList<JLabel> astrix;
	/**
     * {@link java.util.ArrayList} display the data for the user, and can be manipulated without affecting the {@link Person#pplData}
     */
	private ArrayList<JTextField> texts;
	
	
    /**
	 * Person Constructor - with empty data, we call the second Constructor
	 * and disable to cancelButton by calling {@link ClubAbstractEntity#enableCancel}
	 */
	 public Person()
	{
		this(null,null,null,null);
		enableCancel(false);
	}
	
	/**
	 * Person Constructor - calls the {@link #initialize} method to
	 * initialize this object
	 * @param id Person's unique identifier
	 * @param name Person's name 
	 * @param surname Person's surname
	 * @param tel Person's telephone
	 */
	
	public Person(String ID, String f_name, String l_name, String phone)
	{
		initialize(ID,f_name,l_name,phone);
	}
	/**
	 * The method responsible to initialize the Person's data and GUI elements
	 * by calling {@link #buildRow}
	 * and centers them using {@link #addToCenter}
	 * @param data array of data to hold
	 */
	
	void initialize(String ...data)
	{
		setSize(450,220);
		pplData  = new ArrayList<String>();
		astrix  = new ArrayList<JLabel>();
		texts  = new ArrayList<JTextField>();
		String[] labels =  {"ID","Name","Surname","Tel"};
		
		// loop to create rows and put them in the center panel	
		
		for(int i=0;i<data.length;i++)
		{
			addData(data[i]);
			addToCenter(buildRow(data[i],labels[i]));
		}
	}
	/**
	 * Add the data
	 */
	protected void addData(String data)
	{
		pplData.add(data);
	}
	/**
	 * @return the index 
	 */
	protected String getData(int index)
	{
		return pplData.get(index);
	}
	/**
	 * @return the size 
	 */
	protected int getDataSize()
	{
		return pplData.size();
	}
	/**
	 * @return the data using index
	 */
	protected String getText(int index)
	{
		return texts.get(index).getText();
	}
	/**
	 * This method is responsible to create a single row using the parameters that was given
	 * it initialize the row's components, and adds a textField and a label to {@link Person#texts}'s
	 * and {@link Person#astrix} {@link java.util.ArrayList}, respectively
	 * @param data to hold inside textfield
	 * @param label field's name to display for user 
	 * @return {@link javax.swing.JPanel} panel to display to user
	 */
	 
	protected JPanel buildRow(String data, String label)
	{
		
		JPanel rows = new JPanel();
		
	    JLabel labell =new JLabel(label,SwingConstants.RIGHT);
	    rows.add(labell);
		
	    JTextField textField = new JTextField(data,30);
		rows.add(textField);
		texts.add(textField);
		
	    JLabel astrik = new JLabel(" ");
		astrik.setForeground(Color.RED);
		astrik.setPreferredSize(new Dimension(5,astrik.getPreferredSize().height));
		rows.add(astrik);
		astrix.add(astrik);
		
		return rows;
	}
	/**
	 * This Override method checks if an id is not null and checks if the key 
	 * is equal to the id data in person (first element in {@link java.util.ArrayList})
	 * @param key value to match id
	 * @return true if the two Strings are equal
	 * See method: {@link java.lang.String#equals}
	 */
	
	@Override
	public boolean match(String key)
	{
		return pplData.get(0) != null && pplData.get(0).equals(key);
	}
	/**
	 * This Override method checks if id exists in Database using {@link #ifExist}
	 * if exists return false.
	 * go over regular expressions provided inside the method, and check if
	 * all data are valid using overloaded method {@link #ifDataIsValid}
	 * if one of the fields is invalid, the method returns false
	 * @return true if all fields are valid else false 
	 */
	 
	@Override
	protected boolean ifDataIsValid() 
	{
		
		if(!ifExist(texts.get(0).getText(),"ID"))
			return false;
				
		// different regular expressions according to the requirments of the assignment
		String[] regExps = {"^\\d-\\d{7}\\|[1-9]$","^[A-Z][a-z]+$","^([A-Z]([a-z]*[-]?|[a-z]?[']?))+$","^\\+\\([1-9]\\d{0,2}\\)[1-9]\\d{0,2}-[1-9]\\d{6}$"};
		
		//checking if data meets requirments using isValid method
		try
		{
			for(int i=0; i < regExps.length;i++)
				if(!ifDataIsValid(regExps[i],texts.get(i).getText(),i))
					return false;
		}
		//this catch in case when a developer will maintain code incorrectly
		
		catch(IndexOutOfBoundsException  e)
		{
			//this catch will be thrown only when texts or astrix are throwing IndexOutOfBoundsException
			//which regExps.length is larger then what the user sees
			//so some of the regExps may not be needed anymore, it's not a reason to stop the validation just to warn the user
			
			JOptionPane.showMessageDialog(this,String.format("Some Validation may be deprecated, those validations are ingored, Please contact your Administrator for Further information"),"Warning",JOptionPane.WARNING_MESSAGE);
		}
		return true;
	}
	
	/**
	 * This method responsible to notify if clubber exists with specific key
	 * @param key to check if clubber exists with this key
	 * @param keyType field name for display purposes
	 * @return true if clubber with the same key exists, else false
	 */
	protected boolean ifExist(String key,String keyType)
	{
		if(key == null) return true;
		
		ClubAbstractEntity check = NightClubMgmtApp.getClub().getClubber(key);
		if(check != null && check != this)
		{
			JOptionPane.showMessageDialog(this,String.format("Clubber with " + (keyType == null?"Key":keyType) + " " + key + " Exists"),"Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * This method responsible validate a specific data
	 * @param data to validate using regExp
	 * @param regExp to match the data
	 * @param index {@link Person#astrix} index to notify user for validation error
	 * @return true if data is valid, false else
	 */
	/**
	 * This method responsible validate a specific data
	 * @param data to validate using regExp
	 * @param regExp to match the data
	 * @param index {@link Person#marks} index to notify user for validation error
	 * @return true if data is valid, false else
	 */
	protected boolean ifDataIsValid(String regExp,String data,int index)
	{	
		try
		{
			if(!Pattern.compile(regExp).matcher(data).find())
			{
				astrix.get(index).setText("*");
				return false;
			}
				
			astrix.get(index).setText(" ");
			return true;
		} catch(PatternSyntaxException | NullPointerException  | IndexOutOfBoundsException e)		{
			//this catch will be thrown when regExps Array will be maintained incorrectly, i.e bad pattern will be insert or null
			JOptionPane.showMessageDialog(this,String.format("Validation problem, please rollback and contact your Administrator"),"Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
		
		
	/**
	 * This Override method go over each field and commit them
	 * by taking the text from the  {@link javax.swing.JTextField#getText} element from the {@link Person#datas}'s {@link java.util.ArrayList}
	 * and insert it into the equivalent element from the {@link Person#pplData}'s {@link java.util.ArrayList}
	 */
	
	
	@Override
	protected void commit()
	{
		for(int i=0; i < pplData.size();i++)
			pplData.set(i, texts.get(i).getText());
	}
	
	/**
	 * This Override method go over each field and rollback them
	 * by taking the data from the {@link Person#pplData}'s {@link java.util.ArrayList}
	 * and insert it into the equivalent element from the {@link Person#texts}'s {@link java.util.ArrayList}
	 */
	
	@Override
	protected void rollback()
	{
		for(int i=0; i < texts.size();i++)
		{
			texts.get(i).setText(pplData.get(i));
			astrix.get(i).setText(" ");
		}
	}
	
}