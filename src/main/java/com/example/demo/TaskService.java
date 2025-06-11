package com.example.demo;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.Config;

@Service
public class TaskService {
  private final TaskRepository repo;

  public TaskService(TaskRepository repo) {
    this.repo = repo;
  }

  public List<Task> getAll() {
    return repo.findAll();
  }

  public Task get(String id) {
    return repo.findById(id).orElse(null);
  }

  public Task create(Task task) {
    return repo.save(task);
  }

  public void delete(String id) {
    repo.deleteById(id);
  }

  public List<Task> search(String name) {
    return repo.findByNameContainingIgnoreCase(name);
  }

  public String execute(String id) throws IOException, ApiException {
    Task task = repo.findById(id).orElse(null);
    if (task == null) return "Task not found";

    ApiClient client = Config.defaultClient();
    io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
    CoreV1Api api = new CoreV1Api();

    String podName = "task-" + id.toLowerCase();

    V1Pod pod = new V1Pod()
      .metadata(new V1ObjectMeta().name(podName))
      .spec(new V1PodSpec()
        .containers(List.of(new V1Container()
          .name("busybox")
          .image("busybox")
          .args(List.of("sh", "-c", task.command))))
        .restartPolicy("Never"));

    // Fixed: add 6th parameter null here
    api.createNamespacedPod("default", pod, null, null, null, null);

    task.taskExecutions.add("Started pod: " + podName);
    repo.save(task);

    return "Started pod: " + podName;
  }
}
