package com.onlinevoting.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Controller
public class VotingController {

    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;

    public VotingController(
            CandidateRepository candidateRepository,
            VoterRepository voterRepository) {

        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;

        // Add candidates if the database is empty.
        if (candidateRepository.count() == 0) {
            candidateRepository.saveAll(List.of(
                new Candidate(1L, "Candidate One", "Party A"),
                new Candidate(2L, "Candidate Two", "Party B"),
                new Candidate(3L, "Candidate Three", "Party C")
            ));
        }

        // Register sample voter IDs from VOTER001 to VOTER100.
        for (int i = 1; i <= 100; i++) {
            String id = String.format("VOTER%03d", i);

            if (!voterRepository.existsById(id)) {
                voterRepository.save(new Voter(id));
            }
        }
    }

    // Homepage
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute(
                "candidates",
                candidateRepository.findAll()
        );

        return "index";
    }

    // Submit a vote
    @PostMapping("/vote")
    @Transactional
    public String vote(
            @RequestParam("voterId") String voterId,
            @RequestParam("candidateId") Long candidateId,
            RedirectAttributes redirectAttributes) {

        String id = voterId.trim().toUpperCase(Locale.ROOT);

        Voter voter = voterRepository.findById(id).orElse(null);

        if (voter == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Invalid Voter ID. Please use a registered ID."
            );
            return "redirect:/";
        }

        if (voter.isHasVoted()) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "This Voter ID has already voted."
            );
            return "redirect:/";
        }

        Candidate candidate =
                candidateRepository.findById(candidateId).orElse(null);

        if (candidate == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Invalid candidate selected."
            );
            return "redirect:/";
        }

        candidate.setVotes(candidate.getVotes() + 1);
        voter.setHasVoted(true);

        candidateRepository.save(candidate);
        voterRepository.save(voter);

        redirectAttributes.addFlashAttribute(
                "message",
                "Your vote was submitted successfully!"
        );

        return "redirect:/";
    }

    // Election results page
    @GetMapping("/results")
    @Transactional(readOnly = true)
    public String results(Model model) {
        model.addAttribute(
                "candidates",
                candidateRepository.findAll()
        );

        return "results";
    }
}