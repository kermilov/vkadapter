package ru.kermilov.targetaudience.vkadapter.repository;

import org.springframework.data.repository.CrudRepository;

import ru.kermilov.targetaudience.vkadapter.domain.PostTemplateEntity;

import java.util.Optional;

public interface PostTemplateRepository extends CrudRepository<PostTemplateEntity, Long>{
    Optional<PostTemplateEntity> findByExternalId(String externalId);
}
