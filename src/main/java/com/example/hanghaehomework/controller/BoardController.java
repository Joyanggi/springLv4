package com.example.hanghaehomework.controller;


import com.example.hanghaehomework.dto.BoardRequestDto;
import com.example.hanghaehomework.dto.BoardResponseDto;
import com.example.hanghaehomework.security.MemberDetailsImpl;
import com.example.hanghaehomework.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class BoardController {

    private final BoardService boardService;

    //게시글 작성
    @PostMapping("/post")
    public BoardResponseDto creatBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return boardService.createBoard(requestDto, request, memberDetails.getUser());
    }

    //게시글 조회
    @GetMapping("/posts")
    public List<BoardResponseDto> getList() {
        return boardService.getList();
    }

    //선택한 게시글 조회
    @GetMapping("/posts/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    //선택한 게시글 수정
    @PutMapping("/post/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.update(id, requestDto, request, memberDetails.getUser());
    }

    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public String deleteBoard(@PathVariable Long id, HttpServletRequest request, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.deleteBoard(id, request, memberDetails.getUser());
    }

    @PutMapping("/likes/{id}")
    public BoardResponseDto updateLikes(@PathVariable Long id, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.updateLikes(id, memberDetails.getUser());
    }
}