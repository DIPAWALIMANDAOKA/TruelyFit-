package com.TruelyFit.TruelyFit.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TruelyFit.TruelyFit.Entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer,Long>{
   Optional<Trainer>findByUserId(Long userId);
   boolean existsByUserId(Long userId);
}
