package com.lutheroaks.tacoswebsite.member;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lutheroaks.tacoswebsite.bio.Bio;
import com.lutheroaks.tacoswebsite.tickets.Tickets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// This class contains the attributes and getters/setters for the Member table in the database
@Entity
@Table(name = "Member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Member {

    // memberId is the key for each row
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;

    @Column(nullable = false, unique = false, length = 50)
    @NonNull private String firstName;

    @Column(nullable = false, unique = false, length = 50)
    @NonNull private String lastName;

    @Column(nullable = false, unique = true, length = 50)
    @NonNull String email;

    @ManyToMany
    @JoinTable(
        name = "Ticket_Member_Assignments", 
        joinColumns = @JoinColumn(name = "memberId"), 
        inverseJoinColumns = @JoinColumn(name = "ticketNum"))
    @ElementCollection(targetClass = Tickets.class)
    private Set<Tickets> associatedTickets;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "member_bio", 
      joinColumns = 
        { @JoinColumn(name = "memberId", referencedColumnName = "memberId") },
      inverseJoinColumns = 
        { @JoinColumn(name = "bioId", referencedColumnName = "bioId") })
    private Bio biography;
}