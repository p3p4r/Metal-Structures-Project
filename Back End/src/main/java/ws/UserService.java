package ws;

import dtos.ClientDTO;
import dtos.ProjectDTO;
import dtos.UserDTO;
import ejbs.ClientBean;
import ejbs.DraftsmanBean;
import ejbs.SupplierBean;
import ejbs.UserBean;
import entities.Client;
import entities.Supplier;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/user")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class UserService {
    @EJB
    private UserBean userBean;

    @EJB
    private ClientBean clientBean;
    @EJB
    private DraftsmanBean draftsmanBean;
    @EJB
    private SupplierBean supplierBean;

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getAddress(),
                user.getContactPerson(),
                user.getEmail()
        );
    }
    // converts an entire list of entities into a list of DTOs
    private List<UserDTO> toDTOs(List<User> users) {
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/{type}")
    public Response createNewUser(UserDTO userDTO, @PathParam("type") int typeUser)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        // Change in order to only the admin can create some roles
        switch (typeUser){
            case 0: // Client
                clientBean.create(
                        userDTO.getUsername(),
                        userDTO.getPassword(),
                        userDTO.getName(),
                        userDTO.getAddress(),
                        userDTO.getContactPerson(),
                        userDTO.getEmail()
                );
                break;
            case 1:  // Draftsman
                draftsmanBean.create(
                        userDTO.getUsername(),
                        userDTO.getPassword(),
                        userDTO.getName(),
                        userDTO.getAddress(),
                        userDTO.getContactPerson(),
                        userDTO.getEmail()
                );
                break;
            case 2: // Supplier
                supplierBean.create(
                        userDTO.getUsername(),
                        userDTO.getPassword(),
                        userDTO.getName(),
                        userDTO.getAddress(),
                        userDTO.getContactPerson(),
                        userDTO.getEmail()
                );
                break;
            default:
                clientBean.create(
                        userDTO.getUsername(),
                        userDTO.getPassword(),
                        userDTO.getName(),
                        userDTO.getAddress(),
                        userDTO.getContactPerson(),
                        userDTO.getEmail()
                );
                break;
        }

        return Response.status(Response.Status.CREATED)
                .entity("Created Successfully.")
                .build();
    }

    @POST
    @Path("/forgot")
    public Response ForgotPassword(UserDTO userDTO) throws MyEntityNotFoundException {

        String username = userDTO.getUsername();
        String email = userDTO.getEmail();

        List user = userBean.find(username, email);

        if (user.size() != 0){
            User userDetails = (User) user.get(0);
            // Generate New Password & Set as new New User Password
            String password = userBean.generateNewPassword(username);
            username = userDetails.getUsername();
            email = userDetails.getEmail();
            // Send Email With the Information
            userBean.sendEmailWithCredentials(email, username, password);
        }


        return Response.status(Response.Status.OK)
                .entity("Send Successfully.")
                .build();
    }

}
