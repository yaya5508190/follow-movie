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
        //如果id为空，说明是新增配置，如果可以查出数据直接返回数据已经存在的错误
        if (Objects.isNull(config.getId())) {
            ApiKeyMediaFetchConfig existingConfig = mediaFetchConfigRepository.findByPluginIdAndType(
                    mediaFetchPluginConfig.pluginId(),
                    AuthTypeEnum.API_KEY.getCode()
            );
            if (Objects.nonNull(existingConfig)) {
                return CommonResult.failure(ResultEnum.SETTING_EXIST.getCode(), ResultEnum.SETTING_EXIST.getMessage());
            }
        }

        config.setFetcherSource(mediaFetchPluginConfig.name());
        config.setAuthType(AuthTypeEnum.API_KEY.getCode());
        config.setPluginId(mediaFetchPluginConfig.pluginId());
        mediaFetchConfigRepository.save(config.toEntity());
        return CommonResult.success();
    }

    /**
     * 获取设置
     *
     * @return 配置
     */
    public CommonResult<ApiKeyMediaFetchConfigInput> getSetting(Long id) {
        ApiKeyMediaFetchConfig existingConfig = mediaFetchConfigRepository.findById(id,ApiKeyMediaFetchConfig.class);
        if (existingConfig != null) {
            ApiKeyMediaFetchConfigInput configInput = new ApiKeyMediaFetchConfigInput();
            configInput.setId(existingConfig.getId());
            configInput.setName(existingConfig.getName());
            configInput.setFetcherSource(existingConfig.getFetcherSource());
            configInput.setUrl(existingConfig.getUrl());
            configInput.setPluginId(mediaFetchPluginConfig.pluginId());
            configInput.setAuthType(AuthTypeEnum.API_KEY.getCode());
            configInput.setApiKey(existingConfig.getApiKey());
            return CommonResult.success(configInput);
        } else {
            return CommonResult.failure(SETTING_NOT_EXIST.getCode(), SETTING_NOT_EXIST.getMessage());
        }
    }

    /**
     * 删除设置
     *
     * @param id 配置ID
     * @return 删除结果
     */
    @Transactional
    public CommonResult<Void> deleteSetting(Long id) {
        mediaFetchConfigRepository.deleteById(id);
        return CommonResult.success();
    }
}
