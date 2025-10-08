create database follow_movie;
drop table if exists media_fetch_auth;

/*==============================================================*/
/* Table: media_fetch_auth                                      */
/*==============================================================*/
create table media_fetch_auth
(
    id             BIGSERIAL     not null,
    api_key        VARCHAR(150)  null,
    user_name      VARCHAR(150)  null,
    password       VARCHAR(150)  null,
    auth_cookie    VARCHAR(3000) null,
    auth_type      INT2          null     default 1,
    fetcher_source VARCHAR(150)  not null,
    extra_metainfo JSONB         null,
    create_time    TIMESTAMP     not null default CURRENT_TIMESTAMP,
    update_time    TIMESTAMP     null,
    constraint PK_MEDIA_FETCH_AUTH primary key (id)
);

comment on table media_fetch_auth is
    '下载站点认证信息，用于访问资源下载站点';

comment on column media_fetch_auth.api_key is
    '访问密钥
    ';

comment on column media_fetch_auth.user_name is
    '访问用户名';

comment on column media_fetch_auth.password is
    '访问密码';

comment on column media_fetch_auth.auth_cookie is
    '认证Cookie';

comment on column media_fetch_auth.auth_type is
    '1:api_key 2: 用户名密码 3: cookie';

comment on column media_fetch_auth.fetcher_source is
    '来源站点';

comment on column media_fetch_auth.extra_metainfo is
    '额外元数据';

comment on column media_fetch_auth.create_time is
    '创建时间';

comment on column media_fetch_auth.update_time is
    '更新时间';


drop index if exists u_idx_resource_fetcher;

drop table if exists media_torrent_record;

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

drop table if exists download_tool_info;

/*==============================================================*/
/* Table: download_tool_info                                    */
/*==============================================================*/
create table download_tool_info
(
    id          BIGSERIAL     not null,
    name        VARCHAR(150)  not null,
    type        INT2          not null,
    url         VARCHAR(255)  not null,
    username    VARCHAR(255)  null,
    password    VARCHAR(255)  null,
    cookie      VARCHAR(2000) null,
    save_path   VARCHAR(1000) not null,
    create_time TIMESTAMP     not null default CURRENT_TIMESTAMP,
    update_time TIMESTAMP     null,
    constraint PK_DOWNLOAD_TOOL_INFO primary key (id)
);

comment on column download_tool_info.id is
    'id主键';

comment on column download_tool_info.name is
    '名称';

comment on column download_tool_info.type is
    '工具类型 1: qbittorrent';

comment on column download_tool_info.url is
    '访问地址';

comment on column download_tool_info.username is
    '用户名';

comment on column download_tool_info.password is
    '密码';

comment on column download_tool_info.cookie is
    'Cookie';

comment on column download_tool_info.save_path is
    '默认保存路径';

comment on column download_tool_info.create_time is
    '创建时间';

comment on column download_tool_info.update_time is
    '更新时间';

/*==============================================================*/
/* Index: u_idx_name                                            */
/*==============================================================*/
create unique index u_idx_name on download_tool_info ( name );