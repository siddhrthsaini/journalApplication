package com.devbackend.journalApp.Controller;


import com.devbackend.journalApp.Entity.JournalEntry;
import com.devbackend.journalApp.Entity.User;
import com.devbackend.journalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        List<User> allUser = userService.getAll();
        if (allUser != null && !allUser.isEmpty()){
            return new ResponseEntity<>(allUser, HttpStatus.OK);
        }
        else   {return new ResponseEntity<>( "No User Found !!", HttpStatus.NOT_FOUND);}


    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id){

        User userId = userService.getById(id);

        if(userId != null){
            return new ResponseEntity<>(userId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping
    public  ResponseEntity<User> createEntry( @RequestBody User myEntry){

        try {
            return  new ResponseEntity<>(userService.addUser(myEntry), HttpStatus.CREATED);
        }
        catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateEntry(@RequestBody User user ){

        User oldUser = userService.findByUsername(user.getUsername());
        if(oldUser != null){
            oldUser.setUsername(user.getUsername() != null && !user.getUsername().isEmpty() ? user.getUsername() : oldUser.getUsername());
            oldUser.setPassword(user.getPassword() != null && !user.getPassword().isEmpty()? user.getPassword() :oldUser.getPassword());

        }
        return  new ResponseEntity<>(userService.addUser(oldUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteEntry(@PathVariable Long id ){
        User user = userService.getById(id);
        if(user != null){
            userService.deleteUser( id);
            return  new ResponseEntity<>("User Deleted Successfully !!", HttpStatus.OK);
        }
        else  return  new ResponseEntity<>("Invalid User Id", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
