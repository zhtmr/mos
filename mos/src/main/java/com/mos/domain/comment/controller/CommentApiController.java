package com.mos.domain.comment.controller;

import com.amazonaws.Response;
import com.mos.domain.comment.dto.StudyCommentDto;
import com.mos.domain.comment.dto.WikiCommentDto;
import com.mos.domain.comment.service.CommentApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentApiController {

  private final CommentApiService commentApiService;

  //getAllComment
  @GetMapping("/wiki/{wiki_no}")
  public List<WikiCommentDto> getCommentByWikiNo(@PathVariable("wiki_no") int wikiNo) {
    return commentApiService.getCommentByWikiNo(wikiNo);
  }

  @GetMapping("/study/{study_no}")
  public List<StudyCommentDto> getCommentByStudyNo(@PathVariable("study_no") int studyNo) {
    return commentApiService.getCommentByStudyNo(studyNo);
  }

  @PostMapping("/wiki")
  public ResponseEntity<WikiCommentDto> createWikiComment(WikiCommentDto wikiCommentDto) {
    commentApiService.createWikiComment(wikiCommentDto);
    return ResponseEntity.ok(wikiCommentDto);
  }

  @PostMapping("/study")
  public void createStudyComment(StudyCommentDto studyCommentDto) {
    commentApiService.createStudyComment(studyCommentDto);
  }

  //delete one comment by comment_no
  @DeleteMapping("/wiki/{comment_no}")
  public void deleteWikiCommentByCommentNo(@PathVariable("comment_no") int commentNo) {
    commentApiService.deleteWikiCommentByCommentNo(commentNo);
  }

  @DeleteMapping("/study/{comment_no}")
  public void deleteStudyCommentByCommentNo(@PathVariable("comment_no") int commentNo) {
    commentApiService.deleteStudyCommentByCommentNo(commentNo);
  }

}