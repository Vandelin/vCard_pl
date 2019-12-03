package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewService {

    private List<Worker> workers;

    public List<Worker> generate(URL url) {
        workers = new ArrayList<>();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            Worker worker = null;
            String helpLine;
            int id = 0;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("/user/profile.php?id=") && !inputLine.contains("img")) {
                    worker = new Worker();
                    inputLine = inputLine.replace(inputLine.substring(inputLine.indexOf("<a"), inputLine.indexOf("title")), "");
                    helpLine = inputLine.substring(inputLine.indexOf("<h3>") + 4, inputLine.indexOf("</a>"));
                    worker.setGivenName(helpLine.substring(0, helpLine.indexOf(" ")));
                    worker.setFamilyName(helpLine.substring(helpLine.indexOf(" ") + 1));
                    if (!inputLine.contains("nbsp")) {
                        worker.setTitle(inputLine.substring(inputLine.indexOf("h4") + 3, inputLine.indexOf(" </h4>")));
                    }

                }
                if (inputLine.contains("Afiliacja")) {
                    helpLine = in.readLine();
                    if (worker != null) {
                        worker.setInstitute(helpLine.substring(helpLine.indexOf("content") + 9, helpLine.indexOf("</span>")));
                        worker.setId(id++);
                    }
                    workers.add(worker);
                }

            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return workers;
    }
}
