
/**
 * This Class extends {@link Person} and responsible to handle Student Entity
 *
 */
public class Student extends Person
{
	
	/**
	 * Student Constructor -  calls the super constructor, and we call {@link initialize} method with null
	 * and initialize this object
	 */
	public Student()
	{
		initialize("");
	}
		/**
	 * Soldier Constructor -  calls the super constructor, and call {@link initialize} method with studentID
	 * to initialize this object
	 * @param id Student's identifier
	 * @param name Student's name 
	 * @param surname Student's family name 
	 * @param tel Student's telephone
	 * @param studentID Student's student ID
	 */
	public Student(String id,String name, String surname,String tel,String studentID)
	{
		super(id,name,surname,tel);
		initialize(studentID);
	}
	
	/**
	 * The method responsible setting Frame's size using {@link javax.swing.JFrame#setSize},
	 * populate the {@link Person#datas}'s {@link java.util.ArrayList}
	 * build UI for this data using {@link Person#buildRow}
	 * and center them using {@link #addToCenter}
	 * @param data data to hold
	 */
	private void initialize(String data)
	{
		setSize(450,250);
		addData(data);	
		addToCenter(buildRow(data,"Student ID"));
	}
	/**
	 * The method check the key if it equals to the id data in person by calling {@link Person#match}
	 * or equal to Student ID (last field in the {@link java.util.ArrayList})
	 * @param key value to match id or Student ID
	 * @return true if the two Strings are equal
	 * See method: {@link java.lang.String#equals}
	 * See method: {@link Person#getData}
	 * See method: {@link Person#getDataSize}
	 */
	 
	@Override
	public boolean match(String key)
	{
		String student_id = getData(getDataSize() -1);
		//start from char 4
		return super.match(key) || (student_id != null && student_id.substring(4).equals(key));
	}
	/**
	 * The method calls {@link Person#ifDataIsValid} to check {@link Person}'s fields
	 * check if Student ID exists in Database using {@link Person#ifExist}
	 * if exists return false.
	 * call the overloaded method {@link #ifDataIsValid} to check the Student ID validation
	 * if one of the fields is invalid, the method returns false
	 * @return true if all fields are valid else false 
	 */
	 
	@Override
	protected boolean ifDataIsValid()
	{
		boolean isValid = super.ifDataIsValid();
		if(!isValid) return false;
		
		String textFieldData = getText(getDataSize() -1);
		if(!ifExist( textFieldData,"Student ID"))
			return false;
			
		return ifDataIsValid("^[A-Z]{3}\\/[1-9]\\d{4}$",textFieldData,  getDataSize()-1);
	}
}