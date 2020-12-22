package ws;

import dtos.*;
import ejbs.AdjudicationBean;
import ejbs.FamilyBean;
import ejbs.StructureBean;
import ejbs.VarianteBean;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/structures") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class StructureService {
    //Todo: Testar tudo
    @EJB
    private StructureBean structureBean;
    @EJB
    private VarianteBean varianteBean;
    @EJB
    private AdjudicationBean adjudicationBean;
    @EJB
    private FamilyBean familyBean;

    private StructureDTO toDTOnoLists(Structure structure){
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

    private FamilyDTO toDTOFamily(Family family) {
        return new FamilyDTO(
                family.getName(),
                family.getDescription()
        );
    }

    // List all Families
    private List<FamilyDTO> familiesToDTOs(List<Family> families) {
        return families.stream().map(this::toDTOFamily).collect(Collectors.toList());
    }



    private VarianteDTO toDTOnoLists(Variante variante){
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

    private VarianteDTO toDTO(Variante variante){
        return new VarianteDTO(
                toDTOnoLists(variante),
                adjudicationsToDTOs(variante.getAdjudications())
        );
    }
    private List<VarianteDTO> variantesToDTOs(List<Variante> variantes) {
        return variantes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AdjudicationDTO toDTO(Adjudication adjudication){
        return new AdjudicationDTO(
                adjudication.getId(),
                adjudication.getAdjudicate(),
                adjudication.getObservation(),
                adjudication.getClient().getUsername(),
                adjudication.getStructure().getId(),
                adjudication.getProject().getName()
        );
    }
    private List<AdjudicationDTO> adjudicationsToDTOs(List<Adjudication> adjudications) {
        return adjudications.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private StructureDTO toDTO(Structure structure){
        return new StructureDTO(
                toDTOnoLists(structure),
                variantesToDTOs(structure.getVariantes()),
                adjudicationsToDTOs(structure.getAdjudications())
        );
    }

    /*private List<StructureDTO> toDTOsNoLists(List<Structure> structure){
        return structure.stream().map(this::toDTOnoLists).collect(Collectors.toList());
    }*/
    private List<StructureDTO> toDTOs(List<Structure> structure){
        return structure.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @GET
    @Path("/variants")
    public List<VarianteDTO> getAllVariantsWS(){
        return variantesToDTOs(varianteBean.getAllVariants());
    }


    @GET
    @Path("/")
    public List<StructureDTO> getAllStructureWS(){
        return toDTOs(structureBean.getAllStructures());
    }

    @GET
    @Path("/{id}")
    public Response getProjectDetails(@PathParam("id") int id) throws MyEntityExistsException, MyIllegalArgumentException {
        Structure structure = structureBean.getStructure(id);
        if (structure == null) throw new MyIllegalArgumentException("Please insert a name eg. 'projects/project Y'.");

        return Response.status(Response.Status.OK)
                .entity(toDTO(structure))
                .build();

    }

    @GET
    @Path("/name/{name}")
    public Response getStructureDetails(@PathParam("name") String name) throws MyEntityExistsException {
        List structure = structureBean.findStructure(name);

        return Response.status(Response.Status.OK)
                .entity(toDTOs(structure))
                .build();
    }

    @POST
    @Path("/")
    public Response createNewStructure(StructureDTO structureDTO) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        structureBean.create(
             //structureDTO.getId(),
             structureDTO.getName(),
             structureDTO.getDimensions(),
             structureDTO.getCalculationParameters(),
             structureDTO.getType(),
             structureDTO.getProject(),
             structureDTO.getDraftsman()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Created Successfully.")
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEstructure(@PathParam("id") int id) throws  MyEntityExistsException, MyConstraintViolationException {

        structureBean.deleteStructure(id);

        return Response.status(Response.Status.OK)
                .entity("Deleted Successfully.")
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateStructure(StructureDTO structureDTO,@PathParam("id") int id) throws MyEntityExistsException, MyConstraintViolationException {
        structureBean.updateStructure(
                id,
                structureDTO.getName(),
                structureDTO.getDimensions(),
                structureDTO.getCalculationParameters(),
                structureDTO.getType(),
                structureDTO.getProject(),
                structureDTO.getDraftsman()
        );

        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }

    @PUT
    @Path("/addVarint/{id}")
    public Response addListVariantsToStructure(StructureDTO structureDTO,@PathParam("id") int id) throws MyEntityExistsException, MyEntityNotFoundException {
        List<Integer> list = new ArrayList<>();
        for (VarianteDTO v: structureDTO.getVariantes()) {
            list.add(v.getCodigo());
        }
        structureBean.enrollListVariantsInStructure(
            id,
            list
        );
        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }

    @POST
    @Path("/variant/{id}")
    public Response addVariantToStructure(StructureDTO structureDTO,@PathParam("id") int id) throws MyEntityExistsException, MyEntityNotFoundException {

        structureBean.enrollVarianteInStructure(
                id,
                structureDTO.getId()
        );

        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
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

    @PUT
    @Path("/adjudication/{id}")
    public Response updateAdjudication(AdjudicationDTO adjudicationDTO,@PathParam("id") int id) throws MyEntityExistsException, MyConstraintViolationException {
        List<Integer> list = new ArrayList<>();
        for (VarianteDTO v: adjudicationDTO.getVariantes()) {
            list.add(v.getCodigo());
        }
        adjudicationBean.update(
                adjudicationDTO.getId(),
                adjudicationDTO.getAdjudicate(),
                adjudicationDTO.getObservation(),
                adjudicationDTO.getClient(),
                adjudicationDTO.getStructure(),
                list
        );

        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }

    @POST
    @Path("/adjudication/{id}")
    public Response addAdjudication(StructureDTO structureDTO,@PathParam("id") int id) throws MyEntityExistsException, MyEntityNotFoundException {
        List<Integer> list = new ArrayList<>();
        for (AdjudicationDTO a: structureDTO.getAdjudications()) {
            list.add(a.getId());
        }
        structureBean.enrollAdjudicacaoInStructure(
                id,
                list
        );
        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/families")
    @RolesAllowed({"Draftsman","Supplier"})
    public List<FamilyDTO> getAllFamiliesWS() {
        return familiesToDTOs(familyBean.getAllFamilies());
    }

    @GET
    @Path("/project/{projectCode}")
    @RolesAllowed({"Client", "Draftsman"})
    public List<StructureDTO> getStructuresByProject(@PathParam("projectCode") String projectCode) throws MyEntityExistsException, MyIllegalArgumentException {

        List<Structure> structure = structureBean.getStructuresByProject(projectCode);
        if (structure == null)
            throw new MyIllegalArgumentException("Please insert a name eg. 'projects/projectName'");

        return toDTOs(structureBean.getStructuresByProject(projectCode));
    }

}
