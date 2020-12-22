package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

public class MyIllegalArgumentExceptionMapper implements
        ExceptionMapper<MyIllegalArgumentException> {
    private static final Logger logger =
            Logger.getLogger("exceptions.MyIllegalArgumentExceptionMapper");
    @Override
    public Response toResponse(MyIllegalArgumentException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
    }
}