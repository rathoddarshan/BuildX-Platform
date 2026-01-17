package com.codingShuttle.projects.lovable.clone.service;

import com.codingShuttle.projects.lovable.clone.dto.member.InviteMemberRequest;
import com.codingShuttle.projects.lovable.clone.dto.member.MemberResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, InviteMemberRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
