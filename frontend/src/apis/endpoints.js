import { BASE_URL } from "./api";

export const PUBLIC_MEDIAS_ENDPOINT = "/gallery/medias";
export const USER_MEDIAS_ENDPOINT = "/gallery/user/medias/";
export const ADMIN_MEDIAS_ENDPOINT = "/gallery/admin/medias";
export const USER_ALBUMS_ENDPOINT = "/gallery/myalbums/";
export const USER_DELETE_MEDIAS_ENDPOINTS = "/gallery/medias/delete";
export const ALBUM_DELETE = "/gallery/myalbums/delete/";
export const ALBUM_CREATE = "/gallery/myalbums/create/";
export const ALBUM_UPDATE = "/gallery/myalbums/update";
export const ADMIN_GET_STATS_ENDPOINT = "/admin/stats";
export const ADMIN_GET_USERS_ENDPOINT = "/user/admin/users";
export const ADMIN_GET_MEDIAS_ENDPOINT = "/gallery/admin/medias/";
export const ADMIN_GET_PENDING_APPROVALS_ENDPOINT =
  "/gallery/admin/media/pendingApproval";
export const ADMIN_GET_ROLES = "/user/admin/roles";
export const MEDIA_IMAGE_URL = `${BASE_URL}/gallery/image/`;
export const MEDIA_VIDEO_URL = `${BASE_URL}/gallery/video/`;
