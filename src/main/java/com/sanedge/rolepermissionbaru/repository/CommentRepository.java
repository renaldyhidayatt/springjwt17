package com.sanedge.rolepermissionbaru.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sanedge.rolepermissionbaru.models.Comment;

import jakarta.transaction.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTutorialId(Long postId);

    @Transactional
    void deleteByTutorialId(long tutorialId);
}