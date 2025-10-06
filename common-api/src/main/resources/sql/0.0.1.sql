create database follow_movie;

--系统配置表
drop table if exists sys_config;
CREATE TABLE sys_config
(
    id              BIGSERIAL PRIMARY KEY,
    config_type     VARCHAR(50)  NOT NULL,
    config_name     VARCHAR(255) NOT NULL,
    config_key      VARCHAR(255) NOT NULL,
    config_value    VARCHAR(255) NOT NULL,
    remark          VARCHAR(255),
    create_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_config IS '系统配置表';
COMMENT ON COLUMN sys_config.id IS '主键';
COMMENT ON COLUMN sys_config.config_type IS '配置类型';
COMMENT ON COLUMN sys_config.config_name IS '配置名称';
COMMENT ON COLUMN sys_config.config_key IS '配置键';
COMMENT ON COLUMN sys_config.config_value IS '配置值';
COMMENT ON COLUMN sys_config.remark IS '备注';
COMMENT ON COLUMN sys_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_config.update_time IS '更新时间';

CREATE UNIQUE INDEX idx_sys_config_type_key ON sys_config(config_type, config_key);