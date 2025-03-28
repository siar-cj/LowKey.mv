package com.lowkey.userlistservice.controller;

import com.lowkey.userlistservice.model.UserList;
import com.lowkey.userlistservice.service.UserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userlists")
public class UserListController {

    @Autowired
    private UserListService userListService;

    // Fetch all user lists for a specific user (only accessible to the owner of the lists)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<UserList> getAllUserLists(@RequestHeader("username") String username) {
        return userListService.getAllUserListsByUsername(username);
    }

    // Fetch a specific user list by ID (accessible only to the owner or shared users)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{listId}")
    public UserList getUserListById(@PathVariable Long listId, @RequestHeader("username") String username) {
        return userListService.getUserListById(listId, username);
    }

    // Create a new user list (only accessible to logged-in users)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public UserList createUserList(@RequestBody UserList userList, @RequestHeader("username") String username) {
        return userListService.createUserList(userList, username);
    }

    // Update an existing user list (accessible only to the owner)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{listId}")
    public UserList updateUserList(
        @PathVariable Long listId,
        @RequestBody UserList userList,
        @RequestHeader("username") String username
    ) {
        return userListService.updateUserList(listId, userList, username);
    }

    // Delete a user list (accessible only to the owner)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{listId}")
    public void deleteUserList(@PathVariable Long listId, @RequestHeader("username") String username) {
        userListService.deleteUserList(listId, username);
    }

    // Share a user list with another user (only accessible to the owner)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{listId}/share")
    public String shareUserList(
        @PathVariable Long listId,
        @RequestParam String sharedWithUsername,
        @RequestHeader("username") String ownerUsername
    ) {
        return userListService.shareUserList(listId, ownerUsername, sharedWithUsername);
    }
}