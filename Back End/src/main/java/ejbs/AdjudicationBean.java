package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import java.util.LinkedList;
import java.util.List;

@Stateless
public class AdjudicationBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    private EmailBean emailBean;


    //Para o projetista criar adjudicaçao(coloca adjudicação em onApproval
    public void create(String clientUsername, int structureId, String projectName) throws MyEntityNotFoundException, MyConstraintViolationException {
        Client c = em.find(Client.class, clientUsername);
        if(c == null) throw new MyEntityNotFoundException("Client with username: " + clientUsername + " not found.");

        Structure s = em.find(Structure.class, structureId);
        if(s == null)  throw new MyEntityNotFoundException("Structure with name: " + structureId + "not found.");

        Project p = em.find(Project.class, projectName);
        if(p == null)  throw new MyEntityNotFoundException("Project with name: " + projectName + "not found.");

        //if(typeOfAdjudication == null) throw new MyEntityNotFoundException("Type Of Adjudication "+ typeOfAdjudication +" not found.");
        TypeOfAdjudication typeOfAdjudication = TypeOfAdjudication.OnApproval;
        String observation = null;

        try{
            Adjudication a = new Adjudication(typeOfAdjudication,observation,c,s,p);
            s.addAdjudication(a);
            c.addAdjudication(a);
            p.addAdjudication(a);
            em.persist(a);
        }catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Adjudication> getAllAdjudications() {
        return (List<Adjudication>) em.createNamedQuery("getAllAdjudications").getResultList();
    }


    public void enrolAdjudicationInlVariante(int adjudicationId,int codigoVariante) throws MyEntityExistsException, MyEntityNotFoundException {
        Adjudication adjudication = em.find(Adjudication.class, adjudicationId);
        if(adjudication == null) throw new MyEntityNotFoundException("Client with username "+ adjudicationId +" not found.");

        Variante variante = em.find(Variante.class, codigoVariante);
        if(variante == null) throw new MyEntityExistsException("Variante with code "+ codigoVariante +" not found.");

        if(!variante.getAdjudications().contains(adjudication) && !variante.getStructures().contains(adjudication.getStructure())){
            adjudication.addVariante(variante);
            variante.addAdjudication(adjudication);
        }
    }

    public List<Adjudication> getAdjudicationId(String structureName, String projectName){
        return em.createQuery("SELECT a.id FROM Adjudication a INNER JOIN Structure s ON a.structure.id=s.id WHERE a.project.name LIKE :projectName AND s.name LIKE :structureName")
                .setParameter("structureName", structureName)
                .setParameter("projectName", projectName)
                .setMaxResults(3)
                .getResultList();
    }

    //para o cliente aprovar ou adjudicar estrutura e variantes
    public void update(int id, TypeOfAdjudication adjudicate, String observation, String client,  int structure, List<Integer> variantes) throws MyConstraintViolationException, MyEntityExistsException {
        Adjudication adjudication= em.find(Adjudication.class, id);
        if(adjudication ==null) throw new MyConstraintViolationException("Adjudication with id "+ id +" not found.");

        Client c = em.find(Client.class, client);
        if(client ==null) throw new MyEntityExistsException("Client with username "+ client +" not found.");

        Structure s = em.find(Structure.class, structure);
        if(s ==null) throw new MyEntityExistsException("Draftsman with username "+ structure +" not found.");

        if(observation == null){
            adjudication.getObservation();
        };

        if(adjudicate == null) throw new MyConstraintViolationException("TypeOfAdjudication "+ adjudicate +" not found.");


        try{
            adjudication.setAdjudicate(adjudicate);
            adjudication.setObservation(observation);
            adjudication.setClient(c);
            adjudication.setStructure(s);

            //Project project = s.getProject();
            if (adjudicate == TypeOfAdjudication.NotAdjudicated){
                for (Integer var: variantes) {

                    Variante v = em.find(Variante.class, var);
                    if(v == null) throw new MyEntityExistsException("Variante with name "+ v +" not found.");

                    if(!v.getAdjudications().contains(v) && !adjudication.getVariantes().equals(v)) {
                        v.addAdjudication(adjudication);
                        adjudication.addVariante(v);
                    }
                }

                // Set Project Status
                //project.setProjectStatus(ProjectStatus.DENIED);
            }else{
                List<String> emailList = new LinkedList<>();;
                for (Variante v: s.getVariantes()) {
                    if(!v.getAdjudications().contains(v) && !adjudication.getVariantes().equals(v)) {
                        v.addAdjudication(adjudication);
                        adjudication.addVariante(v);
                    }

                    for (Supplier supplier : v.getSuppliers()) {
                        String email = supplier.getEmail();
                        if(!emailList.contains(email)){
                            emailList.add(email);
                        }
                    }
                }

                // TODO - Testar
                // Send Email to Supplier
                for (String email : emailList) {
                    emailBean.send(
                            email,
                            "[Website Notification] Variants Adjudicated.",
                            "Notficition from website: One or more Varints has been Approved by a Client, check your new variants adjudications in website.pt/adjudication)"
                    );
                }



                // Set Project Status
                //project.setProjectStatus(ProjectStatus.APPROVED);
            }
        } catch (
                ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }
}
