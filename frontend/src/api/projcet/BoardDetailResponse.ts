import {Task} from "./Task.ts";

export interface BoardDetailResponse {
    taskBoxId: number;
    taskBoxOrder: number;
    taskBoxTitle: string;
    taskDetailResponses: Task[];
}