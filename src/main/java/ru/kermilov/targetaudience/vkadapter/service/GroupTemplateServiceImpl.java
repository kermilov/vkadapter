package ru.kermilov.targetaudience.vkadapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kermilov.targetaudience.vkadapter.domain.GroupTemplateEntity;
import ru.kermilov.targetaudience.vkadapter.repository.GroupTemplateRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupTemplateServiceImpl implements GroupTemplateService {
    private final GroupTemplateRepository groupTemplateRepository;
    @Override
    public List<GroupTemplateEntity> findAll() {
        return (List<GroupTemplateEntity>) groupTemplateRepository.findAll();
    }
}
