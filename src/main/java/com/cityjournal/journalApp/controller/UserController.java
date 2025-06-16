package com.cityjournal.journalApp.controller;

import com.cityjournal.journalApp.entity.JournalEntry;
import com.cityjournal.journalApp.entity.User;
import com.cityjournal.journalApp.service.JournalEntryService;
import com.cityjournal.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")

class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<User> all = userService.getAll();

        if (all != null && !all.isEmpty()){

            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity <> (HttpStatus.NOT_FOUND);
    }

    @PostMapping public void saveUser( @RequestBody User user){

        userService.saveUser(user);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user , @PathVariable String userName){
        User userInDB = userService.findByUserName(userName);

        if (userInDB != null ){
            userInDB.setUserName(user.getUserName());
            userInDB.setPassword(user.getPassword());
            userService.saveUser(userInDB);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
