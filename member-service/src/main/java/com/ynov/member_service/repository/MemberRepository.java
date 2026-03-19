package com.ynov.member_service.repository;


import com.ynov.member_service.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}