// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addGenerator POST /api/Generator/add */
export async function addGeneratorUsingPost(
  body: API.GeneratorAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/Generator/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editGenerator POST /api/Generator/edit */
export async function editGeneratorUsingPost(
  body: API.GeneratorEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/Generator/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getGeneratorVOById GET /api/Generator/get/vo */
export async function getGeneratorVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getGeneratorVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseGeneratorVO_>('/api/Generator/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listGeneratorByPage POST /api/Generator/list/page */
export async function listGeneratorByPageUsingPost(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGenerator_>('/api/Generator/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listGeneratorVOByPage POST /api/Generator/list/page/vo */
export async function listGeneratorVoByPageUsingPost(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGeneratorVO_>('/api/Generator/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyGeneratorVOByPage POST /api/Generator/my/list/page/vo */
export async function listMyGeneratorVoByPageUsingPost(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGeneratorVO_>('/api/Generator/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateGenerator POST /api/Generator/update */
export async function updateGeneratorUsingPost(
  body: API.GeneratorUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/Generator/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** useGenerator POST /api/Generator/use */
export async function useGeneratorUsingPost(
  body: API.GeneratorUseRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/Generator/use', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
