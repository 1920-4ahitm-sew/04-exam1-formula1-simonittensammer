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

    @PersistenceContext
    EntityManager em;

    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    @GET
    @Path("")
    public JsonObject getPointsSumOfDriver(@QueryParam("name") String name) {
        //Long id = (Long) this.em.createNamedQuery("Driver.getIdByName").setParameter("NAME", name).getSingleResult();
        Driver driver = (Driver) this.em.createNamedQuery("Driver.getByName").setParameter("NAME", name).getSingleResult();
        Long points = (Long) this.em.createNamedQuery("Result.getPointsSumOfDriver").setParameter("ID", driver).getSingleResult();

//        System.out.println(name + ": " + points);

        JsonObject driverJson = Json.createObjectBuilder()
                .add("driver", name)
                .add("points", points)
                .build();

        return driverJson;
    }

    /**
     * @param id des Rennens
     * @return
     */
    @Path("winner")
    public Response findWinnerOfRace(long id) {
        return null;
    }


    // Ergänzen Sie Ihre eigenen Methoden ...

}
