package ru.kermilov.targetaudience.vkadapter.repository;

import org.springframework.data.repository.CrudRepository;

import ru.kermilov.targetaudience.vkadapter.domain.PostEntity;
import ru.kermilov.targetaudience.vkadapter.domain.PostStatus;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<PostEntity, Long> {
    Optional<PostEntity> findByExternalId(Integer externalId);
    List<PostEntity> findAllByStatus(PostStatus status);
    Optional<PostEntity> findByHash(String hash);

}
