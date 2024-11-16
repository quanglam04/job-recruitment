package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Skill;
import vn.trinhlam.jobhunter.domain.Subscriber;
import vn.trinhlam.jobhunter.repository.SkillRepository;
import vn.trinhlam.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
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

}
