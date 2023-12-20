package com.kogi.emr.repository;

import com.kogi.emr.models.Item;
import com.kogi.emr.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long>, JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    Optional<Item> findById(Long id);

    Page<Item> findAll(Specification <Item> finalSpec,Pageable pageable);
}
