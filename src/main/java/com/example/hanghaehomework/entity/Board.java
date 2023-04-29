package com.example.hanghaehomework.entity;


import com.example.hanghaehomework.dto.BoardRequestDto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private int likes;
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    private List<Comment> commentList = new ArrayList<>();

    public Board(BoardRequestDto requestDto) {
        this.member = requestDto.getMember();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public Board(BoardRequestDto requestDto, Member member) {
        if (member.getUsername() == null) {
            throw new IllegalArgumentException("username이 존재하지 않습니다.");
        }

        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("글의 제목이 없습니다.");
        }

        if (requestDto.getContents() == null || requestDto.getContents().isEmpty()) {
            throw new IllegalArgumentException("글의 내용이 없습니다.");
        }

        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.member = member;
    }


    public void update(BoardRequestDto requestDto) {
//        this.member = requestDto.getMember();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void updateLikes(boolean addOrNot){
        this.likes = addOrNot? this.likes+1 : this.likes -1;
    }
}