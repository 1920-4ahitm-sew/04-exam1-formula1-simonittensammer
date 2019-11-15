package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("results")
public class ResultsEndpoint {


    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    @Path("")
    public JsonObject getPointsSumOfDriver(@QueryParam("name") String name) {
        System.out.println(name);
        return null;
    }

    /**
     * @param id des Rennens
     * @return
     */
    @Path("results/winner")
    public Response findWinnerOfRace(long id) {
        return null;
    }


    // Ergänzen Sie Ihre eigenen Methoden ...

}
