package com.cityjournal.journalApp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Optional;

@Document(collection = "journal_entries")

@Data
public class JournalEntry {

    @Id
    private ObjectId id;

    @NonNull
    private  String title;

    private String content;

    private LocalDateTime date;

}
