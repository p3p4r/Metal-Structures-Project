package ws;

import dtos.FileDTO;
import dtos.ProjectDTO;
import dtos.StructureDTO;
import ejbs.ProjectBean;
import entities.Project;
import entities.ProjectStatus;
import entities.File;
import entities.Structure;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/projects") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class ProjectService {

    @EJB
    private ProjectBean projectBean;

    private ProjectDTO toDTONoFiles(Project project) {
        return new ProjectDTO(
            project.getName(),
            project.getClient().getUsername(),
            project.getDraftsman().getUsername(),
            project.getProjectStatus()
        );
    }

    private FileDTO toDTO(File file) {
        return new FileDTO(
                file.getId(),
                file.getFileName(),
                file.getObservations(),
                file.getFilePath(),
                file.getProject().getName()
        );
    }

    private StructureDTO toDTO(Structure structure) {
        return new StructureDTO(
                structure.getId(),
                structure.getName(),
                structure.getDimensions(),
                structure.getCalculationParameters(),
                structure.getType().getName(),
                structure.getProject().getName(),
                structure.getDraftsman().getName(),
                structure.getCreate_at(),
                structure.getUpdate_at()
        );
    }

    private List<StructureDTO> structuresToDtos(List<Structure> structures) {
        return structures.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // List all projects
    /*private List<ProjectDTO> projectsToDTOs(List<Project> projects) {
        return projects.stream().map(this::toDTONoFiles).collect(Collectors.toList());
    }*/

    private List<FileDTO> filesToDtos(List<File> files) {
        return files.stream().map(this::toDTO).collect(Collectors.toList());
    }


    private ProjectDTO toDTO(Project project){
        return new ProjectDTO(
                toDTONoFiles(project),
                filesToDtos(project.getFiles()),
                structuresToDtos(project.getStructures())
        );
    }


    private List<ProjectDTO> toDTOs(List<Project> projects){
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/status") // means: the relative url path is “/api/students/”
    public List<ProjectStatus> getAllProjectStatusWS() {
        return projectBean.getAllStatus();
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<ProjectDTO> getAllProjectsWS() {
        return toDTOs(projectBean.getAllProjects());
    }

    @GET
    @Path("/{name}")
    public Response getProjectDetails(@PathParam("name") String name)
        throws MyEntityExistsException {
        Project project = projectBean.findProject(name);

        return Response.status(Response.Status.OK)
            .entity(toDTO(project))
            .build();

    }

    @POST
    @Path("/")
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
    @Path("/{name}")
    public Response deleteProject (@PathParam("name") String name)
        throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
        if (name == null) throw new MyIllegalArgumentException("Please insert a name eg. 'projects/project Y'.");

        projectBean.delete(name);

        return Response.status(Response.Status.OK)
            .entity("Deleted Successfully.")
            .build();
    }

    // Todo - Atualizar  Estuturas do projeto
    @PUT
    @Path("/{name}")
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
}
