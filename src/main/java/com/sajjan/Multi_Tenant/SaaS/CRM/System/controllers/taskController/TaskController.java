package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.taskController;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.TaskService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.wrapper.TaskDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	TaskService taskService;

	@PostMapping("/createNewTask")
	public ResponseEntity createNewTask(@RequestBody TaskDetails taskDetails){
		boolean  success = taskService.successfullyCreateNewTask(taskDetails);
		ResponseEntity response = success ?
				ResponseEntity.ok("New task successfully created"+taskDetails)
				: ResponseEntity.badRequest().body("Tried to create a task but it already exists or invalid credentials provided");
		return response;
	}

	@PostMapping("/createNewSelfTask")
	public ResponseEntity createNewSelfTask(@RequestBody TaskDetails taskDetails){
		boolean  success = taskService.successfullyCreateNewTask(taskDetails);
		ResponseEntity response = success ?
				ResponseEntity.ok("New task successfully created"+taskDetails)
				: ResponseEntity.badRequest().body("Tried to create a task but it already exists or invalid credentials provided");
		return response;
	}

	@PostMapping("/editTask")
	public ResponseEntity editTask(@RequestBody TaskDetails taskDetails){
		boolean success = taskService.successfullyEditTaskByTitle(taskDetails);
		ResponseEntity response = success ?
				ResponseEntity.ok("Task successfully edited with task details: "+taskDetails)
				: ResponseEntity.badRequest().body("Tried to edit a task but provided invalid credentials or task does not exist");
		return response;
	}

	@GetMapping("/getPendingTasksByDueDate")
	public ResponseEntity getPendingTasksByDueDate(){
		List<Tasks> tasks = taskService.getPendingTasksByDueDate();
		return ResponseEntity.ok(tasks);
	}

	@GetMapping("/getLastUpdatedTask")
	public ResponseEntity getLastUpdatedTask(){
		Tasks task = taskService.getLastWorkedOnTask();
		return ResponseEntity.ok(task);
	}

	@PostMapping("/markTaskAsComplete")
	public ResponseEntity markTaskAsComplete(@RequestBody TaskDetails taskDetails){
		boolean success = taskService.setTaskAsComplete(taskDetails);
		ResponseEntity response = success ?
				ResponseEntity.ok("Successfully marked task as complete")
				: ResponseEntity.badRequest().body("Tried to mark task as complete but provided invalid credentials or task does not exist");
		return response;
	}
}
