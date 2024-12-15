package com.ureca.picky_be.base.persistence.user;

import com.ureca.picky_be.elasticsearch.document.user.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {

    @Query("""
    {
      "bool": {
        "must": [
          { "match": { "nickname": "?0" } }
        ],
        "must_not": [
          { "term": { "role.keyword": "ADMIN" } },
          { "term": { "status.keyword": "SUSPENDED" } }
        ]
      }
    }
    """)
    List<UserDocument> findByNicknameExcludingAdminAndSuspended(String nickname);
}