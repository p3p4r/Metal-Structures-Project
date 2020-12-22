package ejbs;

import entities.Material;
import entities.Project;
import entities.Variante;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Stateless
public class VarianteBean {

    @PersistenceContext
    EntityManager em;

    public void create(int codigo, int idMaterial, String name, double weff_p, double weff_n, double ar, double sigmaC){
        Material material = em.find(Material.class, idMaterial);
        Variante p = new Variante(codigo, material, name, weff_p, weff_n, ar, sigmaC);
        material.addVariante(p);
        em.persist(p);
    }

    public Variante findVariante(int code) throws MyEntityExistsException {
        Variante variante = em.find(Variante.class, code);

        if(variante == null) throw new MyEntityExistsException("Variante with code "+  variante +" not found.");

        return variante;
    }

    public Variante getVariante(int codigo){
        return em.find(Variante.class, codigo);
    }

    public List<Variante> getAllVariants() {
        return (List<Variante>) em.createNamedQuery("getAllVariants").getResultList();
    }


    public void enrollSupplierInVariante(String supplierUsername, int codigo) throws MyEntityNotFoundException {
        Supplier s = em.find(Supplier.class, supplierUsername);
        if(s == null) throw new MyEntityNotFoundException("Supplier not found.");

        Variante v = em.find(Variante.class,codigo);
        if(v == null) throw new MyEntityNotFoundException("Variante not found.");

        if(!v.getSuppliers().contains(v) && !s.getVariantes().equals(v)) {
            v.addSupplier(s);
            s.addVariante(v);
        }

    }
}
