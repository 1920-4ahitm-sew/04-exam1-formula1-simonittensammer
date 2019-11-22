package at.htl.formula1.control;

import at.htl.formula1.boundary.ResultsRestClient;
import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Team;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@ApplicationScoped
public class InitBean {

    private static final String TEAM_FILE_NAME = "teams.csv";
    private static final String RACES_FILE_NAME = "races.csv";

    @PersistenceContext
    EntityManager em;

    @Inject
    ResultsRestClient client;

    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        readTeamsAndDriversFromFile(TEAM_FILE_NAME);
        readRacesFromFile(RACES_FILE_NAME);
        client.readResultsFromEndpoint();

    }

    /**
     * Einlesen der Datei "races.csv" und Speichern der Objekte in der Tabelle F1_RACE
     *
     * @param racesFileName
     */
    private void readRacesFromFile(String racesFileName) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            getClass()
                                    .getResourceAsStream(
                                            "/" + racesFileName),
                            StandardCharsets.UTF_8)
            );

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(";");
                // row[0]: id
                // row[1]: country
                // row[2]: date

                //String to LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate date = LocalDate.parse(row[2], formatter);

                Race currentRace;
                currentRace = new Race(Long.parseLong(row[0]), row[1], date);

                em.persist(currentRace);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Einlesen der Datei "teams.csv".
     * Das String-Array jeder einzelnen Zeile wird der Methode persistTeamAndDrivers(...)
     * übergeben
     *
     * @param teamFileName
     */
    private void readTeamsAndDriversFromFile(String teamFileName) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            getClass().getResourceAsStream("/" + teamFileName)
                    )
            );

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(";");
                persistTeamAndDrivers(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Es wird überprüft ob es das übergebene Team schon in der Tabelle F1_TEAM gibt.
     * Falls nicht, wird das Team in der Tabelle gespeichert.
     * Wenn es das Team schon gibt, dann liest man das Team aus der Tabelle und
     * erstellt ein Objekt (der Klasse Team).
     * Dieses Objekt wird verwendet, um die Fahrer mit Ihrem jeweiligen Team
     * in der Tabelle F!_DRIVER zu speichern.
     *
     * @param row String-Array mit den einzelnen Werten der csv-Datei
     */

    private void persistTeamAndDrivers(String[] row) {
        // row[0]: team
        // row[1]: driver1
        // row[2]: driver2

        Team currentTeam = new Team(row[0]);

        em.persist(currentTeam);

        em.persist(new Driver(row[1], currentTeam));
        em.persist(new Driver(row[2], currentTeam));
    }


}
