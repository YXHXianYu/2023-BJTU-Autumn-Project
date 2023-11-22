package com.yxhxianyu.peerlearningsystem.utils.algo;

import java.util.*;

/**
 * @author YXH_XianYu
 * @date 2023/11/16 21:36
 **/
public class TaskAllocationAlgorithm {

    /**
     * 互评任务分配算法（构造性算法）
     * @param n 学生数量
     * @param m 每个同学的互评任务数量
     * @return 映射关系
     */
    public static List<List<Integer>> allocateTasks(int n, int m) {
        List<List<Integer>> tasks = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            tasks.add(new ArrayList<>(m));
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                tasks.get(i).add((i + j + 1) % n + 1);
            }
        }

        return tasks;
    }

    /**
     * 互评任务分配算法（随机构造性算法）
     * @param n 学生数量
     * @param m 每个同学的互评任务数量
     * @return 映射关系
     */
    public static List<List<Integer>> allocateTasksRandom(int n, int m) {
        List<List<Integer>> tasks = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            tasks.add(new ArrayList<>(m));
        }

        List<Integer> list = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            list.add(i);
        }
        Collections.shuffle(list);

        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < n; i++) {
            map.put(list.get(i), i);
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
//                tasks.get(i).add(map.get(list.get((i + j + 1) % n)) + 1);
                tasks.get(list.get(i)).add(list.get((i + j + 1) % n) + 1);
            }
        }

        return tasks;
    }
}
