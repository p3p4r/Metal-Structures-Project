package entities;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllClients",
                query = "SELECT c FROM Client c ORDER BY c.username" // JPQL
        ),
        @NamedQuery(
                name = "getClientProjects",
                query = "SELECT p FROM Project p, Client c WHERE p.client = c.username"
        )
})
public class Client extends User {

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    List<Project> projects;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    List<Adjudication> adjudications;

    public Client(){
        super();
        this.projects = new LinkedList<>();
        adjudications = new LinkedList<>();
    }

    // Para ser assim o cliente não extende de User

    public Client(String username, String password, String name, String address,String  contactPerson, String email) {
        super(username, password, name, address, contactPerson, email);
        projects = new LinkedList<>();
        adjudications = new LinkedList<>();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Adjudication> getAdjudications() {
        return adjudications;
    }

    public void setAdjudications(List<Adjudication> adjudications) {
        this.adjudications = adjudications;
    }

    public void addProject(Project p){ projects.add(p); }

    public void removeProject(Project p){
        projects.remove(p);
    }

    public void addAdjudication(Adjudication a) {
        adjudications.add(a);
    }
}
