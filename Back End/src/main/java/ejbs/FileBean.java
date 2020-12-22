package ejbs;

import entities.Client;
import entities.File;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class FileBean {
    @PersistenceContext
    EntityManager em;

    public File findDocument(int id) throws MyEntityExistsException {
        File f = em.find(File.class, id);
        if (f == null)
            throw new MyEntityExistsException("File with id: "+ id +" not found.");

        return f;
    }

    //Todo: fazer
    public void create(String filename, String observation ,String filepath, String project) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Project p = em.find(Project.class, project);
        if(p == null){
            throw new MyEntityNotFoundException("Client with username: " + project + " already exists");
        }

        try{
            File f = new File(filename,observation,filepath,p);
            p.addFile(f);
            em.persist(f);
        }catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<File> getProjectFiles(String name) throws MyEntityExistsException {
        Project project = em.find(Project.class, name);
        if(project == null) throw new MyEntityExistsException("Project with name "+  name +" not found.");

        List<File> files = project.getFiles();

        return files;
    }

}
