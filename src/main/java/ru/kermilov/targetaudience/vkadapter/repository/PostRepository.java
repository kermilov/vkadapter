package ru.kermilov.targetaudience.vkadapter.repository;

import org.springframework.data.repository.CrudRepository;

import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;

public interface PostRepository extends CrudRepository<PostEntity, Long> {

}
