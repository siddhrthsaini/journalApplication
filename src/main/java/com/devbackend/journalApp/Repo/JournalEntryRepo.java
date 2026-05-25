package com.devbackend.journalApp.Repo;

import com.devbackend.journalApp.Entity.JournalEntry;
import com.devbackend.journalApp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepo extends JpaRepository<JournalEntry, Integer> {


}
