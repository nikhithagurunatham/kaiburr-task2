package com.example.demo;

import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService service;

  public TaskController(TaskService service) {
    this.service = service;
  }

  @GetMapping
  public List<Task> getAll() { return service.getAll(); }

  @GetMapping("/{id}")
  public Task get(@PathVariable String id) { return service.get(id); }

  @PutMapping
  public Task create(@RequestBody Task task) { return service.create(task); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String id) { service.delete(id); }

  @GetMapping("/search")
  public List<Task> search(@RequestParam String name) { return service.search(name); }

  @PutMapping("/{id}/execute")
  public String execute(@PathVariable String id) throws IOException, ApiException {
    return service.execute(id);
  }
}
