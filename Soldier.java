
/**
 * This Class extends {@link Person} and responsible to handle Soldier Entity
 *
 */
public class Soldier extends Person
{
	/**
	 * Soldier Constructor -  calls the super constructor, and we call {@link initialize} method with null
	 * and initialize this object
	 */
	public Soldier()
	{
		initialize("");
	}

     /**
	 * Soldier Constructor -  calls the super constructor, and we call {@link initialize} method with personalNum
	 * to initialize this object
	 * @param id Soldier's identifier
	 * @param name Soldier's name 
	 * @param surname Soldier's family name 
	 * @param tel Soldier's telephone
	 * @param personalNum Soldier's personal number
	 */
	 
	public Soldier(String ID,String f_name, String l_name,String phone,String personalNum)
	{
		super(ID,f_name,l_name,phone);
		initialize(personalNum);
	}
	/**
	 * This method responsible setting Frame's size using {@link javax.swing.JFrame#setSize},
	 * populate the {@link Person#datas}'s {@link java.util.ArrayList}
	 * build UI for this data using {@link Person#buildRow}
	 * and center them using {@link #addToCenter}
	 * @param data data to hold
	 */

	private void initialize(String data)
	{
		setSize(450,250);
		addData(data);
		addToCenter(buildRow(data,"Personal No"));
	}
	/**
	 * The method check the key if it equals to the id data in person by calling {@link Person#match}
	 * or equal to personal number (last field in the {@link java.util.ArrayList})
	 * @param key value to match id or personal number
	 * @return true if the two Strings are equal
	 * See method: {@link java.lang.String#equals}
	 * See method: {@link Person#getData}
	 * See method: {@link Person#getDataSize}
	 */

@Override
	public boolean match(String key) 
	{
		String personalNumber = getData(getDataSize() -1);
		return super.match(key) || ( personalNumber   != null && personalNumber.equals(key));
	}
	/**
	 * This Override method calls {@link Person#ifDataIsValid} to check {@link Person}'s fields
	 * check if Personal Number exists in Database using {@link Person#ifExist}
	 * if exists return false.
	 * call the overloaded method {@link #ifDataIsValid} to check the Personal Number valid
	 * if one of the fields is invalid, the method returns false
	 * @return true if all fields are valid else false 
	 */


@Override
	protected boolean ifDataIsValid() 
	{
		boolean valid = super.ifDataIsValid();
		if(!valid) return false;
		
		String textFieldData = getText(getDataSize() -1);
		if(!ifExist( textFieldData,"Personal No."))
			return false;
			
		return ifDataIsValid("^[ROC]\\/[1-9]\\d{6}$",textFieldData,  getDataSize()-1);
	}
}