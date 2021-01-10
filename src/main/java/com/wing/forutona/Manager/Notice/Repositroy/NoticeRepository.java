package com.wing.forutona.Manager.Notice.Repositroy;

import com.wing.forutona.Manager.Notice.Domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {

}
