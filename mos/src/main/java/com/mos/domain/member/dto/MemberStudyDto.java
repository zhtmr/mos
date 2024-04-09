package com.mos.domain.member.dto;

import com.mos.domain.study.dto.StudyDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberStudyDto {

    private int no;
    private int memberNo;

    private StudyDto studyDto;

    private String stat;

    private String favorites;

    private String applyMsg;

}
