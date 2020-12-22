package ejbs;

import dtos.FamilyDTO;
import dtos.VarianteDTO;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Stateless
public class StructureBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private AdjudicationBean adjudicationBean;

    public void create(String name, String dimensions, String calculationParameters, String typeName, String projectName, String draftsmanName) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        //Structure s = em.find(Structure.class, id);
        //if(s != null)  throw new MyEntityExistsException("Structure already exists");

        Type t = em.find(Type.class, typeName);
        if(t == null) throw new MyEntityNotFoundException("Type with name: " + typeName + " not found.");

        Project p = em.find(Project.class, projectName);
        if(p == null) throw new MyEntityNotFoundException("Project with name: " + projectName + " not found.");

        Draftsman d = em.find(Draftsman.class, draftsmanName);
        if(d == null) throw new MyEntityNotFoundException("Draftsman with username: " + draftsmanName + " not found.");

        //data de hoje
        Date date = new Date();

        try {
            Structure s = new Structure(name, dimensions, calculationParameters, t, p, d, date, null);
            t.addStructure(s); // add this structure to Type
            p.addStructure(s); // add this structure to Project
            d.addStructure(s); // add this structure to Draftsman
            em.persist(s);
        }catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Structure getStructure(int id) throws MyEntityExistsException {
        Structure s = em.find(Structure.class, id);
        if(s == null){
            throw new MyEntityExistsException("Structure not found.");
        }
        return s;
    }

    public List<Structure> getAllStructures() {
        return (List<Structure>) em.createNamedQuery("getAllStructures").getResultList();
    }

    public List findStructure(String name) throws MyEntityExistsException {
        return  em.createQuery(
                "SELECT s FROM Structure s WHERE s.name LIKE :name")
                .setParameter("name", "%"+name+"%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Structure> getStructuresByProject(String name) {
        return em.createQuery("SELECT s FROM Structure s WHERE s.project.name LIKE :project_code")
            .setParameter("project_code", name)
            .getResultList();
    }

    public void updateStructure(int id, String name, String dimensions, String calculationParameters, String typeName, String projectName, String draftsmanUsername) throws MyEntityExistsException, MyConstraintViolationException {
        Structure s = em.find(Structure.class, id);
        if(s == null)  throw new MyEntityExistsException("Structure not found");
        Type t = null;
        if(typeName != null) {
            t = em.find(Type.class, typeName);
            if (t == null) throw new MyEntityExistsException("Type with name: " + typeName + " not found.");
        }
        Project p = null;
        if (projectName != null) {
            p = em.find(Project.class, projectName);
            if (p == null) throw new MyEntityExistsException("Project with name: " + projectName + " not found.");
        }
        Draftsman d = null;
        if (draftsmanUsername != null) {
            d = em.find(Draftsman.class, draftsmanUsername);
            if (d == null) throw new MyEntityExistsException("Draftsman with username: " + draftsmanUsername + " not found.");
        }
        try {
            //em.lock(s, LockModeType.OPTIMISTIC);
            if (name != null) {
                s.setName(name);
            }
            if (dimensions != null){
                s.setDimensions(dimensions);
            }
            if(calculationParameters != null) {
                s.setCalculationParameters(calculationParameters);
            }
            if (typeName != null) {
                s.getType().removeStructure(s);
                s.setType(t);
            }
            if (projectName != null){
                s.getProject().removeStructure(s);
                s.setProject(p);
            }
            if (draftsmanUsername != null) {
                s.getDraftsman().removeStructure(s);
                s.setDraftsman(d);
            }
            s.setCreate_at(s.getCreate_at());
            s.setUpdate_at(new Date());
        }catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void deleteStructure(int id) throws MyEntityExistsException, MyConstraintViolationException {
        Structure s = em.find(Structure.class, id);
        if(s == null) throw new MyEntityExistsException("Project with username "+ s.getName() +" not found.");

        try{
            em.remove(s);
            s.getType().removeStructure(s);
            s.getProject().removeStructure(s);
            s.getDraftsman().removeStructure(s);
        } catch (
                ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    //Todo: Ver se e necessario rolls (TESTAR)
    //variantes
    public void enrollListVariantsInStructure(int structure, List<Integer> variante) throws MyEntityExistsException, MyEntityNotFoundException {
        Structure s = em.find(Structure.class, structure);
        if(s == null) throw new MyEntityNotFoundException("Structure not found.");

        for (Integer var: variante) {
            Variante v = em.find(Variante.class, var);
            if(v == null) throw new MyEntityExistsException("Variante with name "+ variante +" not found.");

            if(!v.getStructures().contains(v) && !s.getVariantes().equals(v)) {
                v.addStructure(s);
                s.addVariante(v);
            }
        }
    }
    //adjudicação
    public void enrollAdjudicacaoInStructure(int structure, List<Integer> adjudication) throws MyEntityExistsException, MyEntityNotFoundException {
        Structure s = em.find(Structure.class, structure);
        if(s == null) throw new MyEntityNotFoundException("Structure not found.");

        for (Integer var: adjudication) {
            Adjudication a = em.find(Adjudication.class, adjudication);
            if(a == null) throw new MyEntityExistsException("Variante with name "+ adjudication +" not found.");

            if (!a.getStructure().equals(a) && !s.getAdjudications().equals(a)) {
                a.setStructure(s);
                s.addAdjudication(a);
            }
        }
    }

    public void enrollVarianteInStructure(int variantCode,int structureId) throws MyEntityNotFoundException {
        Variante variante = em.find(Variante.class, variantCode);
        if(variante == null) throw new MyEntityNotFoundException("Variant with code "+ variantCode +" not found.");

        Structure structure = em.find(Structure.class, structureId);
        if(structure == null) throw new MyEntityNotFoundException("Structure with id "+ structureId +" not found.");

        if(!variante.getStructures().contains(structure) && !structure.getVariantes().contains(variante)){
            variante.addStructure(structure);
            structure.addVariante(variante);
        }
    }
}
