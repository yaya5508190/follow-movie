package com.yx.nas.tool.plugins.service.impl;

import com.yx.nas.dto.ApiKeyMediaFetchConfig;
import com.yx.nas.dto.ApiKeyMediaFetchConfigInput;
import com.yx.nas.enums.AuthTypeEnum;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.MediaFetchConfigRepository;
import com.yx.nas.tool.plugins.MediaFetchPluginConfig;
import com.yx.nas.tool.plugins.enums.ResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.yx.nas.tool.plugins.enums.ResultEnum.SETTING_NOT_EXIST;

@Service
public class MediaResourceFetcherSettingImpl {
    private final MediaFetchPluginConfig mediaFetchPluginConfig;
    private final MediaFetchConfigRepository mediaFetchConfigRepository;

    public MediaResourceFetcherSettingImpl(
            MediaFetchPluginConfig mediaFetchPluginConfig,
            MediaFetchConfigRepository mediaFetchConfigRepository) {
        this.mediaFetchPluginConfig = mediaFetchPluginConfig;
        this.mediaFetchConfigRepository = mediaFetchConfigRepository;
    }

    /**
     * 保存设置
     *
     * @return 是否保存成功
     */
    @Transactional
    public CommonResult<Void> saveSetting(ApiKeyMediaFetchConfigInput config) {
        //校验同FetcherSource的配置是否存在
        ApiKeyMediaFetchConfig existingConfig = mediaFetchConfigRepository.findBySourceAndType(
                mediaFetchPluginConfig.name(),
                AuthTypeEnum.API_KEY.getCode()
        );
        if (existingConfig == null) {
            // 新增配置
            config.setId(null);
            config.setFetcherSource(mediaFetchPluginConfig.name());
            config.setAuthType(AuthTypeEnum.API_KEY.getCode());
            mediaFetchConfigRepository.save(config);
            return CommonResult.success();
        } else {
            // 更新配置首先比对id是否相同，如果不同说明不应该更新
            if (Objects.equals(existingConfig.getId(), config.getId())) {
                mediaFetchConfigRepository.save(config);
                mediaFetchConfigRepository.save(config);
                return CommonResult.success();
            } else {
                return CommonResult.failure(ResultEnum.SETTING_EXIST.getCode(), ResultEnum.SETTING_EXIST.getMessage());
            }
        }
    }

    /**
     * 获取设置
     *
     * @return 配置
     */
    public CommonResult<ApiKeyMediaFetchConfigInput> getSetting() {
        ApiKeyMediaFetchConfig existingConfig = mediaFetchConfigRepository.findBySourceAndType(
                mediaFetchPluginConfig.name(),
                AuthTypeEnum.API_KEY.getCode()
        );
        if (existingConfig != null) {
            ApiKeyMediaFetchConfigInput configInput = new ApiKeyMediaFetchConfigInput();
            configInput.setId(existingConfig.getId());
            configInput.setName(existingConfig.getName());
            configInput.setFetcherSource(existingConfig.getFetcherSource());
            configInput.setUrl(existingConfig.getUrl());
            configInput.setAuthType(AuthTypeEnum.API_KEY.getCode());
            configInput.setApiKey(existingConfig.getApiKey());
            return CommonResult.success(configInput);
        } else {
            return CommonResult.failure(SETTING_NOT_EXIST.getCode(), SETTING_NOT_EXIST.getMessage());
        }
    }
}
