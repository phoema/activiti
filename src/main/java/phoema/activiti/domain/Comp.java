package phoema.activiti.domain;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 用户信息.
 * @author 
 * @version v.0.1
 */
@Entity
//@Data // lombock
@Getter @Setter
@JsonInclude(Include.NON_NULL) //jackson null不序列化

public class Comp implements Serializable{
    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long compId;
	private String compName;
	@OneToMany(mappedBy = "comp", targetEntity = Person.class)
	private List<Person> people;
	
	public Comp(String compName)
	{this.compName = compName;}
	
	public Comp() {
		
	}

  
}