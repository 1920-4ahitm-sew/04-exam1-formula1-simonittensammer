package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Team;

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
     * @param country des Rennens
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("winner/{country}")
    public Response findWinnerOfRace(@PathParam("country") String country) {

        Race race = (Race) this.em.createNamedQuery("Race.getByCountry").setParameter("COUNTRY", country).getSingleResult();
        Driver winner = (Driver) this.em.createNamedQuery("Result.getWinnerByRace").setParameter("RACE", race).getSingleResult();

        return Response.ok(winner).build();
    }


    @GET
    @Path("raceswon")
    public JsonObject racesWon(@QueryParam("team") String teamName) {
//Result.victoriesByIds
        int help = 0;

        Team team = (Team) this.em.createNamedQuery("Team.getByName").setParameter("NAME", teamName).getSingleResult();
        Driver driver1 = (Driver) this.em.createNamedQuery("Driver.getByTeam").setParameter("TEAM", team).getSingleResult();

        System.out.println(driver1);

//        JsonObject driverJson = Json.createObjectBuilder()
//                .add("driver", name)
//                .add("points", points)
//                .build();
//
//        return driverJson;

        return null;
    }

    @GET
    @Path("all")
    public JsonObject pointsOfEveryDriver() {
        long count = (long) em.createNamedQuery("Driver.amountOfDrivers").getSingleResult();

        System.out.println(count);

        for (int i = 0; i < 20; i++) {

        }

//        JsonObject driverJson = Json.createObjectBuilder()
//                .add("driver", name)
//                .add("points", points)
//                .build();
//
//        return driverJson;

        return null;
    }
}
