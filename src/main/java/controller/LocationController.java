package controller;

import model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.JournalService;

/**
 * Created by Iulian on 11/25/2017.
 */

@RestController
@RequestMapping("/API/locations")
public class LocationController {

//    @Autowired
//    private JournalService service;
//
//    @RequestMapping(method = RequestMethod.GET)
//    public Iterable<Location> getAllLocations(){
//        return service.getAllLocations();
//    }
//
//    @RequestMapping(method = RequestMethod.PUT)
//    public Location addLocation(@RequestBody Location location){
//        return service.addLocation(location);
//    }

}