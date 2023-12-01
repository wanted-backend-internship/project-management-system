import axiosInstance from "../handler/RequestInterceptor";

export const createTask = async (taskData:{
    boardId: string,
    taskBoxId: string,
    taskTitle: string,
    tag: string,
    dueDate: string,
    workHour: string
}) => {
    const response = await axiosInstance.post('/boards/taskBox/task', taskData);
    return response;
}

export const updateTask = async (taskData:{
    taskId: string,
    title: string,
    tag: string,
    dueDate: string,
    workHour: string
}) => {
    const response = await axiosInstance.patch('/boards/taskBox/task', taskData);
    return response;
}

export const deleteTask = async (taskId: string) => {
    await axiosInstance.delete(`/boards/taskBox/task/${taskId}`)
}


export const moveTaskToSameTaskBox = async (taskData: {
    taskBoxId: string,
    taskId: string,
    newTaskOrder: string
}) => {
    try {
        const response = await axiosInstance.post('/boards/taskBox/task/move/same', taskData);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const moveTaskToOtherTaskBox = async (taskData: {
    prevTaskBoxId: string,
    newTaskBoxId: string,
    prevTaskOrder: string,
    newTaskOrder: string,
    taskId: string
}) => {
    try {
        const response = await axiosInstance.post('/boards/taskBox/task/move/other', taskData);
        return response.data;
    } catch (error) {
        throw error;
    }
};