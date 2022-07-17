package ru.kermilov.targetaudience.vkadapter.repository;

import org.springframework.data.repository.CrudRepository;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByExternalId(Integer externalId);
}
