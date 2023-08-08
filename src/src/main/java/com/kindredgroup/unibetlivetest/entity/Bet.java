package com.kindredgroup.unibetlivetest.entity;

import com.kindredgroup.unibetlivetest.types.BetState;

import javax.persistence.*;
import java.util.Date;

@Table(name = "bet")
@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private int name;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name="selection_id", nullable=false)
    private Selection selection;

    @Column(name = "state")
    private BetState betState;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;




}
