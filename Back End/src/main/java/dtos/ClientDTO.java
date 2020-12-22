package dtos;

import entities.Project;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ClientDTO implements Serializable {
    String username, password, name, address, contactPerson,email;

    List<ProjectDTO> projects;
    public ClientDTO(){this.projects = new LinkedList<>();}

    public ClientDTO(String username, String password, String name, String address, String contactPerson, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.contactPerson = contactPerson;
        this.email = email;
        projects = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }
}