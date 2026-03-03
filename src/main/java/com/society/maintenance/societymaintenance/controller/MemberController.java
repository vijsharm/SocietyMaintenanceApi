package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.ApiResponse;
import com.society.maintenance.societymaintenance.dto.MemberDto;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Member> addMember(@RequestBody MemberDto request) {
        Member member = service.addMember(request);
        return ApiResponse.success("Member added successfully", member);
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateMember(
            @PathVariable Long id,
            @RequestBody MemberDto request
    ) {
        Member member = service.updateMember(id, request);
        return ApiResponse.success("Member updated successfully", member);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteMember(@PathVariable Long id) {
        service.deleteMember(id);
        return ApiResponse.success("Member deleted successfully", null);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
    public List<MemberDto> getAllMembers() {
        return service.getAllMembers();
    }
}

