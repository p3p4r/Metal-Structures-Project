package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

public class CatchAllExceptionMapper implements
        ExceptionMapper<MyEntityNotFoundException> {
    private static final Logger logger =
            Logger.getLogger("exceptions.CatchAllExceptionMapper");
    @Override
    public Response toResponse(MyEntityNotFoundException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);

        return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
    }
}