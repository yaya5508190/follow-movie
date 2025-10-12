package com.yx.nas.repository;

import com.yx.nas.dto.ApiKeyMediaFetchConfig;
import com.yx.nas.dto.CookieMediaFetchConfig;
import com.yx.nas.dto.UserNamePasswordMediaFetchConfig;
import com.yx.nas.entity.MediaFetchConfig;
import com.yx.nas.entity.MediaFetchConfigTable;
import com.yx.nas.enums.AuthTypeEnum;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * MediaFetchConfigRepository 接口
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Repository
public class MediaFetchConfigRepository extends AbstractJavaRepository<MediaFetchConfig, Long> {

    private static final MediaFetchConfigTable table = MediaFetchConfigTable.$;

    public MediaFetchConfigRepository(JSqlClient sql) {
        super(sql);
    }

    /**
     * 根据来源站点和认证类型查询认证信息
     */
    @SuppressWarnings("unchecked")
    public <V extends View<MediaFetchConfig>> V findBySourceAndType(String fetcherSource, Integer ConfigType) {
        Class<? extends View<MediaFetchConfig>> viewType = switch (AuthTypeEnum.fromCode(ConfigType)) {
            case API_KEY -> ApiKeyMediaFetchConfig.class;
            case USERNAME_PASSWORD -> UserNamePasswordMediaFetchConfig.class;
            case COOKIE -> CookieMediaFetchConfig.class;
        };

        // 暂时每个站点只能设置一个配置
        return sql
                .createQuery(table)
                .where(
                        Predicate.and(
                                table.authType().eq(ConfigType),
                                table.fetcherSource().eq(fetcherSource)
                        )
                ).select(table.fetch((Class<V>) viewType)).fetchFirstOrNull();
    }
}

