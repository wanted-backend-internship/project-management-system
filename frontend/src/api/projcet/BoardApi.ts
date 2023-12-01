import axiosInstance from '../handler/RequestInterceptor.ts';
import {UnwrapRef} from "vue";

export const createBoard = async (boardData: {
    title: string;
}) => {
    await axiosInstance.post('/boards', boardData);
};

export const displayBoards = async () => {
    const response = await axiosInstance.get('/boards/me');
    return response;
}

export const updateBoard = async (boardData: {
    boardTitle: string;
    boardId: string;
}) => {
    const response = await axiosInstance.patch('/boards', boardData);
    return response;
};

export const deleteBoard = async (boardId: string) => {
    await axiosInstance.delete(`/boards/${boardId}`);
};

export const boardDetail = async (boardId: UnwrapRef<() => string>) => {
    const response = await axiosInstance.get(`/boards/${boardId}`);
    return response;
}