package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class ProjectBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    private EmailBean emailBean;

    public void create(String name, String clientUsername, String draftsmanUsername,ProjectStatus status) throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        Project p = em.find(Project.class, name);
        if(p != null)  throw new MyEntityExistsException("Project with name: " + name + " already exists");

        Client client = em.find(Client.class, clientUsername);
        if(client == null) throw new MyEntityNotFoundException("Client with username: " + clientUsername + " not found.");

        Draftsman draftsman = em.find(Draftsman.class, draftsmanUsername);
        if(draftsman == null) throw new MyEntityNotFoundException("Draftsman with username: " + clientUsername + " not found.");

        if(status == null) throw new MyEntityExistsException("Status "+ status +" not found.");

        try{
            p = new Project(name,client,draftsman,status);
            client.addProject(p);
            draftsman.addProject(p);
            em.persist(p);
        } catch (
            ConstraintViolationException e) {
                throw new MyConstraintViolationException(e);
        }
    }

    public List<Project> getAllProjects() {
        return (List<Project>) em.createNamedQuery("getAllProjects").getResultList();
    }

    // Find project
    public Project findProject(String name) throws MyEntityExistsException {
        Project project = em.find(Project.class, name);

        if(project == null) throw new MyEntityExistsException("Project with name "+  name +" not found.");

        return project;
    }

    //find project by client_username
    public Project findProjectByClientUsername(String client_username) throws MyEntityExistsException {
        Project project = em.find(Project.class, client_username);

        if(project == null) throw new MyEntityExistsException("Project with name "+ client_username +" not found.");

        return project;
    }


    public void delete(String name) throws MyEntityExistsException, MyConstraintViolationException {
        if(name == null) throw new MyEntityExistsException("Project with username "+ name +" not found.");

        Project project = em.find(Project.class, name);
        if(project == null) throw new MyEntityExistsException("Project with name "+  name +" not found.");

        try{
            em.remove(project);
        } catch (
                ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void updateProject(String urlName, String name, String clientUsername, String draftsmanUsername,ProjectStatus status) throws MyEntityExistsException, MyConstraintViolationException {
        Project project = em.find(Project.class, urlName);
        if(project == null)  throw new MyEntityExistsException("Project with name "+ urlName +" not found.");

        Client client = em.find(Client.class, clientUsername);
        if(client ==null) throw new MyEntityExistsException("Client with username "+ clientUsername +" not found.");

        Draftsman draftsman = em.find(Draftsman.class, draftsmanUsername);
        if(draftsman == null){
            project.getDraftsman();
        }

        if(status == null) throw new MyEntityExistsException("Status "+ status +" not found.");

        try{
            em.lock(project,LockModeType.OPTIMISTIC);

            //project.setName(name); // error if is id
            project.setClient(client);
            project.setDraftsman(draftsman);

            if (status == ProjectStatus.ON_APPROVAL){

               /* List<Structure> structures = project.getStructures();

                if (structures.size() == 0){ // no Structures to aproved
                    throw new MyConstraintViolationException("No structures to approve in project" + project.getName());
                }

                for (Structure structure : structures) {
                    // Create Adjudication for each structure
                    try {
                        adjudicationBean.create(project.getClient().getUsername(), structure.getId());
                    } catch (MyEntityNotFoundException e) {

                    }
                }*/

                // send email to Client
                emailBean.send(
                        client.getEmail(),
                        "[Website Notification] Request of Approval for"+ project,
                        "The " + project.getName() + "was changed to approval, check the project in https:localhost:3000/"+
                                clientUsername+"/"+project.getName()
                );
            }

            project.setProjectStatus(status);
        } catch (
                ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Project> getProjectsByClientUsername(String username) {
        return em.createQuery("SELECT p FROM Project p WHERE p.client.username LIKE :clientUsername")
            .setParameter("clientUsername", username)
            .getResultList();
    }

    public List<Project> getProjectsByDraftsmanUsername(String username) throws MyEntityNotFoundException {
        Draftsman draftsman = em.find(Draftsman.class, username);
        if(draftsman == null) throw new MyEntityNotFoundException("Project not found.");

        return em.createQuery("SELECT p FROM Project p WHERE p.draftsman.username LIKE :draftsmanUsername")
                .setParameter("draftsmanUsername", username)
                .getResultList();
    }

    public List<ProjectStatus> getAllStatus() {
        return Arrays.asList(ProjectStatus.values());
    }

    //Todo: (testar)
    public void addStructures(String name, List<Integer> structure) throws MyEntityNotFoundException, MyEntityExistsException {
        Project p = em.find(Project.class, name);
        if(p == null) throw new MyEntityNotFoundException("Project not found.");

        for (Integer str: structure) {
            Structure s = em.find(Structure.class, str);
            if(s == null) throw new MyEntityExistsException("Structure with name "+ structure +" not found.");

            if (p.getStructures().contains(s) == false) {
                if (!s.getProject().equals(s) && !p.getStructures().equals(s)) {
                    s.setProject(p);
                    p.addStructure(s);
                }
            }
        }
    }
}
