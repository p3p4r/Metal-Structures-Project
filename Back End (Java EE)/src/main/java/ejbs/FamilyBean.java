package ejbs;

import entities.Family;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FamilyBean {
    @PersistenceContext
    EntityManager em;

    public void create(String name, String description){
        Family c = new Family(name,description);
        em.persist(c);
    }

    public List<Family> getAllFamilies() {
        return (List<Family>) em.createNamedQuery("getAllFamilies").getResultList();
    }
}
