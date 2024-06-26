package com.mos.domain.wiki.service.impl;

import com.mos.domain.study.repository.StudyRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mos.domain.wiki.dto.WikiDto;
import com.mos.domain.wiki.repository.WikiRepository;
import com.mos.domain.wiki.service.WikiService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultWikiService implements WikiService {

  private final WikiRepository wikiRepository;
  private final StudyRepository studyRepository;

  // 개별 스터디에 따라 위키들을 정렬하는 메서드임!
  @Override
  public List<WikiDto> listByStudyNo(int studyNo) {
    return wikiRepository.listByStudyNo(studyNo);
  }

  @Override
  public Page<WikiDto> listByWikiNo(Pageable page) {
    List<WikiDto> list = wikiRepository.listByWikiNo(page);
    int count = wikiRepository.wikiCount();
    return new PageImpl<>(list, page, count);
  }

  @Override
  public WikiDto getByWikiNo(int wikiNo) {
    return wikiRepository.getByWikiNo(wikiNo);
  }

  @Override
  public int updateWiki(WikiDto wikiDto) {
    return wikiRepository.updateWiki(wikiDto);
  }

  @Override
  public void deleteWiki(int wikiNo) {
    wikiRepository.deleteWiki(wikiNo);
  }

  @Override
  public void add(WikiDto wikiDto) {
    wikiRepository.add(wikiDto);
  }

  @Override
  public int getFirstWikiNo(int studyNo) {
    return wikiRepository.getFirstWiki(studyNo);
  }

  @Transactional
  @Override
  public WikiDto updateHitCount(int wikiNo) {
    wikiRepository.updateHitCount(wikiNo);
    return wikiRepository.getByWikiNo(wikiNo);
  }

  @Override
  public Integer findWikiNoByStudyNo(int studyNo) {
    return wikiRepository.findWikiNoByStudyNo(studyNo);
  }

  @Override
  public void updateLikeCount(int wikiNo, int likes) {
    wikiRepository.updateLikeCount(wikiNo, likes);
  }
}
