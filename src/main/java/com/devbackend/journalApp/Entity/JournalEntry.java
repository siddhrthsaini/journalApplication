package com.devbackend.journalApp.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data

@Entity
@NoArgsConstructor
public class JournalEntry {

    @Id
    private int id;
    private String title;
    private String content;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"journalEntries", "password"})
    private User user;

}
