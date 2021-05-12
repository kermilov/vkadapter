package ru.kermilov.targetaudience.vkadapter.repository;

import org.springframework.data.repository.CrudRepository;

import ru.kermilov.targetaudience.vkadapter.domain.GroupEntity;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {

}
