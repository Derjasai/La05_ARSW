/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices bs;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> manejadorGetRecursoXX() {
        try {
            //obtener datos que se enviarán a través del API
            Gson gson = new Gson();
            return new ResponseEntity<>(gson.toJson(bs.getAllBlueprints()), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/{author}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlueprintByAuthor(@PathVariable String author){
        try {
            Gson gson = new Gson();
            return new ResponseEntity<>(gson.toJson(bs.getBlueprintsByAuthor(author)), HttpStatus.ACCEPTED);
        }catch (BlueprintNotFoundException ex) {
            //Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El autor: "+author+" no existe, error 404",HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(path = "/{author}/{bpname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlueprintByAuthorName(@PathVariable String author, @PathVariable String bpname){
        try {
            Gson gson = new Gson();
            Set<Blueprint> byAuthor = bs.getBlueprintsByAuthor(author);
            Set<Blueprint> data = new HashSet<>();
            for (Blueprint bp:byAuthor){
                if(bpname.equals(bp.getName())){
                    data.add(bp);
                }
            }
            if(data.isEmpty())return new ResponseEntity<>("EL autor: "+author+" no tiene un plano con el nombre: "+bpname, HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(gson.toJson(data), HttpStatus.ACCEPTED);
        }catch (BlueprintNotFoundException ex) {
            //Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El autor: "+author+" no existe, error 404",HttpStatus.NOT_FOUND);
        }
    }


}

