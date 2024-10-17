
package com.keduit.interiors.repository;


import com.keduit.interiors.dto.ItemSearchDTO;
import com.keduit.interiors.entity.Megazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MegazineRepository extends JpaRepository<Megazine, Long>{
   // Page<Megazine> getMegazineItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);
}

