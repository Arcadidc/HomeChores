package com.example.arcadi.treballfigrau.utils;

import com.example.arcadi.treballfigrau.general.Task;

import java.util.Comparator;

public class CustomComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {

        return o2.getDate_end().compareTo(o1.getDate_end());
    }
}