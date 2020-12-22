package ejbs;

import entities.Client;
import entities.Project;
import entities.Supplier;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ClientBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String address, String contactPerson, String email) throws MyEntityExistsException, MyConstraintViolationException {
        Client c = em.find(Client.class, username);
        if(c != null){
            throw new MyEntityExistsException("Client with username: " + username + " already exists");
        }

        /*ContactPerson cp = em.find(ContactPerson.class, contactPerson);
        if(cp == null)  throw new MyEntityExistsException("Project with name: " + cp.getNome() + " not found");*/

        try{
            c = new Client(username,password,name,address,contactPerson,email);
            //cp.addUser(c);
            em.persist(c);
        }catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Client> getAllClients() {
        return (List<Client>) em.createNamedQuery("getAllClients").getResultList();
    }


    public Client findClient(String clientUsername) throws MyEntityExistsException {

        Client client = em.find(Client.class, clientUsername);
        if (client == null)
            throw new MyEntityExistsException("Client with username "+ clientUsername +" not found.");

        return client;
    }
}
