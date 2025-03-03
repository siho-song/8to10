package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.dto.request.fschedule.FScheduleSaveRequest;
import com.eighttoten.schedule.dto.request.fschedule.FScheduleUpdateRequest;
import com.eighttoten.schedule.dto.request.fschedule.FixDetailUpdateRequest;
import com.eighttoten.schedule.service.fschedule.FScheduleDetailService;
import com.eighttoten.schedule.service.fschedule.FScheduleService;
import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.ValidationSequence;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/fixed")
public class FScheduleController {
    private final FScheduleService fScheduleService;
    private final FScheduleDetailService fScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) FScheduleSaveRequest request){

        long fScheduleId = fScheduleService.save(request.toNewFSchedule(member.getId()));
        fScheduleDetailService.saveAllDetails(fScheduleId, request.toFScheduleCreateInfo());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Valid FScheduleUpdateRequest request){

        fScheduleService.update(member, request.toFScheduleUpdate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(
            @CurrentMember Member member,
            @PathVariable(name = "id") Long id
    ){
        fScheduleService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) FixDetailUpdateRequest request){

        fScheduleDetailService.update(member, request.toFDetailUpdate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Void> deleteDetailsGEStartDate(
            @CurrentMember Member member,
            @RequestParam(value = "parentId") Long parentId,
            @RequestParam(value = "startDate") LocalDateTime startDate) {

        fScheduleDetailService.deleteByMemberAndParentIdGEStartDate(member, parentId, startDate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail/{id}")
    public ResponseEntity<Void> deleteDetailById(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id) {

        fScheduleDetailService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}