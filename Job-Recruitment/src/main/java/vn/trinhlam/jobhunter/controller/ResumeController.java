package vn.trinhlam.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import com.turkraft.springfilter.boot.Filter;

import vn.trinhlam.jobhunter.domain.Resume;
import vn.trinhlam.jobhunter.domain.response.ResUpdateDTO;
import vn.trinhlam.jobhunter.domain.response.ResultPaginationDTO;
import vn.trinhlam.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.trinhlam.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.trinhlam.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import jakarta.validation.Valid;
import vn.trinhlam.jobhunter.service.ResumeService;
import vn.trinhlam.jobhunter.util.annotation.ApiMessage;
import vn.trinhlam.jobhunter.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {

        boolean isExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isExist) {
            throw new IdInvalidException("User id/job ib không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));

    }

    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {

        // check id exits
        Optional<Resume> resOptional = this.resumeService.fetchById(resume.getId());
        if (resOptional == null) {
            throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
        }
        Resume reqResume = resOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.update(resume));

    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (resumeOptional.isEmpty())
            throw new IdInvalidException("Resume ứng với id = " + id + " không tồn tại");
        this.resumeService.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (resumeOptional.isEmpty())
            throw new IdInvalidException("Resume ứng với id = " + id + " không tồn tại");
        return ResponseEntity.ok().body(this.resumeService.getResume(resumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Resume> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchAll(specification, pageable));

    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
