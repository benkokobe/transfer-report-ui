package com.bko.persistence;

import java.util.List;

import com.bko.domain.PatchTask;




public interface PatchTaskDao {
	List<PatchTask> getPatchId(String taskId);
}
