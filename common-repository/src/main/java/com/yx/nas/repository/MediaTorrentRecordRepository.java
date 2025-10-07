package com.yx.nas.repository;

import com.yx.nas.entity.MediaTorrentRecord;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * MediaTorrentRecordRepository 接口
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Repository
public class MediaTorrentRecordRepository extends AbstractJavaRepository<MediaTorrentRecord, Long> {
    public MediaTorrentRecordRepository(JSqlClient sql) {
        super(sql);
    }
}

