package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.MemberDto;
import com.society.maintenance.societymaintenance.entity.ElectricityDue;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.repository.ElectricityDueRepository;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final ElectricityDueRepository electricityDueRepository;

    public Member addMember(MemberDto request) {

        Member member = new Member();
        member.setName(request.getName());
        member.setFlatNumber(request.getFlatNumber());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMonthlyAmount(request.getMonthlyAmount());
        member.setActiveSince(
                request.getOnboardingDate() != null ? request.getOnboardingDate() : LocalDate.now()
        );
        Member savedMember = memberRepository.save(member);

        // 2️⃣ If electricity arrear provided → create ElectricityDue
        if (request.getElectricityArrear() != null) {

            ElectricityDue due = new ElectricityDue();
            due.setMember(savedMember);
            due.setElectricityArrear(request.getElectricityArrear());

            electricityDueRepository.save(due);
        }

        return savedMember;
    }

    public Member updateMember(Long id, MemberDto request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMonthlyAmount(request.getMonthlyAmount());
        Member updatedMember = memberRepository.save(member);

        // Handle electricity arrear (if provided)
        if (request.getElectricityArrear() != null) {

            ElectricityDue due = electricityDueRepository
                    .findByMember(member)
                    .orElseGet(() -> {
                        ElectricityDue newDue = new ElectricityDue();
                        newDue.setMember(member);
                        return newDue;
                    });

            due.setElectricityArrear(request.getElectricityArrear());
            electricityDueRepository.save(due);
        }

        return updatedMember;
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<MemberDto> getAllMembers() {
        return memberRepository.findAllWithElectricityDues();
    }
}

