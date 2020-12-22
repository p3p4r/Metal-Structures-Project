package ws;

import dtos.*;
import ejbs.*;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/suppliers") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class SupplierService {
    @EJB
    private SupplierBean supplierBean;
    @EJB
    private MaterialBean materialBean;
    @EJB
    private TypeBean typeBean;
    @EJB
    private AdjudicationBean adjudicationBean;

    @EJB
    private VarianteBean varianteBean;

    private SupplierDTO toDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getUsername(),
                supplier.getPassword(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getContactPerson(),
                supplier.getEmail()
        );
    }
    // converts an entire list of entities into a list of DTOs
    private List<SupplierDTO> toDTOs(List<Supplier> suppliers) {
        return suppliers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /*
    // Only to admin
    @GET
    @Path("/")
    public List<SupplierDTO> getAllSuppliersWS() {
        return toDTOs(supplierBean.getAllSuppliers());
    }*/
    @POST
    @Path("/materials")
    public Response createNewMaterial(MaterialDTO materialDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        materialBean.create(
                //materialDTO.getId(),
                materialDTO.getName(),
                materialDTO.getFamily(),
                materialDTO.getType(),
                materialDTO.getSupplier(),
                materialDTO.getStatus()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Created Successfully.")
                .build();
    }

    private MaterialDTO toDTONoVariants(Material material) {
        return new MaterialDTO(
                material.getId(),
                material.getName(),
                material.getFamily().getName(),
                material.getType().getName(),
                material.getSupplier().getName(),
                material.getStatus()
        );
    }

    private List<MaterialDTO> toDTOsNoVariants(List<Material> materials) {
        return materials.stream().map(this::toDTONoVariants).collect(Collectors.toList());
    }

    // double weff_p, double weff_n, double ar, double sigmaC ) {
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

    private List<VarianteDTO> variantsToDTOs(List<Variante> variantes) {
        return variantes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private MaterialDTO toDTO(Material material) {
        return new MaterialDTO(
                toDTONoVariants(material),
                variantsToDTOs(material.getVariantes())
        );
    }


    @GET // List all materials
    @Path("/materials")
    public List<MaterialDTO> getAllMaterialsWS() {
        return toDTOsNoVariants(materialBean.getAllMaterials());
    }

    // Todo - Falta obter os adjudicados
    @GET
    @Path("/{supplier}")
    public List<Material> getMaterialsBySupplierUsername(@PathParam("supplier") String username)
            throws MyEntityExistsException, MyIllegalArgumentException {

        Supplier supplier = supplierBean.findSupplier(username);
        if (supplier == null) throw new MyIllegalArgumentException("Please insert a name eg. 'materials/supplierName'");

        return materialBean.getMaterialsOfSupplier(username);
    }

    @GET // List all variants
    @Path("/variants")
    public List<VarianteDTO> getAllVariantsWS() {
        return variantsToDTOs(varianteBean.getAllVariants());
    }

    @GET
    @Path("/materials/{id}")
    public Response geMaterialDetails(@PathParam("id") long id)
            throws MyEntityExistsException, MyIllegalArgumentException {

        Material material = materialBean.findMaterial(id);
        if (material == null) throw new MyIllegalArgumentException("Please insert a name eg. 'materials/materialNameGoesHere'");


        return Response.status(Response.Status.OK)
                .entity(toDTO(material))
                .build();

    }

    @PUT
    @Path("/materials/{id}")
    public Response updateMaterial (MaterialDTO materialDTO, @PathParam("id") long id)
            throws  MyEntityExistsException, MyConstraintViolationException {

        materialBean.updateMaterial(
                id,
                materialDTO.getName(),
                materialDTO.getFamily(),
                materialDTO.getType(),
                materialDTO.getSupplier()
        );

        return Response.status(Response.Status.OK)
                .entity("Updated Successfully.")
                .build();
    }

    @DELETE
    @Path("/materials/{id}")
    public Response deleteMaterial (@PathParam("id") long id)
            throws MyEntityExistsException, MyConstraintViolationException, MyIllegalArgumentException {

        materialBean.delete(id);

        return Response.status(Response.Status.OK)
                .entity("Deleted Successfully.")
                .build();
    }

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
    @Path("/materials/types")
    public List<TypeDTO> getAllTypesWS() {
        return typesToDTOs(typeBean.getAllTypes());
    }


    private AdjudicationDTO toDTONoVariants(Adjudication adjudication) {
        return new AdjudicationDTO(
                adjudication.getId(),
                adjudication.getAdjudicate(),
                adjudication.getObservation(),
                adjudication.getClient().getUsername(),
                adjudication.getStructure().getId(),
                adjudication.getProject().getName()
        );
    }

    private List<AdjudicationDTO> toDTOsAdjudications(List<Adjudication> adjudications) {
        return adjudications.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AdjudicationDTO toDTO(Adjudication adjudication) {
        return new AdjudicationDTO(
                toDTONoVariants(adjudication),
                variantsToDTOs(adjudication.getVariantes())
        );
    }

    @GET
    @Path("/adjudications")
    public List<AdjudicationDTO> getAllAdjudicationsWS() {
        return toDTOsAdjudications(adjudicationBean.getAllAdjudications());
    }

    @POST
    @Path("/variant")
    public Response createNewVariante(VarianteDTO varianteDTO) throws MyEntityNotFoundException {

        varianteBean.create(
                varianteDTO.getCodigo(),
                varianteDTO.getMaterial(),
                varianteDTO.getNome(),
                varianteDTO.getWeff_p(),
                varianteDTO.getWeff_n(),
                varianteDTO.getAr(),
                varianteDTO.getSigmaC()
        );

        varianteBean.enrollSupplierInVariante(varianteDTO.getSuppliers().get(0).getUsername(),varianteDTO.getCodigo());

        return Response.status(Response.Status.CREATED)
                .entity("Created Successfully.")
                .build();
    }
}
