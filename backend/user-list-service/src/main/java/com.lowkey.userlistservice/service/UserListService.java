package com.lowkey.userlistservice.service;

import com.lowkey.userlistservice.model.User;
import com.lowkey.userlistservice.model.UserList;
import com.lowkey.userlistservice.model.ListPermission;
import com.lowkey.userlistservice.model.PermissionType;
import com.lowkey.userlistservice.repository.UserListRepository;
import com.lowkey.userlistservice.repository.ListPermissionRepository;
import com.lowkey.userlistservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserListService {

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private ListPermissionRepository listPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch all user lists owned by a specific user
    public List<UserList> getAllUserListsByUsername(String username) {
        return userListRepository.findByOwnerUsername(username);
    }

    // Fetch a specific user list by ID, validating access permissions
    public UserList getUserListById(Long listId, String username) {
        UserList userList = userListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("User list not found."));

        // Validate access permissions
        if (!userList.getOwner().getUsername().equals(username) &&
                listPermissionRepository.findByUserUsernameAndUserListId(username, listId) == null) {
            throw new AccessDeniedException("You do not have permission to access this list.");
        }

        return userList;
    }

    // Create a new user list for a user
    public UserList createUserList(UserList userList, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        userList.setOwner(user);
        return userListRepository.save(userList);
    }

    // Update an existing user list, validating ownership
    public UserList updateUserList(Long listId, UserList updatedList, String username) {
        UserList existingList = userListRepository.findByIdAndOwnerUsername(listId, username);
        if (existingList == null) {
            throw new AccessDeniedException("You do not have permission to update this list.");
        }

        existingList.setName(updatedList.getName());
        existingList.setItems(updatedList.getItems());
        return userListRepository.save(existingList);
    }

    // Delete a user list, validating ownership
    public void deleteUserList(Long listId, String username) {
        UserList existingList = userListRepository.findByIdAndOwnerUsername(listId, username);
        if (existingList == null) {
            throw new AccessDeniedException("You do not have permission to delete this list.");
        }

        userListRepository.delete(existingList);
    }

    // Share a user list with another user by adding permissions
    public String shareUserList(Long listId, String ownerUsername, String sharedWithUsername) {
        UserList userList = userListRepository.findByIdAndOwnerUsername(listId, ownerUsername);
        if (userList == null) {
            throw new IllegalArgumentException("User list not found or you do not have permission to share this list.");
        }

        User sharedWithUser = userRepository.findByUsername(sharedWithUsername);
        if (sharedWithUser == null) {
            throw new IllegalArgumentException("The user you are trying to share with does not exist.");
        }

        ListPermission permission = new ListPermission(userList, sharedWithUser, PermissionType.READ);
        listPermissionRepository.save(permission);

        return "List shared successfully with " + sharedWithUsername;
    }
}