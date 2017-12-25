package phoema.activiti.domain;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

public class Person implements Serializable{
    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long personId;
	
	private String personName;
	
	@ManyToOne(targetEntity = Comp.class)
	private Comp comp;
	
	public Person() {
		
	}
	
	public Person(String personName) {
		this.personName = personName;
	}
  
}