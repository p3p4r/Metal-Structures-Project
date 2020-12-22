package ws;

import dtos.MaterialDTO;
import dtos.SimulacaoDTO;
import dtos.VarianteDTO;
import ejbs.SimulacaoBean;
import ejbs.VarianteBean;
import entities.*;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/simulator") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SimulationService {
    @EJB
    private SimulacaoBean simulacaoBean;
    @EJB
    private VarianteBean varianteBean;

    @POST
    @Path("/")
    public Response createSimulation(SimulacaoDTO simulacaoDTO) throws MyEntityNotFoundException {

        boolean isValidSimulation = simulacaoBean.simulaVariante(
                simulacaoDTO.getNb(),
                simulacaoDTO.getLVao(),
                simulacaoDTO.getQ(),
                simulacaoDTO.getVariantCode()
        );

        return Response.status(Response.Status.OK)
                .entity(isValidSimulation)
                .build();
    }

    private MaterialDTO toDTO(Material material) {

        return new MaterialDTO(
                material.getId(),
                material.getName(),
                material.getFamily().getName(),
                material.getType().getName(),
                material.getSupplier().getName(),
                material.getStatus()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<MaterialDTO> materialsToDTOs(List<Material> materials) {
        return materials.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/{material}")
    public Response getSimulationMaterials(@PathParam("material") String name,SimulacaoDTO simulacaoDTO) throws MyEntityNotFoundException {

        List simulationList= simulacaoBean.getSimulationMaterials(
                simulacaoDTO.getNb(),
                simulacaoDTO.getLVao(),
                simulacaoDTO.getQ(),
                simulacaoDTO.getVariantCode(),
                name
        );

        return Response.status(Response.Status.OK)
                .entity(materialsToDTOs(simulationList))
                .build();
    }

    @POST
    @Path("variants/{variants}")
    public Response getSimulationVariants(@PathParam("variants") String name,SimulacaoDTO simulacaoDTO) throws MyEntityNotFoundException {

        List simulationList= simulacaoBean.getSimulationVariants(
                simulacaoDTO.getNb(),
                simulacaoDTO.getLVao(),
                simulacaoDTO.getQ(),
                simulacaoDTO.getVariantCode(),
                name
        );

        return Response.status(Response.Status.OK)
                .entity(toDTOs(simulationList))
                .build();
    }

    private VarianteDTO toDTO(Variante variante) {

        return new VarianteDTO(
                variante.getCodigo(),
                variante.getMaterial().getId(),
                variante.getNome(),
                variante.getWeff_p(),
                variante.getWeff_n(),
                variante.getAr(),
                variante.getSigmaC()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<VarianteDTO> toDTOs(List<Variante> variantes) {
        return variantes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/variants")
    public List<VarianteDTO> getAllVariantsWS() {
        return toDTOs(varianteBean.getAllVariants());
    }

    @GET
    @Path("/variants/{code}")
    public Response getVarianteDetails(@PathParam("code") int code)
            throws MyEntityExistsException{
        Variante variante = varianteBean.findVariante(code);

        return Response.status(Response.Status.OK)
                .entity(toDTO(variante))
                .build();
    }
}
