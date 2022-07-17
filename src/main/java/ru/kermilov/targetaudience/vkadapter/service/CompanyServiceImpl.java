package ru.kermilov.targetaudience.vkadapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kermilov.targetaudience.vkadapter.domain.CompanyEntity;

@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {
    private final PostTemplateService postTemplateService;
    private final GroupService groupService;
    private final GroupTemplateService groupTemplateService;
    private final PostService postService;

    @Override
    public CompanyEntity quickInsert(String url) {
        var companyEntity = new CompanyEntity("quickInsert", postTemplateService.quickInsert(url));
        companyEntity.setGroupEntities(groupService.findAll());
        companyEntity.setGroupTemplateEntities(groupTemplateService.findAll());
        companyEntity.getGroupEntities().forEach(groupEntity ->
            postService.quickInsert(companyEntity.getPostTemplateEntity(),groupEntity));
        return null;
    }
}
