package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.TasksRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.utils.TasksUtils;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.wrapper.TaskDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {
	@Autowired
	private TasksRepo tasksRepo;

	private Logger logger = LoggerFactory.getLogger(TaskService.class);

	//method should check if project already exists

	public boolean successfullyCreateNewTask(TaskDetails taskDetails){
		final String tenantEmail = getUsernameFromSecurityContext();
		if(taskDetailsNull(taskDetails)){
			logger.warn("Tried to make a task for '{}' but the task was of illegal type or null", tenantEmail);
			return false;
		}
		String taskAssignedTo = taskDetails.getAssignedTo();
		String taskTitle = taskDetails.getTitle();
		String taskProject = taskDetails.getTaskProject();
		if(taskByTitleAndAssignedToExists(taskTitle, taskAssignedTo)){
			logger.warn("Tried to make a task for '{}' with title '{}', but that task already exists", taskAssignedTo, taskTitle);
			return false;
		}

		Tasks task = new Tasks();
		LocalDateTime localDateTime = LocalDateTime.now();

		task.updateTaskFromDetails(taskDetails);
		taskDetails.setAssignedTo(tenantEmail);
		task.setId(generatedIdFrom(tenantEmail+task.getTitle()));
		task.setTaskProject(taskProject);
		task.setCreatedAt(localDateTime);
		task.setUpdatedAt(localDateTime);
		Tasks savedTask = tasksRepo.save(task);
		logger.info("Task '{}' created for user '{}'", savedTask.getTitle(), tenantEmail);
		return true;
	}

	public boolean successfullyEditTaskByTitle(TaskDetails taskDetails){
		//things to edit ->  notes, dueDate, status
		final String email = getUsernameFromSecurityContext();

		if(taskDetailsNull(taskDetails)){
			logger.warn("Tried to edit a null task or a task with empty/null values");
			return false;
		}
		String taskTitle = taskDetails.getTitle();
		String taskAssignedTo = taskDetails.getAssignedTo();
		if(taskByTitleAndAssignedToExists(taskTitle, taskAssignedTo)){
			Tasks task = tasksRepo.findByTitleAndAssignedTo(taskTitle, taskAssignedTo).get();
			task.updateTaskFromDetails(taskDetails);
			tasksRepo.save(task);
			logger.info("Successfully updated task '{}' for '{}' with new values", task.getTitle(), email);
			return true;
		}
		logger.warn("Tried to edit a task that does not exist");
		return false;
	}

	public List<Tasks> getPendingTasksByDueDate(){
		//get all user tasks
		final String email = getUsernameFromSecurityContext();
		logger.info("Getting pending tasks for '{}'", email);
		List<Tasks> pendingTasks = tasksRepo.findByAssignedToAndStatus(email, "PENDING");
		List<Tasks> orderedByDateDue = TasksUtils.sortTasksByDueDate(pendingTasks);
		logger.info("tasks: '{}'",orderedByDateDue);
		return orderedByDateDue;
	}

	public Tasks getLastWorkedOnTask(){
		final String email = getUsernameFromSecurityContext();
		logger.info("Getting last updated task for '{}'", email);
		List<Tasks> tasks = tasksRepo.findByAssignedTo(email);
		return TasksUtils.getLastUpdatedTask(tasks);
	}

	public boolean setTaskAsComplete(TaskDetails taskDetails){ //TODO: better to use a StringWrapper
		final String assignedTo = getUsernameFromSecurityContext();
		String taskTitle = taskDetails.getTitle();
		Optional<Tasks> taskToMark = tasksRepo.findByTitleAndAssignedTo(taskTitle, assignedTo); //not working
		logger.info("Setting '{}' task as complete for '{}'", taskTitle, assignedTo);
		if(taskToMark.isEmpty()){
			logger.warn("Tried to mark a task that does not exist as complete");
			return false;
		}
		taskToMark.get().setStatus("COMPLETE");
		tasksRepo.save(taskToMark.get());
		logger.info("Successfully marked task assigned to '{}' of title '{}' as complete", assignedTo, taskTitle);
		return true;
	}

	private boolean taskByTitleAndAssignedToExists(String title, String assignedTo){
		Optional<Tasks> task = tasksRepo.findByTitleAndAssignedTo(title, assignedTo);
		if(task.isEmpty()){
			return false;
		}
		return true;
	}

	private boolean taskDetailsNull(TaskDetails task) {
		return task==null;
	}

	private String generatedIdFrom(String string){
		return UUID.nameUUIDFromBytes(string.getBytes()).toString();
	}

	private String getUsernameFromSecurityContext(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
