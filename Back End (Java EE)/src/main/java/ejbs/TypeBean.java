package ejbs;

import entities.Type;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TypeBean {
    @PersistenceContext
    EntityManager em;

    public void create(String name, String description){
        Type t = new Type(name,description);
        em.persist(t);
    }

    public List<Type> getAllTypes() {
        return (List<Type>) em.createNamedQuery("getAllTypes").getResultList();
    }
}
