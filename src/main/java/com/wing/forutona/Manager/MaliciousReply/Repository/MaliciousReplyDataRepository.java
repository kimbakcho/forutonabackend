package com.wing.forutona.Manager.MaliciousReply.Repository;

import com.wing.forutona.Manager.MaliciousReply.Domain.MaliciousReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaliciousReplyDataRepository extends JpaRepository<MaliciousReply,Integer> {

    Optional<MaliciousReply> findByReplyUuid(String replyUuid);


}
