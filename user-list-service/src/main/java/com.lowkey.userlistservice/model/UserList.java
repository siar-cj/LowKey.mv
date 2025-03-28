package com.lowkey.userlistservice.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the list (e.g., "Favorite Movies")

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // The user who owns the list

    @OneToMany(mappedBy = "userList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListPermission> permissions = new ArrayList<>(); // Permissions for shared users

    @ElementCollection
    @CollectionTable(name = "list_items", joinColumns = @JoinColumn(name = "list_id"))
    @Column(name = "item")
    private List<String> items = new ArrayList<>(); // Items in the list (e.g., movie titles)

    // Constructors
    public UserList() {}

    public UserList(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ListPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ListPermission> permissions) {
        this.permissions = permissions;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}