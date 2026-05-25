package com.devbackend.journalApp.Services;


import com.devbackend.journalApp.Entity.JournalEntry;
import com.devbackend.journalApp.Entity.User;
import com.devbackend.journalApp.Repo.JournalEntryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JournalEntryService {



    @Autowired
    JournalEntryRepo journalEntryRepo;
    @Autowired
    UserService userService;

    public List<JournalEntry> getAll() {

        return journalEntryRepo.findAll();
    }

    public JournalEntry getById(int id) {

        return journalEntryRepo.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public JournalEntry addJournalEntry(JournalEntry myEntry, String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("User not found: " + username);
            }
            myEntry.setUser(user);
            JournalEntry saved = journalEntryRepo.save(myEntry);
            user.getJournalEntries().add(saved);
            userService.addUser(user);
            return saved;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add journal entry", e);
        }
    }


    public JournalEntry updateJournalEntry(int id, String username, JournalEntry updates) {
        JournalEntry entry = journalEntryRepo.findById(id).orElse(null);
        if (entry == null) {
            return null;
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        User owner = entry.getUser();
        if (owner == null || !user.getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Journal entry does not belong to user: " + username);
        }

        if (updates.getTitle() != null && !updates.getTitle().isEmpty()) {
            entry.setTitle(updates.getTitle());
        }
        if (updates.getContent() != null && !updates.getContent().isEmpty()) {
            entry.setContent(updates.getContent());
        }
        if (updates.getDate() != null) {
            entry.setDate(updates.getDate());
        }

        return journalEntryRepo.save(entry);
    }


    public boolean deleteJournalEntry(int id, String username) {
        JournalEntry entry = journalEntryRepo.findById(id).orElse(null);
        if (entry == null) {
            return false;
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        User owner = entry.getUser();
        if (owner == null || !user.getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Journal entry does not belong to user: " + username);
        }

        if (owner != null) {
            owner.getJournalEntries().remove(entry);
            userService.addUser(owner);
        }
        journalEntryRepo.deleteById(id);
        return true;
    }
}
