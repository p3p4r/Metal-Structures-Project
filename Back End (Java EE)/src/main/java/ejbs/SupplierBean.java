package ejbs;


import entities.Material;
import entities.Supplier;
import entities.Variante;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class SupplierBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String address, String contactPerson, String email) throws MyEntityExistsException, MyConstraintViolationException {
        Supplier s = em.find(Supplier.class, username);
        if(s != null){
            throw new MyEntityExistsException("Supplier with username: " + username + " already exists");
        }
        /*ContactPerson cp = em.find(ContactPerson.class, contactPerson);
        if(cp != null)  throw new MyEntityExistsException("Contact Person with name: " + cp.getNome() + " already exists");*/

        try{
            s = new Supplier(username,password,name,address,contactPerson,email);
            //cp.addUser(s);
            em.persist(s);
        }catch(
        ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Supplier> getAllSuppliers() {
        return (List<Supplier>) em.createNamedQuery("getAllSuppliers").getResultList();
    }

    // Find project
    public Supplier findSupplier(String username) throws MyEntityExistsException {
        Supplier supplier = em.find(Supplier.class, username);

        if(supplier == null) throw new MyEntityExistsException("Supplier with name "+  username +" not found.");

        return supplier;
    }

}
