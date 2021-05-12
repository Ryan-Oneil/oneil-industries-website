INSERT INTO album (id, name, creator) VALUES ('albumID', 'albumName', 'albumCreator');
INSERT INTO album (id, name, creator) VALUES ('test', 'test', 'test');

INSERT INTO users (id, username, password, enabled, email, role) VALUES (34, 'albumCreator', 'test', 1, 'harrison.campbell0101@gmail.com', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (69, 'test', 'test', 1, 'test@example.com', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (70, 'test2', 'test', 1, 'morinlynn@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (71, 'user1', 'test', 1, 'francispate@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (72, 'user2', 'test', 1, 'lessiehubbard@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (73, 'henrywhitaker', 'test', 1, 'henrywhitaker@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (74, 'mcfarlandsloan', 'test', 1, 'mcfarlandsloan@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (75, 'zamorarosario', 'test', 1, 'zamorarosario@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (76, 'nealworkman', 'test', 1, 'nealworkman@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (77, 'vernacantu', 'test', 1, 'vernacantu@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (78, 'rodriquezelliott', 'test', 1, 'rodriquezelliott@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (79, 'genevagonzalez', 'test', 1, 'genevagonzalez@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (80, 'rayharper', 'test', 1, 'rayharper@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (81, 'lizziedonovan', 'test', 1, 'lizziedonovan@oneilindustries.biz', 'ROLE_UNREGISTERED');
INSERT INTO users (id, username, password, enabled, email, role) VALUES (82, 'maycooke', 'test', 1, 'maycooke@oneilindustries.biz', 'ROLE_UNREGISTERED');

INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (1, 'Zfhb4Lg6uEI7MkTT.png', 'Zfhb4Lg6uEI7MkTT.png', 'unlisted', 'albumCreator', '2021/05/06', 'albumID', 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (2, 'test', 'test', 'public', 'user1', '2021/05/06', null, 'IMAGE', '6219');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (3, 'PTM1WRzVcwvmVXa8.png', 'PTM1WRzVcwvmVXa8.png', 'public', 'user1', '2021/05/06', null, 'IMAGE', '6219');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (4, 'j9fAoU712TNwrWHx.png', 'j9fAoU712TNwrWHx.png', 'public', 'user1', '2021/05/06', null, 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (5, '09sHejF2d8hyw5mk.gif', '09sHejF2d8hyw5mk.gif', 'public', 'user1', '2021/05/06', null, 'IMAGE', '86931');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (6, 'r3jwFEoReVZTHSlJ.jpg', 'r3jwFEoReVZTHSlJ.jpg', 'public', 'user1', '2021/05/06', null, 'IMAGE', '384030');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (7, 'lhypaJMPMvSDJs63.mp4', 'lhypaJMPMvSDJs63.mp4', 'unlisted', 'user2', '2021/05/06', null, 'VIDEO', '1188622');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (8, 'KBtQJ0Ik4yZf7Ql4.png', 'KBtQJ0Ik4yZf7Ql4.png', 'unlisted', 'user2', '2021/05/06', null, 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (9, 'ygJWYQ9YBA1thrCb.png', 'ygJWYQ9YBA1thrCb.png', 'unlisted', 'user2', '2021/05/06', null, 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (10, 'bpsxdqcHUgS70996.png', 'bpsxdqcHUgS70996.png', 'unlisted', 'user2', '2021/05/06', null, 'IMAGE', '23853');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (11, 'asovdVPKH0Gia0LV.png', 'asovdVPKH0Gia0LV.png', 'unlisted', 'user2', '2021/05/06', null, 'IMAGE', '73460');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (12, 'haUx9ALbasMcQEGs.jpg', 'haUx9ALbasMcQEGs.jpg', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '289033');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (13, 'hQwQSu4TxOrcCfpz.png', 'hQwQSu4TxOrcCfpz.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '110374');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (14, 'hZLePetp4iXnfeEK.png', 'hZLePetp4iXnfeEK.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '81920');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (15, 'YBfrZaRqy3EHPTZO.png', 'YBfrZaRqy3EHPTZO.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '73460');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (16, 'Azk9PqcWvqW6WwPP.png', 'Azk9PqcWvqW6WwPP.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '110374');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (17, 'iFOQt1rVsT5MJ6Tv.png', 'iFOQt1rVsT5MJ6Tv.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (18, 'uYOHAkEEu00SCR7u.png', 'uYOHAkEEu00SCR7u.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '23853');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (19, 'p59ik3ac3NzHqlV9.jpg', 'p59ik3ac3NzHqlV9.jpg', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '289033');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (20, 'TH1Noz2reC0Hj2JQ.png', 'TH1Noz2reC0Hj2JQ.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '73460');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (21, 'wjvH8k025pkLyjwu.png', 'wjvH8k025pkLyjwu.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '110374');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (22, 'ZpZJ4AL09mjOghsy.png', 'ZpZJ4AL09mjOghsy.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '81920');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (23, 'z83KUVKnZbZAkf1N.png', 'z83KUVKnZbZAkf1N.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '73460');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (24, 'gQ0t4eieiLZYawgv.png', 'gQ0t4eieiLZYawgv.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (25, 'EESa3ZzRzbUxWw0l.png', 'EESa3ZzRzbUxWw0l.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '210253');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (26, 'pyQEDMA1CReCj8o3.png', 'pyQEDMA1CReCj8o3.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '73460');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (27, 'QHeMo5JEy3y2V74n.png', 'QHeMo5JEy3y2V74n.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '73460');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (28, 'Bvv6iX3qSZMrG8hy.png', 'Bvv6iX3qSZMrG8hy.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '110374');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (29, 'bzommRgaWk2bdKK0.png', 'bzommRgaWk2bdKK0.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '81920');
INSERT INTO media (id, name, file_name, link_status, uploader, date_added, album_id, media_type, size) VALUES (30, 'KBQjavVK2zUcHOeU.png', 'KBQjavVK2zUcHOeU.png', 'unlisted', 'test', '2021/05/06', null, 'IMAGE', '73460');

INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('aa98m1shTbTz7gdf', 'test', '2021-05-08 11:46:44', '', 1401, '2021-04-24 12:47:20', 1);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('kaUDrVvUhmK57ZH4', 'test', '2021-05-11 10:19:44', '', 1188622, '2021-04-27 11:19:50', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('VbZVNf3mleSxJYYv', 'test', '2021-05-11 10:20:04', '', 34248174, '2021-04-27 11:20:10', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('oazrFS4a1jfjahwK', 'test', '2021-05-11 10:20:32', '', 1188622, '2021-04-27 11:20:52', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('TiuHzRFhKgDFz0xM', 'test', '2021-05-11 10:22:44', '', 1188622, '2021-04-27 11:22:52', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('nycgRvLtmPBLQIiy', 'test', '2021-05-11 10:25:16', '', 1000, '2021-04-27 11:25:22', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('ds8GCHN1797umVZJ', 'test', '2021-05-11 10:25:42', '', 1000, '2021-04-27 11:25:48', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('UeC0ncr32iZvV7Ol', 'test', '2021-05-11 10:27:59', '', 1000, '2021-04-27 11:28:19', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('wNR8CAunSwRK0EOv', 'test', '2021-05-11 10:49:29', '', 1401, '2021-04-27 11:49:44', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('cjhYg9oqDM87Wh18', 'test', '2021-05-11 10:54:29', '', 1401, '2021-04-27 11:54:35', 1);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('xrwZVI3WSsOw8JPn', 'test', '2021-05-11 10:56:03', '', 1188622, '2021-04-27 11:56:12', 1);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('vpk20IZf0LkQW5yi', 'test', '2021-05-11 20:26:44', '', 108279, '2021-04-27 21:41:03', 1);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('fKlpZEH869G6D8CI', 'test', '2021-05-11 20:46:44', '', 192912, '2021-04-27 21:46:47', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('WdBaGw7RsYFaKkkB', 'test', '2021-05-13 22:08:24', '', 1000, '2021-04-29 23:08:28', 0);
INSERT INTO link (id, creator, expiry_datetime, title, size, creation_date, views) VALUES ('Yoy1j229znwU9sfu', 'test', '2021-05-15 15:08:08', '', 1000, '2021-05-01 16:08:14', 0);

INSERT INTO file (id, name, size, link_id) VALUES ('lBOmv010VZLaK61o', 'image001.jpg', 1401, 'aa98m1shTbTz7gdf');
INSERT INTO file (id, name, size, link_id) VALUES ('lCBUcRMj70AxANPL', 'generic-avatar.png', 28207, 'aa98m1shTbTz7gdf');
INSERT INTO file (id, name, size, link_id) VALUES ('lcRCFjlN9qE9NrR7', 'generic-avatar.png', 28207, 'kaUDrVvUhmK57ZH4');
INSERT INTO file (id, name, size, link_id) VALUES ('ld3w54TbQZJ3bhpG', 'generic-avatar.png', 28207, 'VbZVNf3mleSxJYYv');
INSERT INTO file (id, name, size, link_id) VALUES ('LdiYWhYtlUPFcKdo', 'generic-avatar.png', 28207, 'oazrFS4a1jfjahwK');
INSERT INTO file (id, name, size, link_id) VALUES ('LDlFffeqSNt0SRiE', 'generic-avatar.png', 28207, 'TiuHzRFhKgDFz0xM');
INSERT INTO file (id, name, size, link_id) VALUES ('lekQNBlHleZ5tnk0', 'generic-avatar.png', 28207, 'nycgRvLtmPBLQIiy');
INSERT INTO file (id, name, size, link_id) VALUES ('lensyS5Ky2kurFfy', 'generic-avatar.png', 28207, 'ds8GCHN1797umVZJ');
INSERT INTO file (id, name, size, link_id) VALUES ('LfO0mVXUVMZ4HGcL', 'generic-avatar.png', 28207, 'UeC0ncr32iZvV7Ol');
INSERT INTO file (id, name, size, link_id) VALUES ('lG4ctjNXbPrWspb5', 'generic-avatar.png', 28207, 'wNR8CAunSwRK0EOv');
INSERT INTO file (id, name, size, link_id) VALUES ('LI1XC77uS0jFGzsK', 'generic-avatar.png', 28207, 'cjhYg9oqDM87Wh18');
INSERT INTO file (id, name, size, link_id) VALUES ('LJAAWEdLR5byYxPX', 'generic-avatar.png', 28207, 'xrwZVI3WSsOw8JPn');
INSERT INTO file (id, name, size, link_id) VALUES ('LKbyceAgmUKUSuvW', 'generic-avatar.png', 28207, 'vpk20IZf0LkQW5yi');
INSERT INTO file (id, name, size, link_id) VALUES ('Ll5dasivEZtCHFIA', 'generic-avatar.png', 28207, 'fKlpZEH869G6D8CI');
INSERT INTO file (id, name, size, link_id) VALUES ('LL7CrtidsbbDelYg', 'generic-avatar.png', 28207, 'WdBaGw7RsYFaKkkB');
INSERT INTO file (id, name, size, link_id) VALUES ('lLgwyPJNG1AlFEX5', 'generic-avatar.png', 28207, 'Yoy1j229znwU9sfu');
INSERT INTO file (id, name, size, link_id) VALUES ('lMDquxjIj3MUFDpe', 'generic-avatar.png', 28207, 'Yoy1j229znwU9sfu');

INSERT INTO link_view (id, ip, link_id, date_time) VALUES (19, '0.0.0.0', 'aa98m1shTbTz7gdf', '2020-06-07');
INSERT INTO link_view (id, ip, link_id, date_time) VALUES (20, '0.0.0.0', 'kaUDrVvUhmK57ZH4', '2020-06-07');
INSERT INTO link_view (id, ip, link_id, date_time) VALUES (21, '0.0.0.0', 'fKlpZEH869G6D8CI', '2020-06-11');
INSERT INTO link_view (id, ip, link_id, date_time) VALUES (22, '0.0.0.0', 'Yoy1j229znwU9sfu', '2020-06-11');

INSERT INTO public_media_approval (media_id, public_name, status) VALUES (12, 'TAMdr530hnUgNCRd.png', 'pending');
INSERT INTO public_media_approval (media_id, public_name, status) VALUES (13, 'Pd04GfA1zi9xRVOD.jpg', 'pending');
INSERT INTO public_media_approval (media_id, public_name, status) VALUES (14, 'loGHdJ154SvRejxu.png', 'pending');
INSERT INTO public_media_approval (media_id, public_name, status) VALUES (15, '65Rz9IPpTg0jXMI9.png', 'pending');

INSERT INTO quota (username, used, max, ignore_quota) VALUES ('test1', 2500, 25, 0);
INSERT INTO quota (username, used, max, ignore_quota) VALUES ('test2', 36, 6, 0);

INSERT INTO password_reset_token (id, token, username, expiry_date) VALUES (4, '19fcfd72-5532-44bb-9b06-90351cc6ec6d', 70, '2021-05-09 00:19:54');

INSERT INTO roles (id, role) VALUES (1, 'ROLE_USER');

INSERT INTO verificationtoken (id, token, username, expiry_date) VALUES (1, 'e1d59296-5167-4986-897a-324c170f6e0f', '70', '2020-09-16 16:01:08');
INSERT INTO verificationtoken (id, token, username, expiry_date) VALUES (2, '4f8fe42f-255f-471d-9e93-b8a06d1b0c2f', '69', '2021-05-08 19:23:13');