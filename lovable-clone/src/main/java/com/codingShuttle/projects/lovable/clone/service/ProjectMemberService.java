package com.codingShuttle.projects.lovable.clone.service;

import com.codingShuttle.projects.lovable.clone.dto.member.InviteMemberRequest;
import com.codingShuttle.projects.lovable.clone.dto.member.MemberResponse;
import com.codingShuttle.projects.lovable.clone.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    MemberResponse removeProjectMember(Long projectId, Long memberId, Long userId);
}
