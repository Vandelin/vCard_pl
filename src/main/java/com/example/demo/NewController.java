package com.example.demo;

import ezvcard.VCard;
import ezvcard.property.StructuredName;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
@CrossOrigin
public class NewController {

    List<Worker> users;
    private URL url;
    @Autowired
    private NewService newService;

    @GetMapping({"", "/"})
    public String index(){
        ModelAndView mv = new ModelAndView("book/form");
        mv.addObject("work", new Worker());
        return "index";
    }

    @GetMapping(value = "/search/name", params = "name")
    public String search(@RequestParam String name, Model model) {
        try {

            try {

                url = new URL("https://adm.edu.p.lodz.pl/user/users.php?search=" + name);
                users = newService.generate(url);
                model.addAttribute("workersList", users);
                model.addAttribute("name", name);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return "search";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "search";
    }

    @GetMapping("/download/{id}")
    public String download(@PathVariable Integer id, HttpServletResponse response, Model model) {
        for (Worker worker : users) {
            if (worker.getId() == id) {
                VCard vcard = new VCard();
                StructuredName n = new StructuredName();
                n.setFamily(worker.getFamilyName());
                n.setGiven(worker.getGivenName());
                n.getPrefixes().add(worker.getTitle());
                vcard.setStructuredName(n);
                vcard.setOrganization(worker.getInstitute());

                String vcards = worker.getFamilyName() + ".vcf";

                try {

                    vcard.write(new File(vcards));
                    File fileToDownload = new File(vcards);
                    InputStream inputStream = new FileInputStream(fileToDownload);
                    response.setContentType("application/force-download");
                    response.setHeader("Content-Disposition", "attachment; filename=" + vcards);
                    IOUtils.copy(inputStream, response.getOutputStream());
                    response.flushBuffer();
                    inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("workersList", users);
        return "search";
    }

}
