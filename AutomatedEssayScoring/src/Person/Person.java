package Person;

public class Person {
	private String name;
	private String surname;
	private int id;
	
	public Person() {
		// TODO Auto-generated constructor stub
	}
	public Person(int id) {
		setId(id);
		setName("Name");
		setSurname("Surname");
	}
	public Person(int id, String name, String surname) {
		setId(id);
		setName(name);
		setSurname(surname);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Name :" + getName() + "\nSurname :" + getSurname() + "\nID :" + getId();
	}
}
