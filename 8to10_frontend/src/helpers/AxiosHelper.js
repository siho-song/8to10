import { HEADERS, MEDIA_TYPES } from "@/constants/RequestHeaderConfig.js";
import {
    API_ENDPOINTS_METHOD,
    CORS_REQUIRED_ENDPOINTS,
    JSON_ENDPOINTS, MULTIPART_ENDPOINTS,
    URL_ENCODED_ENDPOINTS
} from "@/constants/ApiEndPoints.js";
import {buildBearerToken} from "@/helpers/TokenManager.js";
import authenticatedApi from "@/api/AuthenticatedApi.js";

export const setAuthorizationHeaders = (config) => {
    const accessToken = localStorage.getItem('Authorization');
    if (accessToken) {
        config.headers[HEADERS.AUTHORIZATION] = buildBearerToken(accessToken);
    }
}


export const setContentTypeHeaders = (config) => {
    if (isJSONEndPoint(config)) {
        config.headers[HEADERS.CONTENT_TYPE] = MEDIA_TYPES.JSON;
    } else if (isUrlEncodedEndPoint(config)) {
        config.headers[HEADERS.CONTENT_TYPE] = MEDIA_TYPES.FORM_URL_ENCODED;
    } else if (isMultipartEndPoint(config)) {
        config.headers[HEADERS.CONTENT_TYPE] = MEDIA_TYPES.MULTIPART;
    }
}

export const setAcceptHeaders = (config) => {
    config.headers[HEADERS.ACCEPT] = MEDIA_TYPES.JSON;
}

export const setWithCredentials = (config) => {
    if (isCORSEndPoint(config)) {
        config.withCredentials = true;
    }
}

const isValidMethod = (config) => {
    return config.method.toUpperCase() === API_ENDPOINTS_METHOD[config.apiEndPoint];
}

const isCORSEndPoint = (config) => {
    return CORS_REQUIRED_ENDPOINTS.includes(config.apiEndPoint)
        && isValidMethod(config);
}

const isJSONEndPoint = (config) => {
    return JSON_ENDPOINTS.includes(config.apiEndPoint)
        && isValidMethod(config);
}

const isUrlEncodedEndPoint = (config) => {
    return URL_ENCODED_ENDPOINTS.includes(config.apiEndPoint)
        && isValidMethod(config);
}

const isMultipartEndPoint = (config) => {
    return MULTIPART_ENDPOINTS.includes(config.apiEndPoint);
}

let externalLogoutHandler = null;

export const setLogoutHandler = (logoutFunction) => {
    externalLogoutHandler = logoutFunction;
};

export const callLogoutHandler = () => {
    if (externalLogoutHandler) {
        externalLogoutHandler();
    }
};

export const isSuccess = async (response) => {
    const axiosResponse = await response;
    return 200 <= axiosResponse.status && axiosResponse.status < 300;
}