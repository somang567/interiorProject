package com.keduit.interiors.repository;

import com.keduit.interiors.dto.ItemSearchDTO;
import com.keduit.interiors.entity.Megazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Megazine> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

}