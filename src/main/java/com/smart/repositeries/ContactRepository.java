package com.smart.repositeries;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

		public List<Contact> findContactByUser(User user);
		
		@Query("from Contact as c where c.user.id =:userId")
		public Page<Contact> getContactsByUserId(@Param("userId") int usesrId,Pageable pageable);
		
		//for searching
		public List<Contact> findByNameContainingAndUser(String name,User user);
		public List<Contact> findByNameContaining(String name);
		
		@Query("from Contact as c where c.user.id=:userId and c.name like %:cName%")
		public List<Contact> getContactsByQuery(int userId,String cName);
}
