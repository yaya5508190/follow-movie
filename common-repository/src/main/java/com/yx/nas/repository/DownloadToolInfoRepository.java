package com.yx.nas.repository;

import com.yx.nas.entity.DownloadToolInfo;
import com.yx.nas.entity.DownloadToolInfoFetcher;
import com.yx.nas.entity.DownloadToolInfoTable;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * DownloadToolInfoRepository 接口
 * </p>
 *
 * @author yx
 * @date 2025-10-08
 */
@Repository
public class DownloadToolInfoRepository extends AbstractJavaRepository<DownloadToolInfo, Long> {
    private static final DownloadToolInfoTable table = DownloadToolInfoTable.$;

    public DownloadToolInfoRepository(JSqlClient sql) {
        super(sql);
    }

    /**
     * 根据id和type查询
     */
    public DownloadToolInfo findByIdAndType(Long id, int type) {
        return sql
                .createQuery(table)
                .where(table.id().eq(id))
                .where(table.type().eq(type))
                .select(table.fetch(DownloadToolInfoFetcher.$.allScalarFields()))
                .fetchFirstOrNull();
    }

}

