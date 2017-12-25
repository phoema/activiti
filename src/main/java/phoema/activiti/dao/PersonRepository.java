package phoema.activiti.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import phoema.activiti.domain.Person;
 
/**
 * UserInfo持久化类;
 * @author Angel(QQ:412887952)
 * @version v.0.1
 */
public interface PersonRepository extends JpaRepository<Person,Long>{
   
    /**通过personName查找用户信息;*/
	public Person findByPersonName(String personName);    
}