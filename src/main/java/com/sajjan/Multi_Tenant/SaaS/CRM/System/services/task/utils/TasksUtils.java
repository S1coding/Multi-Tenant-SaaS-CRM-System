package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.utils;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class TasksUtils {
	public static List<Tasks> sortTasksByDueDate(List<Tasks> tasks) {
		tasks.sort(Comparator.comparing(Tasks::getDueDate)); //very cool interface for ordering
		return tasks;
	}

	public static Tasks getLastUpdatedTask(List<Tasks> tasks){
		tasks.sort(Comparator.comparing(Tasks::getUpdatedAt));
		return tasks.get(tasks.size()-1);
	}
}