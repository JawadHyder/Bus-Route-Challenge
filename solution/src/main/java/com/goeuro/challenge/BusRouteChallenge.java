package com.goeuro.challenge;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.port;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goeuro.challenge.dto.ErrorResponseDto;
import com.goeuro.challenge.dto.ResponseDto;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.sound.sampled.Line;
import java.io.*;
import java.util.Map;
import java.util.Set;

public class BusRouteChallenge {

    private static final String DEP_SID = "dep_sid";
    private static final String ARR_SID = "arr_sid";
    private static final String STATION_DELIMITER = " ";

    public static void main( String[] args) {
        File dataFile;
        try {
            // Check if the path of file is valid and we can read from it.
            dataFile = new File(args[0]);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            bufferedReader.readLine();
            bufferedReader.close();
        }
        catch (ArrayIndexOutOfBoundsException | IOException ex) {
            throw new RuntimeException("This service needs valid path to the 'Bus Routes Data File' as an argument which can be read.");
        }

        port(8088);
        get("/api/direct", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                response.status(HttpStatus.OK_200);
                response.type("application/json");

                String dep = request.queryParams(DEP_SID);
                String arr = request.queryParams(ARR_SID);
                if (dep == null || arr == null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return mapObjToJson(new ErrorResponseDto("Missing required fields", DEP_SID + " and " + ARR_SID + " are required."));
                }

                ResponseDto responseDto = new ResponseDto(dep, arr);

                try {
                    long start = System.currentTimeMillis();
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
                    bufferedReader.readLine(); // ignoring the first line
                    String line;
                    while ((line = bufferedReader.readLine()) != null) { // only read one line into memory at a time
                        line += STATION_DELIMITER; // add a trailing delimiter.
                        // split and check for occurrence also works but no need in this situation. contains() is much efficient.
                        if (line.contains(STATION_DELIMITER + dep + STATION_DELIMITER)
                                && line.contains(STATION_DELIMITER + arr + STATION_DELIMITER)) { // this will not match the starting route id because it does not have space before it.
                            responseDto.setDirect_bus_route(true);
                            break;
                        }
                    }
                    bufferedReader.close();
                    long end = System.currentTimeMillis();
                    System.out.println("took " + (end - start) + " milliseconds to return results");
                } catch (IOException ex) {
                    response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                    System.out.println("[ERROR] IOException occurred. Cannot read from data file, please restart the service.");
                    return mapObjToJson(new ErrorResponseDto("Internal Server Error", "Please check server logs for details."));
                }

                return mapObjToJson(responseDto);

            }
        });
    }

    public static String mapObjToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException ex) {
            throw new RuntimeException("IOException occurred while converting object to JSON");
        }
    }
}
