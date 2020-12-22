package ejbs;

import entities.Client;
import entities.Draftsman;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class DraftsmanBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String address, String contactPerson, String email) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Draftsman d = em.find(Draftsman.class, username);
        if(d != null){
            throw new MyEntityExistsException("Draftsman with username: " + username + " already exists");
        }
        /*ContactPerson cp = em.find(ContactPerson.class, contactPerson);
        if(cp != null)  throw new MyEntityExistsException("Contact Person with name: " + cp.getNome() + " already exists");*/

        try{
            d = new Draftsman(username,password,name,address,contactPerson,email);
            //cp.addUser(d);
            em.persist(d);
        }catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Draftsman> getAllDraftsmen() {
        return (List<Draftsman>) em.createNamedQuery("getAllDraftsmen").getResultList();
    }


}
