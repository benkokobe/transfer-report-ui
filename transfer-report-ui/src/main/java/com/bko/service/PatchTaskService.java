package com.bko.service;

import java.util.List;

import com.bko.domain.PatchTask;


public interface PatchTaskService {
	public List<PatchTask> getPatchId(String taskId);
}
