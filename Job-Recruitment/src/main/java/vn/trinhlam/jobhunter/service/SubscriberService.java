package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Job;
import vn.trinhlam.jobhunter.domain.Skill;
import vn.trinhlam.jobhunter.domain.Subscriber;
import vn.trinhlam.jobhunter.domain.response.email.ResEmailJob;
import vn.trinhlam.jobhunter.repository.JobRepository;
import vn.trinhlam.jobhunter.repository.SkillRepository;
import vn.trinhlam.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public Subscriber create(Subscriber subscriber) {
        if (subscriber.getSkills() != null) {
            List<Long> listSkills = subscriber.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> ListSkills = this.skillRepository.findByIdIn(listSkills);
            subscriber.setSkills(ListSkills);
        }

        return this.subscriberRepository.save(subscriber);
    }

    public boolean isExistByEmail(Subscriber subscriber) {
        return this.subscriberRepository.existsByEmail(subscriber.getEmail());
    }

    public Subscriber findById(long id) {
        Optional<Subscriber> subOptional = this.subscriberRepository.findById(id);
        if (subOptional.isPresent()) {
            return subOptional.get();
        }
        return null;
    }

    public Subscriber update(Subscriber subscriberFromRequest, Subscriber subscriberInDB) {
        // check skill
        if (subscriberFromRequest.getSkills() != null) {
            List<Long> reqSkills = subscriberFromRequest.getSkills().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            subscriberInDB.setSkills(skills);

        }
        return subscriberRepository.save(subscriberInDB);
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> skillEmails = skills.stream()
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName())).collect(Collectors.toList());
        res.setSkills(skillEmails);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }

    // @Scheduled(fixedDelay = 1000)
    // public void testCron() {
    // System.out.println(">>>>>TEST CRON");

    // }

}
