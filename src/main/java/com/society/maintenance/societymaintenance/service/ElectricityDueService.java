package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.ElectricityDueBatchRequest;
import com.society.maintenance.societymaintenance.dto.ElectricityDueUpdate;
import com.society.maintenance.societymaintenance.entity.ElectricityDue;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.repository.ElectricityDueRepository;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectricityDueService {

    private final ElectricityDueRepository dueRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public int updateElectricityDuesBatch(ElectricityDueBatchRequest request) {

        List<ElectricityDue> toSave = new ArrayList<>();

        for (ElectricityDueUpdate update : request.updates) {

            Member member = memberRepository.findById(update.memberId)
                    .orElseThrow(() ->
                            new RuntimeException("Member not found: " + update.memberId));

            ElectricityDue due = dueRepository.findByMember(member)
                    .orElseGet(() -> {
                        ElectricityDue d = new ElectricityDue();
                        d.setMember(member);
                        return d;
                    });

            due.setElectricityArrear(update.electricityArrear);
            toSave.add(due);
        }

        dueRepository.saveAll(toSave);
        return toSave.size();
    }
}

