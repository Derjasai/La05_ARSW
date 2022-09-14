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
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Blueprint data = bs.getBlueprint(author,bpname);
            return new ResponseEntity<>(gson.toJson(data), HttpStatus.ACCEPTED);
        }catch (BlueprintNotFoundException ex) {
            //Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("EL autor: "+author+" o el nombre del plano: "+bpname+"No coincide, Error 404",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postBlueprint(@RequestBody Blueprint blueprint){
        try {
            bs.addNewBlueprint(blueprint);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException | BlueprintNotFoundException ex) {
            //Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("No se ha logrado registrar",HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(path = "/{author}/{bpname}", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint blueprint){
        Blueprint blueprintNew = null;
        try {
            blueprintNew = bs.getBlueprint(author, bpname);

            blueprintNew.setAuthor(blueprint.getAuthor());
            blueprintNew.setName(blueprint.getName());
            blueprintNew.setPoints(blueprint.getPoints());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("Estamos trabajando para ti",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

