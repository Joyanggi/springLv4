package com.example.hanghaehomework.repository;

import com.example.hanghaehomework.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {
    boolean existsByBoardIdAndMemberId(Long post_id, Long member_id);
    BoardLikes findByBoardIdAndMemberId(Long post_id, Long member_id);
}
