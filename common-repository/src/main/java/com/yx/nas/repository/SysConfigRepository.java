package com.yx.nas.repository;

import com.yx.nas.dto.SimpleSysConfigView;
import com.yx.nas.entity.SysConfig;
import com.yx.nas.entity.SysConfigTable;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

@Repository
public class SysConfigRepository extends AbstractJavaRepository<SysConfig, Long> {
    private static final SysConfigTable table = SysConfigTable.$;


    public SysConfigRepository(JSqlClient sql) {
        super(sql);
    }

    //根据type和key查询配置
    public SimpleSysConfigView findByTypeAndKey(String type, String key) {
        return sql
                .createQuery(table)
                .where(Predicate.and(table.configKey().eq(key), table.configType().eq(type)))
                .select(table.fetch(SimpleSysConfigView.class))
                .fetchOne();
    }
}
