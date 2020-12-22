package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class ContactPerson {
    //TODO: ver melhot para que serve
    @Id
    int id;
    @NotNull
    String nome;
    @NotNull
    int telefone;
    @NotNull
    String email;
    @ManyToOne
    @JoinColumn(name = "CLIENT_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    User user;

    public ContactPerson(int id, @NotNull String nome, @NotNull int telefone, @NotNull String email, @NotNull User user) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.user = user;
    }

    public ContactPerson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User client) {
        this.user = user;
    }

    public void addUser(User u) {
        this.user = u;
    }
}
