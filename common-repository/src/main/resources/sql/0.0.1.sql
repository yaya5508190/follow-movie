create database follow_movie;
drop table if exists media_fetch_config;

/*==============================================================*/
/* Table: media_fetch_config                                    */
/*==============================================================*/
create table media_fetch_config
(
    id             BIGSERIAL     not null,
    name           VARCHAR(150)  not null,
    url            VARCHAR(255)  not null,
    api_key        VARCHAR(150)  null,
    user_name      VARCHAR(150)  null,
    password       VARCHAR(150)  null,
    auth_cookie    VARCHAR(3000) null,
    auth_type      INT2          null     default 1,
    fetcher_source VARCHAR(150)  not null,
    extra_metainfo JSONB         null,
    create_time    TIMESTAMP     not null default CURRENT_TIMESTAMP,
    update_time    TIMESTAMP     null,
    constraint PK_MEDIA_FETCH_CONFIG primary key (id)
);

comment on table media_fetch_config is
    '下载站点认证信息，用于访问资源下载站点';

comment on column media_fetch_config.name is
    '站点名称';

comment on column media_fetch_config.url is
    '站点URL';

comment on column media_fetch_config.api_key is
    '访问密钥';

comment on column media_fetch_config.user_name is
    '访问用户名';

comment on column media_fetch_config.password is
    '访问密码';

comment on column media_fetch_config.auth_cookie is
    '认证Cookie';

comment on column media_fetch_config.auth_type is
    '1:api_key 2: 用户名密码 3: cookie';

comment on column media_fetch_config.fetcher_source is
    '来源站点';

comment on column media_fetch_config.extra_metainfo is
    '额外元数据';

comment on column media_fetch_config.create_time is
    '创建时间';

comment on column media_fetch_config.update_time is
    '更新时间';

/*==============================================================*/
/* Table: media_torrent_record                                  */
/*==============================================================*/
create table media_torrent_record
(
    id             BIGSERIAL    not null,
    resource_id    VARCHAR(150) not null,
    fetcher_source VARCHAR(150) not null,
    torrent_url    VARCHAR(500) not null,
    torrent_status INT2         not null default 1,
    create_time    TIMESTAMP    not null default CURRENT_TIMESTAMP,
    update_time    TIMESTAMP    null,
    constraint PK_MEDIA_TORRENT_RECORD primary key (id)
);

comment on table media_torrent_record is
    '种子下载记录表';

comment on column media_torrent_record.fetcher_source is
    '来源站点';

comment on column media_torrent_record.torrent_status is
    '种子状态 1: 待下载  2:已下载';

comment on column media_torrent_record.create_time is
    '创建时间';

comment on column media_torrent_record.update_time is
    '更新时间';

/*==============================================================*/
/* Index: u_idx_resource_fetcher                                */
/*==============================================================*/
create unique index u_idx_resource_fetcher on media_torrent_record (resource_id, fetcher_source);

drop index if exists u_idx_name;

drop table if exists download_tool_config;

/*==============================================================*/
/* Table: download_tool_config                                  */
/*==============================================================*/
create table download_tool_config
(
    id           BIGSERIAL     not null,
    name         VARCHAR(255)  not null,
    auth_type    INT2          not null default 1,
    type         VARCHAR(100)  not null,
    url          VARCHAR(255)  not null,
    username     VARCHAR(255)  null,
    password     VARCHAR(255)  null,
    cookie       VARCHAR(2000) null,
    save_path    VARCHAR(1000) not null,
    default_tool BOOLEAN       not null,
    create_time  TIMESTAMP     not null default CURRENT_TIMESTAMP,
    update_time  TIMESTAMP     null,
    constraint PK_DOWNLOAD_TOOL_CONFIG primary key (id)
);

comment on column download_tool_config.id is
    'id主键';

comment on column download_tool_config.name is
    '名称';

comment on column download_tool_config.auth_type is
    '认证类型 1 用户名密码 2 cookie';

comment on column download_tool_config.type is
    '注册下载工具的插件名称';

comment on column download_tool_config.url is
    '访问地址';

comment on column download_tool_config.username is
    '用户名';

comment on column download_tool_config.password is
    '密码';

comment on column download_tool_config.cookie is
    'Cookie';

comment on column download_tool_config.save_path is
    '默认保存路径';

comment on column download_tool_config.default_tool is
    '是否默认';

comment on column download_tool_config.create_time is
    '创建时间';

comment on column download_tool_config.update_time is
    '更新时间';

/*==============================================================*/
/* Index: u_idx_name                                            */
/*==============================================================*/
create unique index u_idx_name on download_tool_config (name);


/*==============================================================*/
/* Index: u_idx_name                                            */
/*==============================================================*/
create unique index u_idx_name on download_tool_config (name);


drop index if exists u_idx_media_fetch_download;

drop table if exists media_fetch_download_rel;

/*==============================================================*/
/* Table: media_fetch_download_rel                              */
/*==============================================================*/
create table media_fetch_download_rel
(
    media_fetch_id   BIGSERIAL not null,
    download_tool_id BIGSERIAL not null
);

/*==============================================================*/
/* Index: u_idx_media_fetch_download                            */
/*==============================================================*/
create unique index u_idx_media_fetch_download on media_fetch_download_rel (media_fetch_id, download_tool_id);