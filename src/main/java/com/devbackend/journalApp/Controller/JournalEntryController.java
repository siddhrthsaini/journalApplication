package com.devbackend.journalApp.Controller;


import com.devbackend.journalApp.Entity.JournalEntry;
import com.devbackend.journalApp.Entity.User;
import com.devbackend.journalApp.Services.JournalEntryService;
import com.devbackend.journalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllEntriesOfUser( @PathVariable String username){
        User user = userService.findByUsername(username);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if (allEntries != null && !allEntries.isEmpty()){
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        else   {return new ResponseEntity<>( "No Journal Entry Found !!", HttpStatus.NOT_FOUND);}


    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getById(@PathVariable int id){

        JournalEntry journalEntryId = journalEntryService.getById(id);

        if(journalEntryId != null){
            return new ResponseEntity<>(journalEntryId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/user/{username}")
    public  ResponseEntity<JournalEntry> createEntry( @RequestBody JournalEntry myEntry, @PathVariable String username){

        try {

            JournalEntry saved = journalEntryService.addJournalEntry(myEntry, username);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }
        catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }


    @PutMapping("/user/{username}/{id}")
    public ResponseEntity<JournalEntry> updateEntry(
            @PathVariable String username,
            @PathVariable int id,
            @RequestBody JournalEntry myEntry) {
        try {
            JournalEntry updated = journalEntryService.updateJournalEntry(id, username, myEntry);
            if (updated != null) {
                return new ResponseEntity<>(updated, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{username}/{id}")
    public ResponseEntity<?> deleteEntry(
            @PathVariable String username,
            @PathVariable int id) {
        try {
            if (journalEntryService.deleteJournalEntry(id, username)) {
                return new ResponseEntity<>("Journal Entry Deleted Successfully !!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
