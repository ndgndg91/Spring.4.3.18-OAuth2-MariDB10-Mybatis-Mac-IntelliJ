package com.ndgndg91.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"applyNo"})
@ToString(of = {"applyNo", "applicantNo", "acceptorNo", "isAccept", "createdTime"})
public class FriendDTO {
    private String applyNo;
    @Setter
    private String applicantNo;
    @Setter
    private String acceptorNo;
    private Boolean isAccept;
    private LocalDateTime createdTime;

    private FriendDTO() { };
    private FriendDTO(String applyNo, String applicantNo, String acceptorNo, Boolean isAccept, LocalDateTime createdTime) {
        this.applyNo = applyNo;
        this.applicantNo = applicantNo;
        this.acceptorNo = acceptorNo;
        this.isAccept = isAccept;
        this.createdTime = createdTime;
    }
}