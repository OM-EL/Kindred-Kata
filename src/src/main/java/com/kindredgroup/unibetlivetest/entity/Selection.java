package com.kindredgroup.unibetlivetest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kindredgroup.unibetlivetest.types.SelectionResult;
import com.kindredgroup.unibetlivetest.types.State;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "selection")
@Entity
@Data
@Accessors(chain = true)
public class Selection {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "current_odd")
    BigDecimal currentOdd;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    State state;

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    SelectionResult result;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "market_id")
    @JsonBackReference
    Market market;

}
