package com.kindredgroup.unibetlivetest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "market")
@Entity
@Data
@Accessors(chain = true)
public class Market {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity=Selection.class, mappedBy="market", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Selection> selections = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    Event event;




}
