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
