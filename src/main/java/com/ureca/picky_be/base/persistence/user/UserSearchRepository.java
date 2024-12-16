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
          { "prefix": { "nickname": "?0" } }  // 앞에서부터 일치
        ],
        "must_not": [
          { "term": { "role.keyword": "ADMIN" } },   // ADMIN 제외
          { "term": { "status.keyword": "SUSPENDED" } }  // SUSPENDED 제외
        ]
      }
    }
    """)
    List<UserDocument> findByNicknameExcludingAdminAndSuspended(String nickname);
}