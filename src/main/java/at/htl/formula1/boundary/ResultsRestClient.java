package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;

import javax.json.*;
import javax.json.stream.JsonParser;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ResultsRestClient {


    public static final String RESULTS_ENDPOINT = "http://vm90.htl-leonding.ac.at/results";
    private Client client;
    private WebTarget target;

    @PersistenceContext
    EntityManager em;

    /**
     * Vom RestEndpoint werden alle Result abgeholt und in ein JsonArray gespeichert.
     * Dieses JsonArray wird an die Methode persistResult(...) übergeben
     */
    public void readResultsFromEndpoint() {
        this.client = ClientBuilder.newClient();
        this.target = client.target(this.RESULTS_ENDPOINT);

        Response response = this.target.request(MediaType.APPLICATION_JSON).get();
        JsonArray payload = response.readEntity(JsonArray.class);
//        System.out.println("payload = " + payload);

        persistResult(payload);
    }

    /**
     * Das JsonArray wird durchlaufen (iteriert). Man erhäjt dabei Objekte vom
     * Typ JsonValue. diese werden mit der Methode .asJsonObject() in ein
     * JsonObject umgewandelt.
     * <p>
     * zB:
     * for (JsonValue jsonValue : resultsJson) {
     * JsonObject resultJson = jsonValue.asJsonObject();
     * ...
     * <p>
     * Mit den entsprechenden get-Methoden können nun die einzelnen Werte
     * (raceNo, position und driverFullName) ausgelesen werden.
     * <p>
     * Mit dem driverFullName wird der entsprechende Driver aus der Datenbank ausgelesen.
     * <p>
     * Dieser Driver wird dann dem neu erstellten Result-Objekt übergeben
     *
     * @param resultsJson
     */
    @Transactional
    void persistResult(JsonArray resultsJson) {
        long raceNo;
        String driverName;
        int position;

        for (JsonValue jsonValue : resultsJson) {
            JsonObject resultJson = jsonValue.asJsonObject();

            raceNo = resultJson.getInt("raceNo");
            driverName = resultJson.getString("driverFullName");
            position = resultJson.getInt("position");

            em.persist(new Result(
                    (Race) this.em.createNamedQuery("Race.getById").setParameter("ID", raceNo).getSingleResult(),
                    position,
                    (Driver) this.em.createNamedQuery("Driver.getByName").setParameter("NAME", driverName).getSingleResult()
            ));
        }
    }

}
