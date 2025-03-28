package com.lowkey.userlistservice.repository;

import com.lowkey.userlistservice.model.UserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {

    // Fetch all lists owned by a specific user
    List<UserList> findByOwnerUsername(String ownerUsername);

    // Fetch a list by its ID and the owner's username (ensures the owner is valid)
    UserList findByIdAndOwnerUsername(Long id, String ownerUsername);
}