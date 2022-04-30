package com.kaisengroup.bbrmanagementwork.controller.repository;

import java.util.List;

import javax.websocket.Session;

import com.kaisengroup.bbrmanagementwork.controller.model.Work;

import org.hibernate.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    
    //@Query("select * from Work where type == true")
    List<Work> findByStatusTrue();
    List<Work> findByStatusFalse();
    

}
