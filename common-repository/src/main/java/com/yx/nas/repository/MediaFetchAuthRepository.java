package com.yx.nas.repository;
import com.yx.nas.dto.ApiKeyMediaFetchAuth;
import com.yx.nas.dto.CookieMediaFetchAuth;
import com.yx.nas.dto.UserNamePasswordMediaFetchAuth;
import com.yx.nas.entity.MediaFetchAuth;
import com.yx.nas.entity.MediaFetchAuthTable;
import com.yx.nas.enums.AuthTypeEnum;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * MediaFetchAuthRepository 接口
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Repository
public class MediaFetchAuthRepository extends AbstractJavaRepository<MediaFetchAuth, Long> {

    private static final MediaFetchAuthTable table = MediaFetchAuthTable.$;

    public MediaFetchAuthRepository(JSqlClient sql) {
        super(sql);
    }

    /**
     * 根据来源站点和认证类型查询认证信息
     */
    @SuppressWarnings("unchecked")
    public <V extends View<MediaFetchAuth>> V findBySourceAndType(String fetcherSource, Integer authType) {
        Class<? extends View<MediaFetchAuth>> viewType = switch (AuthTypeEnum.fromCode(authType)) {
            case API_KEY -> ApiKeyMediaFetchAuth.class;
            case USERNAME_PASSWORD -> UserNamePasswordMediaFetchAuth.class;
            case COOKIE -> CookieMediaFetchAuth.class;
        };

        return sql
                .createQuery(table)
                .where(
                        Predicate.and(
                                table.authType().eq(authType),
                                table.fetcherSource().eq(fetcherSource)
                        )
                ).select(table.fetch((Class<V>)viewType)).fetchFirstOrNull();
    }
}

