package ws;

import dtos.*;
import ejbs.DraftsmanBean;
import ejbs.FamilyBean;
import ejbs.ProjectBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/draftsmen") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class DraftsmanService {
    @EJB
    private DraftsmanBean draftsmanBean;
    @EJB
    private ProjectBean projectBean;

    private DraftsmanDTO toDTO(Draftsman draftsman) {
        return new DraftsmanDTO(
                draftsman.getUsername(),
                draftsman.getPassword(),
                draftsman.getName(),
                draftsman.getAddress(),
                draftsman.getContactPerson(),
                draftsman.getEmail()
        );
    }
    // converts an entire list of entities into a list of DTOs
    private List<DraftsmanDTO> toDTOs(List<Draftsman> draftsmen) {
        return draftsmen.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<DraftsmanDTO> getAllDraftsmenWS() {
        return toDTOs(draftsmanBean.getAllDraftsmen());
    }

    private ProjectDTO toDTOProject(Project project) {
        return new ProjectDTO(
                project.getName(),
                project.getClient().getUsername(),
                project.getDraftsman().getUsername(),
                project.getProjectStatus()
        );
    }
    // List all projects
    private List<ProjectDTO> projectsToDTOs(List<Project> projects) {
        return projects.stream().map(this::toDTOProject).collect(Collectors.toList());
    }

    //List Project of Specific Drafstman
    @GET
    @Path("/{username}")
    public List<ProjectDTO> getProjectsByUsername(@PathParam("username") String username) throws MyEntityExistsException, MyIllegalArgumentException, MyEntityNotFoundException {

        return projectsToDTOs(projectBean.getProjectsByDraftsmanUsername(username));
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/projects")
    public List<ProjectDTO> getAllProjectsWS() {
        return projectsToDTOs(projectBean.getAllProjects());
    }

    @GET
    @Path("/projects/{name}")
    public Response getProjectDetails(@PathParam("name") String name)
            throws MyEntityExistsException, MyIllegalArgumentException {
        Project project = projectBean.findProject(name);
        if (project == null) throw new MyIllegalArgumentException("Please insert a name eg. 'projects/project Y'.");

        return Response.status(Response.Status.OK)
                .entity(toDTOProject(project))
                .build();

    }

    @POST
    @Path("/projects")
    public Response createNewProject(ProjectDTO projectDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        projectBean.create(
                projectDTO.getName(),
                projectDTO.getClientUsername(),
                projectDTO.getDraftsmanUsername(),
                projectDTO.getStatus()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Created Successfully.")
                .build();
    }

    // Remover projeto
    @DELETE
    @Path("/projects/{name}")
    public Response deleteProject (@PathParam("name") String name)
            throws MyEntityExistsException, MyConstraintViolationException, MyIllegalArgumentException {
        if (name == null) throw new MyIllegalArgumentException("Please insert a name eg. 'projects/project Y'.");

        projectBean.delete(name);

        return Response.status(Response.Status.OK)
                .entity("Deleted Successfully.")
                .build();
    }

    // Todo - Atualizar  Estuturas do projeto
    @PUT
    @Path("/projects/{name}")
    public Response updateProject (ProjectDTO projectDTO, @PathParam("name") String name)
            throws MyEntityExistsException, MyConstraintViolationException, MyIllegalArgumentException {

        if (name == null) throw new MyIllegalArgumentException("Please insert a name eg. 'projects/project Y'.");

        projectBean.updateProject(
                name,
                projectDTO.getName(),
                projectDTO.getClientUsername(),
                projectDTO.getDraftsmanUsername(),
                projectDTO.getStatus()
        );

        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }


    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/projects/status")
    public List<ProjectStatus> getAllProjectStatusWS() {
        return projectBean.getAllStatus();
    }

    @PUT
    @Path("/projects/structure/{name}")
    public Response addEstructuresToProject(@PathParam("name") String name, ProjectDTO projectDTO) throws MyIllegalArgumentException, MyEntityNotFoundException, MyEntityExistsException {

        if (name == null) throw new MyIllegalArgumentException("Please insert a name eg. 'projects/project Y'.");
        List<Integer> list = new ArrayList<Integer>();
        for (StructureDTO s : projectDTO.getStructures()) {
            list.add(s.getId());
        }
        projectBean.addStructures(
                name,
                list
        );
        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }
}
