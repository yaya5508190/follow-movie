package com.yx.nas.interceptor;

import com.yx.nas.entity.base.BaseEntity;
import com.yx.nas.entity.base.BaseEntityDraft;
import com.yx.nas.entity.base.BaseEntityProps;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseEntityDraftInterceptor implements DraftInterceptor<BaseEntity, BaseEntityDraft> {

    @Override
    public void beforeSave(@NotNull BaseEntityDraft draft, @Nullable BaseEntity original) {
        if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.UPDATE_TIME)) {
            draft.setUpdateTime(LocalDateTime.now());
        }

        if (original == null) {
            if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.CREATE_TIME)) {
                draft.setCreateTime(LocalDateTime.now());
            }
        }
    }
}
