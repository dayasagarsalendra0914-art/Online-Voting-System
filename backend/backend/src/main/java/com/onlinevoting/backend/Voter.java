package com.onlinevoting.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Voter {

    @Id
    private String voterId;

    @Column(nullable = false)
    private boolean hasVoted = false;

    public Voter() {
    }

    public Voter(String voterId) {
        this.voterId = voterId;
        this.hasVoted = false;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}