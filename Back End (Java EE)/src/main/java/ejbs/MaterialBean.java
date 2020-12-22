package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class MaterialBean {
    @PersistenceContext
    EntityManager em;

    MaterialState status;

    public void create(String name,String nameFamily, String nameType, String supplierUsername,MaterialState status) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Family family = em.find(Family.class, nameFamily);
        if(family == null)  throw new MyEntityNotFoundException("Family with Family: " + nameFamily + " already exists");

        Type type = em.find(Type.class, nameType);
        if(type == null) throw new MyEntityNotFoundException("Type with type: " + nameType + " not found.");

        Supplier supplier = em.find(Supplier.class, supplierUsername);
        if(supplier == null) throw new MyEntityNotFoundException("Supplier with username: " + supplierUsername + " not found.");

        if(status == null){
           status = status.visible;
        }

        try {
            Material p = new Material(name, family, type, supplier, status);

            em.persist(p);
        }catch (ConstraintViolationException e) {
                throw new MyConstraintViolationException(e);
        }
    }

    public List<Material> getAllMaterials() {
        return (List<Material>) em.createNamedQuery("getAllMaterials").getResultList();
    }

    public List<Material> getMaterialsOfSupplier(String username){
        return em.createQuery("SELECT m FROM Material m WHERE m.supplier.username LIKE :supplierUsername")
                .setParameter("supplierUsername", username)
                .getResultList();
    }

    public void updateMaterial(long id,String name,String familyName, String typeName,String supplierUsername) throws MyEntityExistsException, MyConstraintViolationException {

        Material material = em.find(Material.class, id);
        if(name == null)  throw new MyEntityExistsException("Material with id "+ name +" not found.");

        Family family = em.find(Family.class, familyName);
        if(family == null) throw new MyEntityExistsException("Family with name "+ familyName +" not found.");

        Type type = em.find(Type.class, typeName);
        if(type == null) throw new MyEntityExistsException("Type with name "+ typeName +" not found.");

        Supplier supplier = em.find(Supplier.class, supplierUsername);
        if(family == null) throw new MyEntityExistsException("Supplier with username "+ supplierUsername +" not found.");

        try{
            em.lock(material, LockModeType.OPTIMISTIC);
            material.setName(name);
            material.setFamily(family);
            material.setType(type);
            material.setSupplier(supplier);
        } catch (
                ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(long id) throws MyEntityExistsException, MyConstraintViolationException {

        Material material = em.find(Material.class, id);
        if(material == null) throw new MyEntityExistsException("Material with id "+  material +" not found.");

        try{
            //em.remove(material);
            material.setStatus(status.hidden);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Material findMaterial(long id) throws MyEntityExistsException {
        Material material = em.find(Material.class, id);

        if(material == null) throw new MyEntityExistsException("Material with id "+  id +" not found.");

        return material;
    }

}
