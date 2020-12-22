package ws;

import dtos.AdjudicationDTO;
import dtos.ClientDTO;
import dtos.ProjectDTO;
import ejbs.AdjudicationBean;
import ejbs.ClientBean;
import ejbs.ProjectBean;
import entities.Client;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/clients") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class ClientService {
    @EJB
    private ClientBean clientBean;
    @EJB
    private ProjectBean projectBean;
    @EJB
    private AdjudicationBean adjudicationBean;

    private ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getUsername(),
                client.getPassword(),
                client.getName(),
                client.getAddress(),
                client.getContactPerson(),
                client.getEmail()
        );
    }
    // converts an entire list of entities into a list of DTOs
    private List<ClientDTO> toDTOs(List<Client> clients) {
        return clients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    @RolesAllowed({"Client","Draftsman"})
    public List<ClientDTO> getAllClientsWS() {
        return toDTOs(clientBean.getAllClients());
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


    @GET
    @Path("/{client}")
    @RolesAllowed({"Client"})
    public List<ProjectDTO> getProjectsByClientUsername(@PathParam("client") String clientUsername) throws MyEntityExistsException, MyIllegalArgumentException {

        Client client = clientBean.findClient(clientUsername);
        if (client == null)
            throw new MyIllegalArgumentException("Please insert a name eg. 'clients/clientName'");

        return projectsToDTOs(projectBean.getProjectsByClientUsername(clientUsername));
    }


    @POST
    @Path("/adjudication")
    public Response createAdjudication(AdjudicationDTO adjudicationDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        adjudicationBean.create(
                adjudicationDTO.getClient(),
                adjudicationDTO.getStructure(),
                adjudicationDTO.getProject()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Created Successfully.")
                .build();
    }
}
