package com.mos.domain.member.controller;

import com.mos.domain.comment.dto.StudyCommentDto;
import com.mos.domain.comment.dto.WikiCommentDto;
import com.mos.domain.member.dto.MemberDto;
import com.mos.domain.member.dto.MemberJoinDto;
import com.mos.domain.member.dto.MemberStudyDto;
import com.mos.domain.member.dto.MyStudiesDto;
import com.mos.domain.member.dto.MyStudiesUpdateDto;
import com.mos.domain.member.dto.UpdateFavoritesDto;
import com.mos.domain.member.service.impl.DefaultMemberService;
import com.mos.domain.study.dto.StudyDto;
import com.mos.domain.study.service.impl.DefaultStudyService;
import com.mos.domain.wiki.dto.WikiDto;
import com.mos.domain.wiki.service.WikiService;
import com.mos.global.auth.LoginUser;
import com.mos.global.storage.service.StorageService;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController implements InitializingBean {

  private final Log log = LogFactory.getLog(Thread.currentThread().getClass());
  private final DefaultMemberService memberService;
  private final DefaultStudyService studyService;
  private final StorageService storageService;
  private final WikiService wikiService;
  private String uploadDir;

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.uploadDir = "member/";

    log.debug(String.format("uploadDir: %s", this.uploadDir));
    log.debug(String.format("bucketname: %s", this.bucketName));
  }

  @GetMapping("findByEmail")
  public void findByEmail(String email) throws Exception {
    MemberDto member = memberService.get(email);
    if (member == null) {
      throw new Exception("해당 이메일이 존재하지 않습니다.");
    }
    memberService.get(member.getEmail());
  }

  @GetMapping("findByUsername")
  public String findByUsername(String username) throws Exception {
    MemberDto member = memberService.getName(username);
    if (memberService.getName(username) != null) {
      System.out.println("중복된 닉네임입니다.");
      return "auth/signup";
    }
    return "index";
  }

  @PostMapping("add")
  public String add(MemberJoinDto joinDto) {
    memberService.join(joinDto);
    System.out.println("사용가능한 닉네임입니다.");
    return "redirect:/";
  }

    @GetMapping("dashboard")
    public void viewDashboard(int no, Model model) throws Exception {
        MemberDto member = memberService.getNo(no);
        if (member == null) {
            throw new Exception("회원 번호가 유효하지 않습니다.");
        }
        model.addAttribute("member", member);
    }

// 참여한 스터디목록 페이지
@GetMapping("mystudy")
public String getMyStudy(@LoginUser MemberDto loginUser, Model model) {
  int memberNo = loginUser.getMemberNo();

  List<MemberStudyDto> myStudies = memberService.findMyStudies(memberNo);
  System.out.println("myStudies = " + myStudies);

  model.addAttribute("memberStudyList", myStudies);
  return "member/mystudy";
}
  /**
   * 스터디장일 경우 가입 신청, 멤버 관리를 위한 컨트롤러
   * @param studyNo 스터디 번호
   * @param loginUser 로그인 유저 정보
   * @return 해당 스터디 멤버 리스트
   */
  @GetMapping("studyManagement")
  public ResponseEntity<Page<MyStudiesDto>> studyManagement(
      int studyNo,
      @LoginUser MemberDto loginUser,
      @PageableDefault(size = 5) Pageable page) {
    int memberNo = loginUser.getMemberNo();
    return ResponseEntity.ok().body(memberService.findListByStudyNo(studyNo, memberNo, page));
  }

  @PostMapping("updateStatus")
  public ResponseEntity<?> updateStatus(@RequestBody MyStudiesUpdateDto updateDto) {
    memberService.updateStatus(updateDto);
    return ResponseEntity.ok().build();
  }


  // 회원 정보 조회 페이지
  @GetMapping("edit")
  public String editMemberForm(Model model, @LoginUser MemberDto loginUser) {

    if (loginUser == null) {
      return "auth/login";
    }

    MemberDto member = memberService.getNo(loginUser.getMemberNo());
    model.addAttribute("member", member);
    return "member/editProfile";
  }


  // 회원 정보 수정
  @PostMapping("update")
  public String updateMember(@Valid MemberDto member,
      MultipartFile memberPhoto,
      BindingResult bindingResult,
      @LoginUser MemberDto loginUser) throws Exception {

    if (loginUser == null) {
      return "auth/login";
    }

    String newUserName = member.getUserName();
    String originalUserName = loginUser.getUserName();

    // 새로운 유저네임이 비어있으면 기존 유저네임으로 설정
    if (newUserName == null || newUserName.trim().isEmpty()) {
      member.setUserName(originalUserName);
    }

    // 새로운 유저네임이 기존에 존재하는 유저네임인지 확인
    if (!originalUserName.equals(newUserName) && memberService.existsByUserName(newUserName)) {
      bindingResult.rejectValue("userName", "duplicate", "이미 존재하는 유저네임입니다.");
    }

    // 유효성 검사 실패 시 editForm 뷰로 이동
    if (bindingResult.hasErrors()) {
      return "member/editProfile";
    }

    if (memberPhoto.getSize() > 0) {
      String filename = storageService.upload(this.bucketName, this.uploadDir, memberPhoto);
      member.setPhoto(filename); // 파일 이름 또는 경로를 저장
      storageService.delete(this.bucketName, this.uploadDir, loginUser.getPhoto());
    } else {
      member.setPhoto(loginUser.getPhoto());
    }

    member.setMemberNo(loginUser.getMemberNo());

    memberService.update(member);
    return "redirect:/member/edit";
  }


  @PostMapping("/withdraw")
  public String withdrawMember(HttpSession session) throws Exception {
    MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
    if (loginUser != null) {

      // 회원 탈퇴 처리
      memberService.withdraw(loginUser.getMemberNo());

      String filename = loginUser.getPhoto();

      if (filename != null) {
        storageService.delete(this.bucketName, this.uploadDir, loginUser.getPhoto());
      }
      // 세션 무효화 (로그아웃)
      session.invalidate();
    }
    return "redirect:/";
  }



  @GetMapping("myWriteCommentList")
  public String getMyWiki(@LoginUser MemberDto loginUser, Model model, HttpSession session) throws Exception {

//    user = (MemberDto) session.getAttribute("loginUser");

    int memberNo = loginUser.getMemberNo();

    MemberDto member = memberService.getNo(memberNo);
//    System.out.println("member = " + member);
    if (member == null) {
      throw new Exception("회원 번호가 유효하지 않습니다.");
    }
    model.addAttribute("member", member);

    // 회원 번호를 이용하여 회원의 위키 목록을 조회
    List<WikiDto> myWiki = memberService.findMyWiki(memberNo);
//    System.out.println("myWiki = " + myWiki);
    if (myWiki == null) {
      throw new Exception("회원 번호가 유효하지 않습니다.");
    }

    // 회원 번호를 이용하여 회원의 스터디 댓글 목록을 조회
    List<StudyCommentDto> myStudyComment = memberService.findMyStudyComment(memberNo);
    if (myStudyComment == null) {
      throw new Exception("회원 번호가 유효하지 않습니다.");
    }

    // 회원 번호를 이용하여 회원의 스터디 댓글 목록을 조회
    List<WikiCommentDto> myWikiComment = memberService.findMyWikiComment(memberNo);
    if (myWikiComment == null) {
      throw new Exception("회원 번호가 유효하지 않습니다.");
    }

    model.addAttribute("memberWikiList", myWiki);
    model.addAttribute("memberStudyCommentList", myStudyComment);
    model.addAttribute("memberWikiCommentList", myWikiComment);

    return "member/myWriteCommentList";
  }


  @PostMapping("/addFavorites")
  public ResponseEntity<?> addFavorites(@RequestBody UpdateFavoritesDto updateFavoritesDto) {
     memberService.addFavorites(updateFavoritesDto);
     return ResponseEntity.ok().build();
  }

}
