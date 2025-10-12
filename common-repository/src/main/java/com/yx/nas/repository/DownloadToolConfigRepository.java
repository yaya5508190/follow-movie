package com.yx.nas.repository;

import com.yx.nas.dto.DownloadToolConfigView;
import com.yx.nas.entity.DownloadToolConfig;
import com.yx.nas.entity.DownloadToolConfigFetcher;
import com.yx.nas.entity.DownloadToolConfigTable;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * DownloadToolConfigRepository 接口
 * </p>
 *
 * @author yx
 * @date 2025-10-08
 */
@Repository
public class DownloadToolConfigRepository extends AbstractJavaRepository<DownloadToolConfig, Long> {
    private static final DownloadToolConfigTable table = DownloadToolConfigTable.$;

    public DownloadToolConfigRepository(JSqlClient sql) {
        super(sql);
    }

    /**
     * 根据id和type查询
     */
    public DownloadToolConfig findByIdAndType(Long id, String type) {
        return sql
                .createQuery(table)
                .where(table.id().eq(id))
                .where(table.type().eq(type))
                .select(table.fetch(DownloadToolConfigFetcher.$.allScalarFields()))
                .fetchFirstOrNull();
    }

    /**
     * 根据站点类型或者是否默认查询
     */
    public DownloadToolConfigView findByTypeOrDefault(Long mediaFetchConfigId) {
        //查询站点对应的下载工具
        DownloadToolConfigView mediaFetchDownloadToolConfigView = sql.createQuery(table)
                .where(
                        table.mediaFetchConfigs(
                                config -> config.id().eq(mediaFetchConfigId)
                        )
                )
                .select(table.fetch(DownloadToolConfigView.class))
                .fetchFirstOrNull();
        if (mediaFetchDownloadToolConfigView != null) {
            return mediaFetchDownloadToolConfigView;
        } else {
            //查询默认的下载工具
            return sql.createQuery(table)
                    .where(
                            table.defaultTool().eq(true)
                    )
                    .select(table.fetch(DownloadToolConfigView.class))
                    .fetchFirstOrNull();
        }
    }

}

