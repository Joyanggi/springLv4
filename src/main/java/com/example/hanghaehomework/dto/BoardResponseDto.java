package com.example.hanghaehomework.dto;

import com.example.hanghaehomework.entity.Board;
import com.example.hanghaehomework.entity.BoardLikes;
import com.example.hanghaehomework.entity.Comment;
import com.example.hanghaehomework.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    private int likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.username = board.getMember().getUsername();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.commentList = board.getCommentList().stream().map(CommentResponseDto::new).toList();
        this.likes = board.getLikes();
    }

}