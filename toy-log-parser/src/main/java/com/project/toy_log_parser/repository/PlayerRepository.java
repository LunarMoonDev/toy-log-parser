package com.project.toy_log_parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.toy_log_parser.entity.Player;

import java.math.BigInteger;

@Repository
@Transactional
public interface PlayerRepository extends JpaRepository<Player, BigInteger>{
}
