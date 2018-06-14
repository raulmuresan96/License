package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.JournalService;

/**
 * Created by Raul on 14/06/2018.
 */

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;


    @RequestMapping(value = "/populate", method = RequestMethod.GET)
    public void init(){
        journalService.populateFromFile();
    }

}
