package ws;


import dtos.MaterialDTO;
import dtos.ProjectDTO;
import dtos.TypeDTO;
import ejbs.TypeBean;
import entities.Material;
import entities.Project;
import entities.Type;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.stream.Collectors;

@Path("/types") // relative url web path for this service

public class TypeService {

    @EJB
    private TypeBean typeBean;

    private TypeDTO toDTO(Type type) {
        return new TypeDTO(
            type.getName(),
            type.getDescription()
        );
    }

    private List<TypeDTO> typesToDTOs(List<Type> types) {
        return types.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // List all materials
    @Path("/")
    public List<TypeDTO> getAllTypesWS() {
        return typesToDTOs(typeBean.getAllTypes());
    }
}
