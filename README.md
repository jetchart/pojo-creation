### POJO Creation
##### Tool to create POJO parsing a sql file.

**Program arguments:** `[0] [1]`<br>

**[0]:** Indicates file or folder to parse. If it's a folder, it will find all sql files into. It's optional (./ by default)<br>
**[1]:** Indicates folder target where POJOS will be created. It's optional (./ by default)<br>

**Run example:**
`/home/jetchart/sql/program.sql /home/jetchart/sql/generated/`

Using the following sql file:<br>
`CREATE TABLE ORACLE.PERSON_HEADER (
   ID   		INT           NOT NULL,
   FIRST_NAME 	VARCHAR (20)  NOT NULL,
   AGE  		INT           NOT NULL,
   ADDRESS  	INT (25) ,
   SALARY   	DECIMAL (18, 2),       
   PRIMARY KEY (ID),
   FOREIGN KEY (ADDRESS) REFERENCES PERSON_ADDRESS(ADDRESS)
);`


Generates the following POJO:<br>

`package FILL_PACKAGE_PATH_HERE;

import javax.persistence.*;

@Entity
@Table(name = "PERSON_HEADER", schema = "ORACLE")
public class PersonHeaderEntity {

	@Id
	@NotNullable
	@Column(name = "ID")
	private Integer id;

	@NotNullable
	@Column(name = "FIRST_NAME", length = 20)
	private String firstName;

	@NotNullable
	@Column(name = "AGE")
	private Integer age;

	@ManyToOne
	@JoinColumn(name = "ADDRESS")
	private AddressEntity address;

	@Column(name = "SALARY", precision = 18, scale = 2)
	private BigDecimal salary;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public AddressEntity getAddress() {
		return this.address;
	}

	public void setAddress (AddressEntity address) {
		this.address = address;
	}

	public BigDecimal getSalary() {
		return this.salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

}`
