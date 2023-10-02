create table shorten_url
(
    id             bigint unsigned auto_increment
        primary key,
    original_url   text    not null  comment '원본 URL',
    short_url      varchar(100)     null comment '단축 URL',
    created_at     datetime         not null default CURRENT_TIMESTAMP,
    updated_at     datetime         not null default CURRENT_TIMESTAMP,

    constraint shorten_url_short_url_uindex unique (short_url)
);
