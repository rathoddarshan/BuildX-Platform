package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.member.InviteMemberRequest;
import com.codingShuttle.projects.buildX.platform.dto.member.MemberResponse;
import com.codingShuttle.projects.buildX.platform.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    void  removeProjectMember(Long projectId, Long memberId, Long userId);
}
