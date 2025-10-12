package com.xtraCoder.SecurityApp.SecurityApplication.repository;


import com.xtraCoder.SecurityApp.SecurityApplication.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {


}
