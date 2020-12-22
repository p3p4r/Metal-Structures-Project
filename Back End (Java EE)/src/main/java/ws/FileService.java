package ws;

import dtos.FileDTO;
import ejbs.FileBean;
import ejbs.ProjectBean;
import entities.Project;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/file")
public class FileService {
    @EJB
    private FileBean fileBean;
    @EJB
    private ProjectBean projectBean;

    FileDTO toDTO(entities.File file) {
        return new FileDTO(
                file.getId(),
                file.getFileName(),
                file.getObservations(),
                file.getFilePath(),
                file.getProject().getName()
        );
    }

    List<FileDTO> filesToDTOs(List<entities.File> documents) {
        return documents.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private String getFilename(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
        System.out.println("Written: " + filename);
    }

    //Todo:fazer
    @GET
    @Path("/project/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileDTO> getDocuments(@PathParam("name") String name) throws
            MyEntityNotFoundException, MyEntityExistsException {
        Project project = projectBean.findProject(name);
        if (project == null) {
            throw new MyEntityNotFoundException("Project with name: " + project + " not found");
        }
        return filesToDTOs(fileBean.getProjectFiles(name));
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(MultipartFormDataInput input) throws IOException, MyEntityExistsException, MyEntityNotFoundException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        String projectName = uploadForm.get("project").get(0).getBodyAsString();
        String observations = uploadForm.get("observations").get(0).getBodyAsString();

        Project project = projectBean.findProject(projectName);
        if (project == null) {
            throw new MyEntityNotFoundException("Project with name " + projectName + " not found.");
        }
            for (InputPart inputPart : inputParts) {
                try {
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    String filename = getFilename(header);
                    // convert the uploaded file to inputstream
                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    String path = System.getProperty("user.home") + File.separator + "uploads";
                    File customDir = new File(path);
                    if (!customDir.exists()) {
                        customDir.mkdir();
                    }
                    String filepath = customDir.getCanonicalPath() + File.separator + filename;
                    writeFile(bytes, filepath);

                    fileBean.create(filename, observations, path, project.getName());

                    return Response.status(200).entity("Uploaded file name : " + filename).build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @GET
        @Path("download/{id}")
        @Produces(MediaType.APPLICATION_OCTET_STREAM)
        public Response download (@PathParam("id") int id) throws MyEntityExistsException {
            entities.File f = fileBean.findDocument(id);
            File fileDownload = new File(f.getFilePath() + File.separator + f.getFileName());
            Response.ResponseBuilder response = Response.ok((Object) fileDownload);
            response.header("Content-Disposition", "attachment;filename=" + f.getFileName());
            return response.build();
        }

    }
