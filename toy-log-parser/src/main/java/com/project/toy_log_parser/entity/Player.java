package com.project.toy_log_parser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "server")
    private String server;

    @Column(name = "job")
    private String job;

    @Column(name = "crit_average")
    private Double critAverage;

    @Column(name = "direct_average")
    private Double directAverage;

    @Column(name = "dmg_average")
    private Double dmgAverage;

    @Column(name = "crit_log_num")
    private Long critLogNum;

    @Column(name = "direct_log_num")
    private Long directLogNum;

    @Column(name = "dmg_log_num")
    private Long dmgLogNum;
}
