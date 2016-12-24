package main;

public class ContactList {
	private String firstname1;
	private String lastname1;
	private String phone1;
	private String email1;
	private String address1;

	
	public ContactList() {
		firstname1 = "";
		lastname1 = "";
		phone1 = "";
		email1 = "";
		address1 = "";

	}

	public ContactList(String firstname1, String lastname1, String phone1, String email1,  String address1) {
		this.firstname1 = firstname1;
		this.lastname1 = lastname1;
		this.phone1 = phone1;
		this.email1 = email1;
		this.address1 = address1;

	}

	//setters
	public void setFirstname1(String firstname1) {
		this.firstname1 = firstname1;
	}
	public void setLastname1(String lastname1) {
		this.lastname1 = lastname1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	//getters
	
	public String getFirstname1() {
		
		return firstname1;

	}
	
	public String getLastname1() {
		return lastname1;
	}

	public String getPhone1() {
		return phone1;
	}

	public String getEmail1() {
		return email1;
	}

	public String getAddress1() {
		return address1;
	}
}
