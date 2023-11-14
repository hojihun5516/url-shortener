create table shorten_url
(
    id             bigint unsigned auto_increment
        primary key,
    original_url   text    not null  comment '원본 URL',
    short_key      varchar(31)     null comment '단축 Key',
    created_at     datetime         not null default CURRENT_TIMESTAMP,
    updated_at     datetime         not null default CURRENT_TIMESTAMP,

    constraint shorten_url_origin_url_uindex unique (origin_url),
    constraint shorten_url_short_key_uindex unique (short_key)
);
