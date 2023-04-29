package com.example.hanghaehomework.service;


import com.example.hanghaehomework.dto.BoardRequestDto;
import com.example.hanghaehomework.dto.BoardResponseDto;
import com.example.hanghaehomework.entity.Board;
import com.example.hanghaehomework.entity.BoardLikes;
import com.example.hanghaehomework.entity.Member;
import com.example.hanghaehomework.entity.UserRoleEnum;
import com.example.hanghaehomework.jwt.JwtUtil;
import com.example.hanghaehomework.repository.BoardLikesRepository;
import com.example.hanghaehomework.repository.BoardRepository;
import com.example.hanghaehomework.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    public final BoardRepository boardRepository ;
    private final MemberRepository memberRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final JwtUtil jwtUtil;


    //게시글 작성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request, Member member) {
//        member = checkJwtToken(request);
//            if (member == null) {
//            throw new IllegalArgumentException("로그인이 필요합니다.");
//        }
//        requestDto.setMember(member);
//        Board board = new Board(requestDto);
        Board board = boardRepository.saveAndFlush(new Board(requestDto, member));
        return new BoardResponseDto(board);
    }

    //게시글 목록 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getList() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().sorted((memo1, memo2) -> memo2.getModifiedAt().compareTo(memo1.getModifiedAt())).map(BoardResponseDto::new).toList();
    }

    //선택한 게시글 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 틀립니다.")
        );
        return new BoardResponseDto(board);
    }

    //게시글 수정
    @Transactional
    public  BoardResponseDto update(Long id, BoardRequestDto requestDto, HttpServletRequest request, Member member) {
//        Member member = checkJwtToken(request);

        Board board =boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        if(member.getUsername().equals(board.getMember().getUsername()) || member.getRole() == UserRoleEnum.ADMIN) {
            board.update(requestDto);
        }else{
            throw new IllegalArgumentException("권한이 없습니다");
        }
        return new BoardResponseDto(board);
    }

    //게시글 삭제
    @Transactional
    public  String deleteBoard(Long id, HttpServletRequest request, Member member) {
//        Member member = checkJwtToken(request);

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        if(member.getUsername().equals(board.getMember().getUsername()) || member.getRole() == UserRoleEnum.ADMIN) {
            boardRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("권한이 없습니다");
        }

        return "게시글 삭제 성공.";
    }

    @Transactional
    public BoardResponseDto updateLikes(Long id, Member member) {
        // 게시글 존재 여부 확인
        Board board = validatePost(id);

        // 게시글에 현재 유저의 좋아요 유무 확인
        if (boardLikesRepository.existsByBoardIdAndMemberId(id, member.getId())){
            BoardLikes boardLikes = boardLikesRepository.findByBoardIdAndMemberId(id, member.getId());
            boardLikesRepository.delete(boardLikes);
            board.updateLikes(false);
        } else { // 현재 유저의 좋아요 흔적 없음 -> 좋아요
            boardLikesRepository.save(new BoardLikes(board, member));
            board.updateLikes(true);
        }
        return new BoardResponseDto(board);
    }

    //게시글 존재 여부 확인
    private Board validatePost(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
    }

    //작성자 일치 여부 판단
    private void isPostAuthor(Member member, Board board) {
        if (!board.getMember().getUsername().equals(member.getUsername())) {
            if (member.isAdmin()) return;
            throw new IllegalArgumentException("작성자가 일치하지 않습니다");
        }
    }
}